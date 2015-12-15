#! python

# pylint: disable=broad-except

from xml.etree import ElementTree
import sys,os
import stat
import json
import xmltodict
import argparse
import logging
import signal
from urlparse import urljoin
import urllib
import glob
import ssl
import cStringIO
import re
import datetime
import StringIO

#  Apalling monkey patch hack to get around SSL certificate verification issue under Python for OSX.
#  This is actually version specific in strange ways.
from sys import platform as _platform
if _platform == "darwin":
    if '_create_unverified_context' in dir(ssl):
        ssl._create_default_https_context = ssl._create_unverified_context


# DataOne libraries
import d1_common
import d1_client.mnclient as mnclient
import d1_client_cli.impl.access_control as access_control
import d1_common.types.exceptions as d1_exception
import d1_common.checksum
import d1_common.const
import d1_common.types.generated.dataoneTypes as dataoneTypes


# Something of a magic value. Default for EML system it seems
hasher_algorithm = u'MD5'

# Globals

args = None
logger = None
sysmeta_generator = None
error = None


# Modified from dataone d1_client_cli.impl.system_metadata.py
# Replaced use of session values by parameters and local values

# Some of these values might need updating or overriding in the future.
# Based upon current usage.

class SystemMetadataCreator():
    """
    Create a system metadata object from scratch
    """
    
    def __init__(self):
        self._format_id = 'eml://ecoinformatics.org/eml-2.1.1'
        self._algorithm = hasher_algorithm
        self._authoritative_mn = 'urn:node:TERN'
        self._replication = {'preferred-nodes' : [],
                             'blocked-nodes' : [],
                             'replication-allowed' : True,
                             'number-of-replicas' : 1}
        
    def set_format_id(self, format):
        self._format_id = format

    def set_hash_algorithm(self, algo):
        self._algorithm = algo

    def set_authoritative_mn(self, mn_name):
        self._authoritative_mn = mn_name
    
    def create_system_metadata(self, content, pid, authenticated_user, checksum_string, identifier_old = None):
        file_size = len(content)
        if checksum_string is not None:
            checksum = dataoneTypes.checksum(checksum_string)
        else:
            checksum = dataoneTypes.checksum(self._get_string_checksum(content))
        access = access_control.AccessControl()
        access.add_allowed_subject('public', 'read')
        access.add_allowed_subject(authenticated_user, 'read')
        access.add_allowed_subject(authenticated_user, 'write')
        access.add_allowed_subject(authenticated_user, 'changePermission')
        
        sysmeta =  self._create_pyxb_object(pid,
                                            self._format_id,
                                            file_size,
                                            checksum,
                                            authenticated_user,
                                            access)
        
        if identifier_old is not None:
            sysmeta.obsoletes = identifier_old            
        return sysmeta            
    
    def _create_pyxb_object(self, pid, format_id, file_size, checksum, authenticated_user, access):
        print "create pxyb"
        now = datetime.datetime.utcnow()
        sys_meta = dataoneTypes.systemMetadata()
        sys_meta.serialVersion = 1
        sys_meta.identifier = pid
        sys_meta.formatId = format_id
        sys_meta.size = file_size
        sys_meta.rightsHolder =  authenticated_user
        sys_meta.checksum = checksum
        sys_meta.checksum.algorithm = self._algorithm
        sys_meta.dateUploaded = now
        sys_meta.dateSysMetadataModified = now
        sys_meta.authoritativemn = self._authoritative_mn
        sys_meta.accessPolicy = self._create_access_policy_pyxb_object(access)
        sys_meta.replicationPolicy = self._create_replication_policy_pyxb_object()
        return sys_meta
        
    def _create_access_policy_pyxb_object(self, access):
        acl = access.get_list()
        if not len(acl):
            return None
        access_policy = dataoneTypes.accessPolicy()
        for s, p in acl:
            access_rule = dataoneTypes.AccessRule()
            access_rule.subject.append(s)
            permission = dataoneTypes.Permission(p)
            access_rule.permission.append(permission)
        access_policy.append(access_rule)
        return access_policy
        

    def _create_replication_policy_pyxb_object(self):
        r = self._replication
        access_policy = dataoneTypes.ReplicationPolicy()
        for preferred in r['preferred-nodes']:
            node_reference = dataoneTypes.NodeReference(preferred)
            access_policy.preferredMemberNode.append(node_reference)
        for blocked in r['blocked-nodes']:
            node_reference = dataoneTypes.NodeReference(blocked)
            access_policy.blockedMemberNode.append(node_reference)
        access_policy.replicationAllowed = r['replication-allowed']
        access_policy.numberReplicas = r['number-of-replicas']
        return access_policy

    def _get_file_size(self, path):
        return os.path.getsize(os.path.expanduser(path))


    def _get_file_checksum(self, path, algorithm = hasher_algorithm, block_size=1024 * 1024):
        with open(os.path.expanduser(path), u'r') as f:
            return self._get_flo_checksum(f, algorithm, block_size)

    def _get_string_checksum(self, string, algorithm = hasher_algorithm, block_size=1024 * 1024):
        return self._get_flo_checksum(StringIO.StringIO(string), algorithm, block_size)

    def _get_flo_checksum(self, flo, algorithm = hasher_algorithm, block_size=1024 * 1024):
        h = d1_common.checksum.get_checksum_calculator_by_dataone_designator(algorithm)
        while True:
            data = flo.read(block_size)
            if not data:
                break
            h.update(data)
        return h.hexdigest()

class InternalError:
    # Provide an exception to allow us to bail out
    pass
    
class errors:
    """
    Provides a simple class to encapsulate a limit on the number of run-time recoverable errors, to log this
    and to the program.
    """
    def __init__(self, limit, exception = InternalError):
        self._count = 0
        self._limit = limit
        self._exception = exception
        
    def error(self):
        if self._limit == -1:
            return
        self._count += 1
        if self._count > self._limit:
            logger.info("Runtime limit of {} errors exceeded".format(self._limit))
            if self._exception is not None:
                raise self._exception
            else:
                raise InternalException
            
def signal_exit_handler(signum, frame):
    """
    Provide a mechanism to log if the program is terminated via an external signal (ie a kill command).
    """
    logger.info("Exiting with signal {}".format(signum))
    # Exit instantly
    sys.exit(0)
    
class Connector(object):
    """
    Abstract class base for classes that talk to sources and sinks of eml data
    """    
    def __init__(self):
        self._eml_packages = {}
    
    def get_iterator(self):
        return self.__iter__()
    
    def idents(self):
        return self._eml_packages.keys()

    def get_package(self, ident):
        return self._eml_packages[ident]
    
    def __iter__(self):
        for id in self._eml_packages.keys():
            yield self._eml_packages[id]
            
    def update(self, package, update):
        pass

    def update_sysmeta(self, content, update):
       pass

def eml_idents(full_id):    
    try:
        package_id = full_id[0: -9]
        timestamp = int(full_id[-8:])
    except ValueError, IndexError:
        # Assume that there is no timestamp
        package_id = full_id     # use the entire id
        timestamp = 0            # start of time

    # some simple sanity checking, 
    if timestamp <= 0:
        package_id = full_id
        timestamp = 0
    if len(package_id) == 0:
        if len(full_id) == 0:
            logger.debug("Empty package id")
            raise ValueError
        package_id = full_id  # Get here if the id is very short
        timestamp = 0         # start of time
    return (package_id, timestamp)

class eml_component:
    """
    Class encapulates the eml data being processed and provides a few utility functions
    to help with processing.
    As much as possible we avoid calculating anything unless we need it - especially if this involves
    talking to a DataOne node
    """
    
    def __init__(self, source, content = None, sysmeta = None):

        self._source = source      #  the Connector object that created us - used for lazy creation/obtaining info as a callback
        self._content = content    #  String holding the XML package content
        self._dict = None          #  Cached dictionary used to compare package contents for sematic equivalence - not currently used
        self._sysmeta = None 
        self._sysmeta_content = None    #  A DataOne sysmeta object - lazily created if not provided
        self._checksum = None      #  Holds the checksum for this package as calculated with the timestamp the package holds
        self._full_package_id = None   # The Pid that DataNone will refere to this with
        self._package_id = None        # The package id without the timestamp
        self._timestamp =  None        # The package's timestamp
        self._authenticated_user = None  # The user ident found in the content xml file

        if content is not None:
            try:
                e = ElementTree.fromstring(content)
                self._full_package_id = e.attrib['packageId']
                self._package_id,  self._timestamp =  eml_idents(self._full_package_id)
                dataset = e.findall('dataset')
                creators = []
                for x in dataset[0].findall('creator'):
                    creators.append(x.attrib['id'])

                # Use the first creator - we may wish to change this to do something like add all creators to the authenticated users
                if len(creators) >= 1:
                    self._authenticated_user =  creators[0]
                
            except KeyError as ex:
                # whilst this is a .xml file, it would not appear to be the right content
                logger.debug("Missing package id or owner id")
                raise ValueError
            except ElementTree.ParseError as ex:
                # Something bad has happened.
                logger.debug("Failure in parsing XML {}".format(ex))
                raise ValueError
            except Exception as ex:
                logger.exception("Exception in XML parse.", exc_info = ex)
                raise ValueError            
            
        if content is not None and self._sysmeta is not None:
            self._check_sanity()        
            
    def set_pid(self, pid):
        if self._full_package_id is not None:
            raise InternalError("Object {} already has a pid".format(self._full_package_id))
        self._full_package_id = pid
        self._package_id,  self._timestamp =  eml_idents(pid)
        
    def get_pid(self):
        return self._full_package_id

    def _get_dict(self):
        # Do this lazily - it probably won't be needed all that often
        # Currently don't use this - simply use checksums.
        if self._dict is None:
            self._dict = json.loads(json.dumps((xmltodict.parse(self._content_no_package_id()))))
        return self._dict

    def _check_sanity(self):
        if self._full_package_id != self._sysmeta.package():
            raise ValueError("Mismatch in package id {}  {}".format(self._full_package_id, self._sysmeta.package()))
    
    def same_as(self, other):
        # Packages are semantically identical
        return self._get_dict() == other._get_dict()

    def set_checksum(self, checksum):
        self._checksum = checksum.lower()
    
    def same_checksum(self, other):
        """
        A cheap way of checking for identical components.  If the checksums are the same they are byte for byte identical.
        This does not cover semantically identical components. We use the same_as() method for that.

        By slamming the other package with our timestamp we can see if they would be identical if only the timestamps were the same.
        """        
        if self._checksum is None:
            self._checksum = self.checksum()
        other_checksum = other.checksum(str(self._timestamp))
        return self._checksum == other_checksum
    
    def package_id(self):
        return self._package_id

    def full_package_id(self):
        return self._full_package_id

    def timestamp(self):
        return self._timestamp
    
    def content(self):
        return self._content

    def content_reader(self):
        return cStringIO.StringIO(self._content)
    
    def _content_no_package_id(self):
        # Elide the string "packageId ....  ' ' " from the string
        # Effectively removes the XML definition of the packageid
        try:
            first_char = self._content.index("packageId")
            last_char  = self._content.index(" ", first_char)
            return self._content[0 : first_char] + self._content[last_char:]
        except (KeyError, IndexError):
            # We will assume there was never a package id present
            return self._content 

    def _insert_packageid(self, id):
        if self._content is None:
            return None
        try:
            first_char = self._content.index("packageId")
            last_char  = self._content.index(" ", first_char)
            # Timestamps are 8 chars long. Also, we need to avoid the closing quote
            return self._content[0 : last_char - 9] + id + self._content[last_char - 1:]
        except (KeyError, IndexError):
            # We will assume there was never a package id present
            return None
        
    def checksum(self, timestamp = None):
        """
        Find the hash of the package contents.  Optionally calculate the checksum as if the package had a different timestamp.
        """
        checksum = None

        if timestamp is None:
            return self._get_checksum()
        
        if self._sysmeta is not None and timestamp is None:
            # Use the checksum that came in the sysmeta data
            self._checksum = self.get_sysmeta().checksum().lower()
            return self._checksum

        if timestamp is None:
            # Get the checksum from the source
            if self._content is not None:
                content =  self._content
            else:
                checksum = self._source.get_checksum(self._full_package_id).lower()
                return checksum
        else:
            if len(timestamp) != 8:
                raise ValueError("Bad timestamp")
            packageId = self._package_id + "." + timestamp
            content = self._insert_packageid(timestamp)
                
        hasher = d1_common.checksum.get_checksum_calculator_by_dataone_designator(hasher_algorithm)
        hasher.update(content)
        return hasher.hexdigest().lower()

    def _get_checksum(self):
        if self._checksum is None:
            if self._sysmeta is not None:
                self._checksum = self._sysmeta.checksum().lower()
            else:
                if self._content is not None:
                    hasher = d1_common.checksum.get_checksum_calculator_by_dataone_designator(hasher_algorithm)
                    hasher.update(self._content)
                    self._checksum = hasher.hexdigest().lower()
                else:
                    self._checksum = self._source.get_checksum(self._full_package_id).lower()
        return self._checksum
            
    def get_sysmeta(self, obsoletes = None):
        """
        Return the sysmeta object that describes this eml content.
        Lazyily evaluated to avoid the cost of fetching/generating if not needed
        """
        if self._sysmeta is None:
            self._sysmeta = self._generate_sysmeta(obsoletes)
        return self._sysmeta

    def get_sysmeta_content(self, obsoletes = None):
        """
        Return the sysmeta object that describes this eml content.
        Lazyily evaluated to avoid the cost of fetching/generating if not needed
        """        
        if self._sysmeta_content is None:
            sys_meta = self._generate_sysmeta(obsoletes)
            self._sysmeta_content = sys_meta.toxml().encode('utf-8')
        return self._sysmeta_content
        
    def _generate_sysmeta(self, obsoletes):
        # Use the DataOne library to generate a sysmeta object
        if self._sysmeta is None:
            # We have no content - so assume we must generate sysmetadata from the main content
            self._sysmeta = sysmeta_generator.create_system_metadata(self._content, self._full_package_id, self._authenticated_user, self._checksum, obsoletes )
        return self._sysmeta
 
    def _get_dict(self):
        # Do this lazily - it probably won't be needed all that often - if at all
        if self._dict is None:
            self._dict = json.loads(json.dumps((xmltodict.parse(self._content_no_package_id()))))
        return self._dict

    def package_id(self):
        return self._package_id

    def timestamp(self):
        return self._timestamp
    
    def content(self):
        return self._content
            

class file_connector(Connector):
    """
    Class provides for sources and sinks of eml data in a local file system
    """
    def __init__(self, root_dir):
        super(file_connector, self).__init__()
        self._root_dir = root_dir
        logger.info("Using file connector for {}".format(root_dir))
        try:
            # for all the local eml files
            for (path, dirs, files) in os.walk(self._root_dir):
                for instance in files:
                    if not instance.endswith('.xml'):
                        continue
                    filename = os.path.join(path, instance)

                    # find packageId from the XML file
                    e = None
                    package_id = None
                    try:
                        with open(filename) as the_file:
                            content = the_file.read()
                    except IOError:
                        # Empty or unable to read
                        logger.info("Skipped unreadable file {}".format(filename))
                        continue
                    except Exception as ex:
                        logger.exception("Exception in XML file read for {}".format(filename), exc_info = ex)
                        error.error()
                        continue
                    
                    try:
                        new_component = eml_component(self, content)
                    except ValueError:
                        logger.debug("Skipped bad file {}".format(filename))
                        continue                    
                        
                    new_base_id = new_component.package_id()

                    if new_base_id in self._eml_packages.keys():
                        current_time = self._eml_packages[new_base_id].timestamp()
                        new_time = new_component.timestamp()
                        if new_time > current_time:                            
                            self._eml_packages[new_base_id] = new_component
                        else:
                            logger.debug("Skipped file with older timestamp {}".format(filename))
                    else:
                        self._eml_packages[new_base_id] = new_component
                        logger.info("Using package {}".format(new_base_id))
 
        except Exception as ex:
            logger.exception("Exception in file connection {}. Exiting".format(self._root_dir), exc_info = ex)
            raise InternalError
        logger.info("File connector finds {} eml objects".format(len(self._eml_packages)))


    # These functions are primarily used for testing, but do allow the tool to be used
    # to sychronise two directories of files, if that is ever desirable.
        
    def create(self, package):
        self.update(None, package)
        
    def update(self, existing, package):
        if existing is not None:
            oldPid = existing.get_pid()
        else:
            oldPid = None
        if args.trial_run:
            return
        if package is None:
            return
        try:
            filename = urllib.quote(package.full_package_id(), safe = "" ) + ".xml"
        except UnicodeEncodeError as ex:
            logger.exception("Unencodable byte in package name.  File not saved.", ex)
            error.error()
            return
            
        try:
            with open( os.path.join(self._root_dir, filename), "w" ) as the_file:
                the_file.write(package.content())
        except Exception as ex:
            logger.exception("Failure to create package file {} in {}".format(filename, self._root_dir), exc_info = ex)
            error.error()
            return
        
        self._update_sysmeta(package, package.get_sysmeta_content(existing))
        

    def _update_sysmeta(self, package, sysmeta):        
        try:
            filename = urllib.quote(package.full_package_id(), safe = "" ) + "-sysmeta.xml"
        except UnicodeEncodeError as ex:
            logger.exception("Unencodable byte in package name.  File not saved.", ex)
            error.error()
            return
        
        try:
           with open(os.path.join(self._root_dir, filename), "w") as the_file:
               the_file.write(sysmeta)
        except Exception as ex:
            logger.exception("Error during writing of sysmeta {} to {}".format(filename, self._root_dir), exc_info = ex)
            error.error()

    def get_checksum(self, pid):
        # For completness - since a file connector has read the xml content for the eml_content object
        # this function should not be called
        raise InternalError("get_checksum for file connector called")
        return "should have one"
            
                
class dataone_connector(Connector):
    """
    Class provides for sources and sinks of eml data that are provided by a DataOne repository
    """
    def __init__(self):
        super(dataone_connector, self).__init__()
        
        self._host_url = d1_common.url.makeMNBaseURL(args.destination_url)

        logger.info("Connecting to DataOne MN {}".format(self._host_url))
        if args.cert_file:
            cert_file = glob.glob(os.path.expanduser(os.path.expandvars(args.cert_file)))[0]

            if os.path.isfile(cert_file):
            
                mode = os.stat(cert_file).st_mode
                if stat.S_IRUSR & mode :
                    if mode & (stat.S_IRWXG | stat.S_IRWXO) :
                        logger.error("Cert file {} has insecure permissions".format(cert_file))
                        raise InternalError
                else:
                    logger.error("Cert file {} is not readable".format(cert_file))
                    raise InternalError
            else:
                logger.error("Cert file is not a plain file")
                raise InternalError

#  For preference we should be able to use these functions rather than the cert file directly.
#  The DataOne implementation is too old it seems
#            ssl_context = ssl.SSLContext(ssl.PROTOCOL_SSLv3)
#            ssl_context.load_cert_chain(cert_file)

        else:            
#            ssl.create_default_context(ssl.Purpose.CLIENT_AUTH)
            cert_file = None
            
        try:
            self._mn_client = mnclient.MemberNodeClient( base_url = self._host_url, cert_path = cert_file )
        except Exception as ex:
            logger.exception("Failure in connecting to member node {}".format(self._url), exc_info = ex)
            raise InternalError

        # All indexed by package_id (with no timestamp)
        self._eml_sysmeta = {}           # Cache of sysmeta data
        self._mn_client_checksums = {}   # chache of checksums
        self._eml_packages = {}          # cache of entire packages
        self._eml_timestamp = {}         # cache of eml timestamps

        # This is probably a limit to scalability - however there is little that can be done.
        # The user should restrict the base of the search to cull the returned objects
        try:        
            self._mn_list = self._mn_client.listObjects(count = 1000000 )
            # (fromDate=None, toDate=None, objectFormat=None,
            #           replicaStatus=None, start=0,
            #           count=d1_common.const.DEFAULT_LISTOBJECTS,
            #           vendorSpecific=None):

        except Exception as ex:
            logger.exception("Unable to get package list from mnclient", exc_info = ex)
            raise InternalError

        # Create the dictionary of content.  
        for obj in self._mn_list.orderedContent():
            try:
                package_id, timestamp =  eml_idents(obj.value.identifier.value())
            except ValueError:
                continue
            self._eml_packages[package_id] = eml_component(self)
            # Since we don't download the package for the eml_component we need to explicitly set the pid
            self._eml_packages[package_id].set_pid(obj.value.identifier.value())
            # We can also add the checksum right away, which can save time later.
            self._eml_packages[package_id].set_checksum(obj.value.checksum.value())
#            self._eml_packages[package_id].set_????(obj.value.dateSysMetadataModified.value())  # Might be useful  - not bothering for now.

        logger.info("Found {} eml objects on MN".format(len(self._eml_packages)))
            
    def _get_content_from_server(self, ident):
        # Not used, could be if we enable the code to slurp data from the MN
        try:
            record_content = self._nm_client.get(ident) 
        except Exception as ex:
            logger.exception("Error in GET for ident {} from mnclient".format(ident), exc_info = ex)
            error.error()
            return None
        return record_content

    def update(self, existing, package):
        """
        existing is the package we are replacing
        package is the new package to be uploaded
        """
        pid = package.get_pid()
        oldPid = existing.get_pid()
        logger.info("Updating {} to {}".format(oldPid, pid))
        if args.trial_run:
            return
        try:
#            self._mn_client.reserveIdentifier(newPid)   # May not really need to do this            
            metadata = package.get_sysmeta()
            if not self._mn_client.update(oldPid, package.content_reader(), pid, metadata):
                logger.error("Failure to queue update for newPid {}".format(pid))
                error.error()
                    
        except Exception as ex:
            logger.exception("", exc_info = ex)
            error.error()

    def create(self, package):
        """
        Create a new package on the MN
        """
        newPid = package.get_pid()
        logger.info("Creating new with Pid {}".format(newPid))
        if args.trial_run:
            return
        ##        We could loop and try to ensure a unique id, but we are assuming uniqueness
        ##        has been guarenteed by the timestamp appended.  So there is no value currently.
        #        if not self._mn_client.reserveIdentifier(newPid):
        #            logger.error("Failure to reserve new Pid {}".format(newPid))
        #            error.error()
        #            return
        try:
            self._mn_client.create(newPid, package.content_reader(), package.get_sysmeta())
        except d1_exception.IdentifierNotUnique:
            logger.error("Identifier {} not unique".format(newPid))
            error.error()
            return
            
    def get_package(self, ident):
        # Lazy evaluation of package - try to avoid the expense of getting the content
        if ident not in self._eml_packages.keys():
            return None
        if self._eml_packages[ident] is None:
            content = self._get_content_from_server(ident)
            package = eml_content(content)
            self._eml_packages[ident] = package
            if ident != package.package_id():
                logger.info("Packageid: {} does not match ident: {}".format(package_id, ident))
                # We may want to provide for some option to not proceed if this does not match
                # This is a "should not happen"
                    
        return self._eml_packages[ident]

    def get_checksum(self, pid):
        if pid not in self._mn_client_checksums.keys():
            try:
                self._mn_client_checksums[pid] =  self._mn_client.getChecksum(pid).value()
            except Exception as ex:
                logger.exception("Failure to find checksum from MN for pid {}".format(pid), exc_info = ex)
                error.error()
                return None
        return self._mn_client_checksums[pid]
            
    def get_sysmeta(self, ident):
        # Lazy fetch of sysmetadata from the DataOne MN
        package_id, timestamp =  eml_(ident)
        if package_id in self._eml_sysmeta.keys():
            return self._eml_sysmeta[package_id]
        try:
            self._eml_sysmeta.keys[package_id] = self._mn_client.getSystemMetadata(ident)
        except Exception as ex:
            logger.exception("Failure in getSystemMetadata", exc_info = ex)
            error.error()
        return self._eml_sysmeta[package_id]

    
def perform_update(source, destination):
    same_content = 0
    updated_content = 0
    new_content = 0
    same_timestamp = 0
    
    logger.info("EML Update starts.")

    for new_package in source:
        logger.info("Processing source package: {} with time {}".format(new_package.package_id(), new_package.timestamp()))
        ident = new_package.package_id()
        if ident in destination.idents():
            existing = destination.get_package(ident)
            if new_package.timestamp() > existing.timestamp():
                if not args.force_update:
                    if existing.same_checksum(new_package):
                        same_content += 1
                        logger.debug("Record {} :  Identical content already on server. Not updated.".format(ident))
                    else:
                        logger.debug("Record {} :  New content for existing record. Updating.".format(ident))
                        updated_content += 1
                        destination.update(existing, new_package)
                else:
                    # Update no matter what - don't trust the checksum test.
                    logger.debug("Record {} :  Forcing update to exisiting content.".format(ident))
                    updated_content += 1
                    destination.update(existing, new_package)
            else:
                same_timestamp += 1
                logger.debug("Record {} : Timestamps same.".format(ident))
        else:
            logger.debug("Record {} :  New content, uploading".format(ident))
            new_content += 1
            destination.create(new_package)

        logger.info("EML Update completes.  {} new files uploaded, {} existing updated.  {} files with identical content and {} with same timestamp ignored."
                    .format(new_content, updated_content, same_content, same_timestamp)  )

        
def get_arg_parser():
    parser = argparse.ArgumentParser("eml_pusher", description='Create list of EML files that must be uploaded to a DataOne server')
    parser.add_argument('-t', '--trial_run',  action = 'store_true', help = 'Do not upload to the DataOne server, will describe what needs to be done')
    parser.add_argument('-v', '--verbose',    action = 'store_true', help = 'Enable informational messages')
    parser.add_argument('-V', '--debug',      action = 'store_true', help = 'Enable debugging messages')
    parser.add_argument('-q', '--quiet',      action = 'store_true', help = 'Do not output anything to standard output or standard error')
    parser.add_argument('-i', '--intolerant', action = 'store_true', help = 'Do not perform any uploads if any errors at all are found')
    parser.add_argument('-f', '--force_update',  action = 'store_true', help = 'Do not check if the content to be uploaded is identical to what is on the server.  Upload anyway.')
    parser.add_argument('-l', '--log_file',                         help = 'Log file. If not specified output goes to standard output')
    parser.add_argument('-c', '--config_log_file',                  help = 'Logging configuration file. Python logger format.')
    parser.add_argument('-C', '--cert_file',        help = 'Path to certificate file for access to DataOne node.')
    parser.add_argument('-p', '--path',        help = 'Path to base of tree on MN of packages to synchronise. Not used.')

    dest_group = parser.add_mutually_exclusive_group(required = True)
    dest_group.add_argument('-D', '--destination_url',                  help = 'Hostname or IP number for destination DataOne server')
    dest_group.add_argument('-d', '--destination_dir',                  help = 'Path of destination directory tree for EML')    

    parser.add_argument('-s', '--source_dir',                       help = 'Path of source directory tree containing EML files', required = True)    

    return parser


def build_logger():
    """
    Simple logger configuration that allows logging to nothing, standard output, a file, or allows use of a configuration file for more advanced
    logging destinations. Captures warnings to the log as well.
    """
    if args.config_log_file is not None:
        handler = logging.FileConfig(args.config_log_file)
    if args.log_file is not None:
        handler = logging.FileHandler(args.log_file, delay = True)
    else:
        if args.quiet:
            handler = logging.NullHandler()
        else:
            handler = logging.StreamHandler()

    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
    handler.setFormatter(formatter)
    
    logger = logging.getLogger("eml_pusher")
    logger.addHandler(handler)
    logging._srcfile = None
    
    logging.captureWarnings(True)
    logger.setLevel( logging.INFO if args.verbose else logging.WARNING )
    logger.setLevel( logging.DEBUG if args.debug else logger.getEffectiveLevel() )
    return logger
    

def eml_pusher():
    global args, logger, sysmeta_generator, error
    
    parser = get_arg_parser()
    args = parser.parse_args()
    logger = build_logger()
    sysmeta_generator = SystemMetadataCreator()
    error = errors(0 if args.intolerant else 20, InternalError)
    
    signal.signal(signal.SIGHUP, signal_exit_handler)    # Provide for a kill notification to go into the log file
    
    logger.info("EML Push starts.")
    if args.trial_run:
        logger.info("Trial run. No content will actually upload.")
        
    try:
        source =      file_connector(args.source_dir)
        destination = dataone_connector() if args.destination_url is not None else file_connector(args.destination_dir)        
        perform_update(source, destination)
    except KeyboardInterrupt:
        logger.info("Keyboard interrupt halts processing.")
        sys.exit(0)
    except InternalError:
        sys.exit(1)
    except Exception as ex:
        logger.exception("Unhandled exception, exiting.", exc_info = ex)
        sys.exit(1)
        
    sys.exit(0)

if __name__ == "__main__":
    eml_pusher()
