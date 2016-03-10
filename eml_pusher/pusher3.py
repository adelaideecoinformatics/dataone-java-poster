#! python

# pylint: disable=broad-except


from xml.etree import ElementTree
import sys
import os
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
import string
import dateutil.parser


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
import d1_client.objectlistiterator as objectlistiterator
import d1_common.types.generated.dataoneTypes as dataoneTypes
import d1_client


# Globals

args = None
logger = None
error = None
config = None




class PersistentIdentifier:
    """Encapsulates the various timestamped PIDs used for content

    It seems we can get almost anything back as a PID.
    Although TERN/ecoinformatics has a restricted set of possibilities
    there is nothing that says that something quite different might be added by some other
    agency, and thus turn up in our list.  This include UUIDs and hashes.
    We need to deal with format suffixes, and a general amount of grief.

    It would be good if there was a decidable grammar for PIDs and timestamps, but 
    it is probably way too late to create one.  Some simple heiristics is about all we can do.
    """

    _pid_suffixes = ['html','xml']  # Add more as they become apparent
    _minimum_version_length = 1     # This should be more to avoid problems, but we have some items already with only 1 digit
    
    def __init__(self, ident):
        self._full_id = ident
        self._base_id, self._timestamp, self._kind = self._parse_ident()

    def full_id(self):
        """Return a string that represents the PID, including timestamp

        :rtype: string
        """
        return self._full_id
    
    def base_id(self):
        """Return a string that represents the PID with any timestamp (and separator) removed

        :rtype: string
        """
        return self._base_id

    def timestamp(self):
        """Return the integer value of the timestamp

        :rtype: integer
        """
        return self._timestamp

    def kind(self):
        """Return a string that represents the kind of entity the URL provided references if it can be deduced from the URL

        :rtype: string
        """
        return self._id_kind

    def _parse_ident(self):
        """Parse as best we can the provided URL.  

        Returns a triplet: base name, timestamp, kind


        :rtype: tuple (string, integer, string)
        :raises: ValueError

        Note - if we decide the ident is invalid, throw a ValueError
        Currently there are not a lot of ways to get a bad ident.
        """
        if len(self._full_id) == 0:
            raise ValueError

        kind = None
        full_name = self._full_id

        # First - look for a type
        # If there is one split it off.
        elements = self._full_id.split('/')
        if len(elements) >= 2:
            if elements[-1] in self._pid_suffixes:
                kind = elements[-1]
                full_name = string.join(elements[:-1],'/')

        components = full_name.split('.')        # Now go looking for a version
        try:
            if len(components) <= 1:                  # Nothing can be usefully done - no timestamp separator
                return(full_name, 0, kind)            # This will capture UUIDs

            last = components[-1]
            if len(last) < self._minimum_version_length:   # It isn't a valid version number - assume it is part of the name.
                return(full_name,  0, kind)

            timestamp = int(last)
            base_name = string.join(components[:-1], sep = '.')   # Name without timestamp
        except (IndexError, ValueError):   # Last bit isn't a valid number - assume it is all one identifier
            return (full_name, 0, kind)
        
        if timestamp < 0:        #  Negative numbers might sneak through (we could allow this, but probably a bad idea.)
            return(full_name, 0, kind)
        
        return (base_name, timestamp, kind)


class PusherConfiguration:
    """Configuration object encapsulates the configuration of the pusher.

    Configuration defaults are set in __init__
    Any public component may be overridden by a value in the config file
    Only public components can be set from the config file, it is not possible to create new values.
    The types must match 
    """
    def __init__(self, config_file_name):
        """Construct an initial configuration

        :param config_file_name: Path of configuration file
        :type config_file_name: string

        These are the default values
        They are overriden by any values in the config file
        """
        self.hasher_algorithm = 'MD5'                            # Default for DataOne system it seems
        self.authoritative_mn = 'urn:node:TERN'
        self.replication = {'preferred-nodes' : [],
                             'blocked-nodes' : [],
                             'replication-allowed' : True,
                             'number-of-replicas' : 1}

        self.rights_holder = 'authenticatedUser'     # This is ridiculous.
        # We need a much better scheme for this inside the DataOne setup. 

        if config_file_name is not None:
            self._parse_yaml(config_file_name)

    def _assign_config(self, fields, configuration):
        """Recursively traverse the configuration in tandem with the config object and assign values as found in the configuration.

        :param fields: a configuration obgject that will have its fields written
        :type fields: Configuration
        :param configuration: an object as returned by parsing a yaml configuration file
        :type configuration: object
        :raises: InternalError

        Python type checking will avoid the most simple
        of type mismatches, and will catch unknow/incorrect configuration option names.        
        """
        for key in configuration.iterkeys():
            if type(fields) is not dict and key in fields.__dict__:
                if key[:2] == "_":
                    continue   # Don't let some smart alec override private values.
                if type(configuration[key]) is dict:
                    try:
                        self._assign_config(fields.__dict__[key], configuration[key])
                    except (ValueError, AttributeError) as ex:
                        logger.error("Error in config file - no such configuration field: {}".format(key))
                        raise InternalError
                else:
                    try:
                        fields.__dict__[key] = configuration[key]
                    except TypeError as ex:
                        logger.error("Error in config file - type mismatch for {} : {}".format(key, configuration))
                        raise InternalError
            else:
                try:
                    fields[key] = configuration[key]
                except AttributeError as ex:
                    logger.error("Unknown option in configuration file: {}".format(key))
                    raise InternalError
        
            
    def _parse_yaml(self, config_file_name):
        """Loads and parses a yaml config file.  

        :param config_file_name: Path of configuration file containing yaml config
        :type config_file_name: string
        :raises: InternalError

        The file may only contain valid config options, as only items already and the types must match 
        those of the options.  Default values are overridden.
        """
        from yaml import safe_load
        try:
            with open(config_file_name) as config_file:
                configuration = safe_load(config_file)
        except IOError as ex:
            logger.exception("Failure in reading config file", exc_info = ex)
            raise InternalError
        self._assign_config(self, configuration)
             
    def __str__(self):
        """Dump the object as yaml

        The output should be a valid configuration file, hence we remove the top level object header

        :rtype: string
        """
        from yaml import dump as dump
        cont = dump(self).split("\n")
        content = string.join(cont[1:],"\n")    # chop off the object header
        return content
                
class SystemMetadataCreator():
    """Create a system metadata object from scratch

    Some parts modified from dataone d1_client_cli.impl.system_metadata.py
    Replaced use of session values by parameters and local values

    Some of these values might need updating or overriding in the future.
    Based upon current usage.
    """
    
    def __init__(self, data_format):
        self._format_id = data_format.format_id
        self._algorithm = config.hasher_algorithm
        self._authoritative_mn = config.authoritative_mn
        self._replication = config.replication
        self._rights_holder = config.rights_holder

        # Some string constants  # TODO Maybe grab these strings from the DataOne library if they are visible
        self._authenticated_user = 'authenticatedUser'
        self._verified_user = 'verifiedUser'
        self._public_user = 'public'
                
    def set_format_id(self, format):
        self._format_id = format

    def set_hash_algorithm(self, algo):
        self._algorithm = algo

    def set_authoritative_mn(self, mn_name):
        self._authoritative_mn = mn_name
    
    def create_system_metadata(self, content, pid, checksum_string, identifier_old = None):
        file_size = len(content)
        if checksum_string is not None:
            checksum = dataoneTypes.checksum(checksum_string)
        else:
            checksum = dataoneTypes.checksum(self._get_string_checksum(content))
        access = access_control.AccessControl()
        # These policies may need to be the subject of the config file if further flexibility is warranted.
        
        # The next three additions contain an odd issue - never add anything other then read, write, changePermission, or the d1 code will
        # perform an interactive verification of intent - which will kill a batch job.
        # Similarly adding write or change permission to 'authenticated_user' should not be tried.
        # Should never do anything so silly as this anyway.
        
        access.add_allowed_subject(self._public_user, 'read')
        access.add_allowed_subject(self._authenticated_user, 'read')
        access.add_allowed_subject(self._verified_user, 'read')

        sysmeta =  self._create_pyxb_object(pid,
                                            self._format_id,
                                            file_size,
                                            checksum,
                                            self._rights_holder,
                                            access)
        
        if identifier_old is not None:
            sysmeta.obsoletes = identifier_old            
        return sysmeta            
    
    def _create_pyxb_object(self, pid, format_id, file_size, checksum, rights_holder, access):
        now = datetime.datetime.utcnow()
        sys_meta = dataoneTypes.systemMetadata()
        sys_meta.serialVersion = 1
        sys_meta.identifier = pid
        sys_meta.formatId = format_id
        sys_meta.size = file_size
        sys_meta.rightsHolder =  rights_holder
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
        if len(acl) == 0:
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


    def _get_file_checksum(self, path, block_size=1024 * 1024):
        with open(os.path.expanduser(path), u'r') as f:
            return self._get_flo_checksum(f, block_size)

    def _get_string_checksum(self, string, block_size=1024 * 1024):
        return self._get_flo_checksum(StringIO.StringIO(string), block_size)

    def _get_flo_checksum(self, flo, block_size=1024 * 1024):
        h = d1_common.checksum.get_checksum_calculator_by_dataone_designator(self._algorithm)
        while True:
            data = flo.read(block_size)
            if not data:
                break
            h.update(data)
        return h.hexdigest()

class InternalError(Exception):
    # Provide an exception to allow us to bail out cleanly
    pass
    
class errors:
    """Provides a simple class to encapsulate a limit on the number of run-time recoverable errors, and to log this.
    """
    def __init__(self, limit, exception = InternalError):
        """Construct an error handler

        :param limit" The maximum number of erros before an exception is raised
        :type limit: integer
        :param excpetion: Optional exception to raise when the limit is reached.  Default is InternalError
        :type exception: Exception or derived class
        """
        
        self._count = 0
        self._limit = limit
        self._exception = exception
        
    def error(self):
        """Log that an error occured

        :rasies: InternalError or other Exception
        """     
        if self._limit == -1:
            return
        self._count += 1
        if self._count > self._limit:
            logger.info("Runtime limit of {} errors exceeded".format(self._limit))
            if self._exception is not None:
                raise self._exception
            else:
                raise InternalError 
            
    def error_count(self):
        """ Return the current count of errors

        :rtype: integer
        """
        return self._count

    
def signal_exit_handler(signum, frame):
    """Provide a mechanism to log if the program is terminated via an external signal (ie a kill command).

    :param signum: signal number
    :type signum: integer
    :param frame: reference to the frame where the signal was caught
    :type frame: frame
    """    
    logger.info("Exiting with signal {}".format(signum))
    # Exit instantly
    sys.exit(0)

    
class Connector(object):
    """Abstract class base for classes that talk to sources and sinks of data"""
    
    def __init__(self):
        self._packages = {}
    
    def get_iterator(self):
        return self.__iter__()
    
    def idents(self):
        return self._packages.keys()

    def get_package(self, ident):
        return self._packages[ident]
    
    def __iter__(self):
        for id in self._packages.keys():
            yield self._packages[id]
            
    def update(self, package, update):
        pass

    def update_sysmeta(self, content, update):
       pass


class Component(object):
    """Class encapulates the data being processed and provides a few utility functions to help with processing.

    As much as possible we avoid calculating anything unless we need it - especially if this involves
    talking to a DataOne node

    Needs sub-classing to allow it to work with specific formats of data.
    """

    class WrongFormat(Exception):
        """Conveys attempt to parse a file of the wrong format"""
        pass

    # The id the system metadata will use when metadata for this component is created.
    format_id = None
    _sysmeta_generator = None
    
    def __init__(self, source, content = None, sysmeta = None):

        self._source = source          #  Connector object that created us - used for lazy creation/obtaining info as a callback
        self._content = content        #  String holding the XML package content
        self._dict = None              #  Cached dictionary used to compare package contents for sematic equivalence - not currently used
        self._sysmeta = None           #  A DataOne sysmeta object - lazily created if not provided
        self._sysmeta_content = None   #  sysmeta object as string - lazily created
        self._checksum = None          #  Holds the checksum for this package as calculated with the timestamp the package holds
        self._full_package_id = None   #  Pid that DataNone will refere to this with
        self._package_id = None        #  Package id without the timestamp
        self._timestamp =  0           #  Package's timestamp
        self._data_user = None         #  User ident found in the content xml file
        self._pid = None

        if self._sysmeta_generator is None:
            self._sysmeta_generator = SystemMetadataCreator(self)

        
    def set_pid(self, pid):
        if self._pid is not None:
            raise InternalError("Object {} already has a pid".format(self._full_package_id))
        self._full_package_id = pid.full_id()
        self._package_id = pid.base_id()
        self._timestamp =  pid.timestamp()
        
    def get_pid(self):
        return self._full_package_id

    def _get_dict(self):
        # Do this lazily - it probably won't be needed all that often
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
        """A cheap way of checking for identical components.  If the checksums are the same they are byte for byte identical.
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
        """Returns the entire package except that any unique id component that identifies the package is removed.

        The intent is to allow semantic comparison without worrying about fields specifically designed to make the
        content unique.

        This isn't currently used.

        :rtype string:

        Format specific and must be overriden in sub-classes
        """
        return None

    def _insert_packageid(self, id):
        """Return a copy of the package with the exisiting package name/id information overwritten with a new id.

        :param id: identifying name or id.
        :type id: string
        :rtype: string
        Format specific and must be overriden in sub-classes
        """        
        return None

    @staticmethod
    def _valid_timestamp(cls, timestamp):
        """Perform simple validation that a timestamp is valid

        :rtype: boolean
        """
        return True
    
    def checksum(self, timestamp = None):
        """Find the hash of the package contents.  Optionally calculate the checksum as if the package had a different timestamp.

        :param timestamp: Optional timestamp to calculate the package with
        :type timestamp: string
        :rtype: string
        """
        checksum = None

        if timestamp is None:
            return self._get_checksum()
        
        if not self._valid_timestamp(timestamp):   
            raise ValueError("Bad timestamp")

        content = self._insert_packageid(timestamp)
                
        hasher = d1_common.checksum.get_checksum_calculator_by_dataone_designator(config.hasher_algorithm)
        hasher.update(content)
        return hasher.hexdigest().lower()

    def _get_checksum(self):
        if self._checksum is None:
            if self._sysmeta is not None:
                # Avoid computing it ourselves, use the one we have in the system-meta
                self._checksum = self._sysmeta.checksum().lower()
            else:
                if self._content is not None:
                    hasher = d1_common.checksum.get_checksum_calculator_by_dataone_designator(config.hasher_algorithm)
                    hasher.update(self._content)
                    self._checksum = hasher.hexdigest().lower()
                else:
                    self._checksum = self._source.get_checksum(self._full_package_id).lower()
        return self._checksum
            
    def get_sysmeta(self, obsoletes = None):
        """Return the sysmeta object that describes this content.

        :param obsoletes: If this object will obsolete an existing object, passes the obsolete object
        :type obsoletes: Component
        :rtype: string
        
        Lazyily evaluated to avoid the cost of fetching/generating if not needed
        """
        if self._sysmeta is None:
            self._sysmeta = self._generate_sysmeta(obsoletes)
        return self._sysmeta

    def get_sysmeta_content(self, obsoletes = None):
        """
        Return the system metadata, as an xml encoded string, that describes this content.
        Lazyily evaluated to avoid the cost of fetching/generating if not needed
        """        
        if self._sysmeta_content is None:
            self._generate_sysmeta(obsoletes)
            self._sysmeta_content = self._sysmeta.toxml().encode('utf-8')
        return self._sysmeta_content
        
    def _generate_sysmeta(self, obsoletes):
        if self._sysmeta is None:
            self._sysmeta = self._sysmeta_generator.create_system_metadata(self._content, self._full_package_id, self._checksum, obsoletes )
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
            

class eml_component(Component):
    """Class encapulates EML formats needed
    """

    format_id = 'eml://ecoinformatics.org/eml-2.1.1'    

    def __init__(self, source, content = None, sysmeta = None):
        super(eml_component, self).__init__(source, content, sysmeta)
        
        if content is not None:
            try:
                e = ElementTree.fromstring(content)
                # Is it an EML file?
                if e.tag != '{eml://ecoinformatics.org/eml-2.1.1}eml':
                    raise Component.WrongFormat
                self._full_package_id = e.attrib['packageId']
                self._pid = PersistentIdentifier(self._full_package_id)
                self._package_id = self._pid.base_id()
                self._timestamp = self._pid.timestamp()
                #                dataset = e.findall('dataset')
            except Component.WrongFormat:
                raise
            except KeyError as ex:
                # whilst this is an EML .xml file, it would not appear to be the right content
                logger.debug("EML file Missing package id or owner id")
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
            
            existing_id_length =  8 + 1  # Plus one to account for closing quote            

            return self._content[0 : last_char - existing_id_length] + id + self._content[last_char - 1:]
        except (KeyError, IndexError):
            # We will assume there was never a package id present
            return None

    @staticmethod
    def _valid_timestamp(timestamp):
        return len(timestamp) >= 1


class gmd_component(Component):
    """Class encapulates GMD data formats needed
    """

    format_id = "http://www.isotc211.org/2005/gmd"
    
    def __init__(self, source, content = None, sysmeta = None):
        super(gmd_component, self).__init__(source, content, sysmeta)

        if content is not None:
            try:
                e = ElementTree.fromstring(content)
                if e.tag != '{http://www.isotc211.org/2005/gmd}MD_Metadata':
                    raise Component.WrongFormat
                
                self._full_package_id = e.findall('{http://www.isotc211.org/2005/gmd}fileIdentifier/{http://www.isotc211.org/2005/gco}CharacterString')[0].text
                self._pid = PersistentIdentifier(self._full_package_id)
                self._package_id = self._pid.base_id()
                
                self._pid = PersistentIdentifier(self._full_package_id)
                self._package_id = self._pid.base_id()

                timestamp = e.findall('{http://www.isotc211.org/2005/gmd}dateStamp/{http://www.isotc211.org/2005/gco}DateTime')[0].text
                numeric_time = dateutil.parser.parse(timestamp)
                self._timestamp =  numeric_time
            except Component.WrongFormat:
                raise
            except KeyError as ex:
                # whilst this is a GMD .xml file, it would not appear to be the right content
                logger.error("GMD file Missing needed MD_Metadata fields")
                raise ValueError
            except ElementTree.ParseError as ex:
                # Something bad has happened.
                logger.error("Failure in parsing XML {}".format(ex))
                raise ValueError
            except Exception as ex:
                logger.exception("Exception in XML parse.", exc_info = ex)
                raise ValueError            
            
        if content is not None and self._sysmeta is not None:
            self._check_sanity()        

            
    def same_checksum(self, other):
        return other == self


    def checksum(self, timestamp = None):
        if timestamp is not None:
            raise InternalError
        return super(gmd_component, self).checksum(None)
    
    def _content_no_package_id(self):
        """Removes both package id and timestamp.

        Allows direct comparison of semantic content via tree generated from XML
        """        
        try:
            first_char = self._content.index("<gmd:fileIdentifier>")
            last_char  = self._content.index("</gmd:fileIdentifier>", first_char)
            new_content = self._content[0 : first_char] + self._content[last_char + 18:]
            first_char = new_content.index("<gmd:dateStamp>")
            last_char  = new_content.index("</gmd:dateStamp>", first_char)
            return new_content[0 : first_char] + self._content[last_char + 16:]
        except (KeyError, IndexError):
            return self._content 

    def _insert_packageid(self, **kwargs):
        """Overwrite the timestamp to allow direct comparison of content via hash of content

        :param :
        :type : 
        """
        try:
            if self._content is None:
                return None
            return self._content # Noop for now
        except (KeyError, IndexError):
            # We will assume there was never a package id present
            return self._content
        
    @staticmethod
    def _valid_timestamp(timestamp):
        try:
            numeric_time = dateutil.parser.parse(timestamp)
            return numeric_time >= 1                
        except Exception:
            return False


class universal_component(object):
    """Encapsulates the creation of any of the specialised Component classes by context

    Use the ability of the Component constructors to throw a WrongFormat exception to
    determine the right format, and thus parse any supported format file with no prior knowledge
    of its format.
    """
    
    def __new__(self, source, content = None, sysmeta = None):
        try:
            return eml_component(source, content, sysmeta)
        except Component.WrongFormat:
            pass        
        try:
            return gmd_component(source, content, sysmeta)
        except Component.WrongFormat:
            pass
        # Cascade additional formats here
        raise ValueError
    
class file_connector(Connector):
    """Class provides for sources and sinks of data in a local file system"""
    
    def __init__(self, root_dir, data_format):
        super(file_connector, self).__init__()
        self._root_dir = root_dir
        self._data_component = data_format
        logger.info("Using directory {}".format(root_dir))
        try:
            # for all the local files
            for (path, dirs, files) in os.walk(self._root_dir):
                for instance in files:
                    if not instance.endswith('.xml') or instance.startswith('.'):
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
                        logger.debug("Skipped unreadable file {}".format(filename))
                        continue
                    except Exception as ex:
                        logger.exception("Exception in XML file read for {}".format(filename), exc_info = ex)
                        error.error()
                        continue
                    
                    try:
                        new_component = self._data_component(self, content)
                    except ValueError:
                        logger.debug("Skipped bad file {}".format(filename))
                        continue                    
                        
                    new_base_id = new_component.package_id()

                    if new_base_id in self._packages.keys():
                        current_time = self._packages[new_base_id].timestamp()
                        new_time = new_component.timestamp()
                        if new_time > current_time:                            
                            self._packages[new_base_id] = new_component
                        else:
                            logger.debug("Skipped file with older timestamp {}".format(filename))
                    else:
                        self._packages[new_base_id] = new_component
                        logger.debug("Adding package {}".format(new_base_id))
 
        except Exception as ex:
            logger.exception("Exception in file connection {}. Exiting".format(self._root_dir), exc_info = ex)
            raise InternalError
        logger.info("File connector finds {} objects".format(len(self._packages)))


    # These functions are primarily used for testing, but do allow the tool to be used
    # to sychronise two directories of files, if that is ever desirable.
        
    def create(self, package):
        return self.update(None, package)
        
    def update(self, existing, package):
        oldPid = existing.full_package_id() if existing is not None else None

        if package is None:
            return False
        try:
            filename = urllib.quote(package.full_package_id(), safe = "" ) + ".xml"
        except UnicodeEncodeError as ex:
            logger.exception("Unencodable byte in package name.  File not saved.", ex)
            error.error()
            return False

        if args.trial_run:
            return True
        
        try:
            with open( os.path.join(self._root_dir, filename), "w" ) as the_file:
                the_file.write(package.content())
        except Exception as ex:
            logger.exception("Failure to create package file {} in {}".format(filename, self._root_dir), exc_info = ex)
            error.error()
            return False
        
        return self._update_sysmeta(package, package.get_sysmeta_content(oldPid))

    def _update_sysmeta(self, package, sysmeta):        
        try:
            filename = urllib.quote(package.full_package_id(), safe = "" ) + "-sysmeta.xml"
        except UnicodeEncodeError as ex:
            logger.exception("Unencodable byte in package name.  File not saved.", ex)
            error.error()
            return False
        
        try:
           with open(os.path.join(self._root_dir, filename), "w") as the_file:
               the_file.write(sysmeta)
        except Exception as ex:
            logger.exception("Error during writing of sysmeta {} to {}".format(filename, self._root_dir), exc_info = ex)
            error.error()
            return False
        return True

    def get_checksum(self, pid):
        # For completness - since a file connector has read the xml content for the data_content object
        # this function should not be called
        raise InternalError("get_checksum for file connector called")
        return "should have one"
            
                
class dataone_connector(Connector):
    """
    Class provides for sources and sinks of data that are provided by a DataOne repository
    """
    def __init__(self, data_format):
        super(dataone_connector, self).__init__()
        
        self._data_component = data_format
        self._host_url = d1_common.url.makeMNBaseURL(args.destination_url)

        # This might be used to limit the range of action.  Not totally clear if the MN will ever support doing this.
        #  Currently attempts to use it get a 404 - seems we can only traverse the list from the top.
#        if args.path is not None:
#            try:
#                self._base_path_url = self._host_url + "/" +  args.path
#            except Exception as ex:
#                logger.exception("Failure to build valid destination url from {}  and  {}",format(self._host_url, args.path), exc_info = ex)
#                raise InternalError
#            self._sub_path = args.path
#        else:
        self._base_path_url = self._host_url
            
        logger.info("Using {} for DataOne MN destination".format(self._host_url))
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
#  The DataOne implementation is too old it seems - we really don't want old SSL versions in use.
#            ssl_context = ssl.SSLContext(ssl.PROTOCOL_SSLv23)
#            ssl_context.options |= ssl.OP_NO_SSLv2
#            ssl_context.options |= ssl.OP_NO_SSLv3
#            ssl_context.load_cert_chain(cert_file)

        else:            
#            ssl_context = ssl.create_default_context(ssl.Purpose.SERVER_AUTH)
            cert_file = None
        
        try:
            self._mn_client = mnclient.MemberNodeClient( base_url = self._host_url, cert_path = cert_file )
        except Exception as ex:
            logger.exception("Failure in connecting to member node {}".format(self._host_url), exc_info = ex)
            raise InternalError

        # All indexed by package_id (with no timestamp)
        self._sysmeta = {}           # cache of sysmeta data
        self._mn_client_checksums = {}   # cache of checksums
        self._packages = {}          # cache of entire packages
        self._timestamp = {}         # cache of timestamps


        self._connection = self._mn_client.connection
        
        try:
            objListIter = objectlistiterator.ObjectListIterator(self._mn_client)
            for obj in iter(objListIter):
                try:
                    pid = PersistentIdentifier(obj.identifier.value())
                except ValueError:
                    continue
                logger.debug("Using package from Dataone: {} at time  {}".format(pid.base_id(), pid.timestamp()))
                package_id = pid.base_id()
                self._packages[package_id] = self._data_component(self)
                # Since we don't download the package for the component right now (if ever) we need to explicitly set the pid
                self._packages[package_id].set_pid(pid)
                # We can also add the checksum right away, which can save time later.
                self._packages[package_id].set_checksum(obj.checksum.value())
                #            self._packages[package_id].set_????(obj.value.dateSysMetadataModified.value())  # Might be useful  - not bothering for now.

        except Exception as ex:
            logger.exception("Unable to get package list from mnclient", exc_info = ex)
            raise InternalError

        logger.info("Found {} objects on MN".format(len(self._packages)))
        
            
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
        existing: package we are replacing
        package:  new package to be uploaded
        """
        pid = package.get_pid()
        oldPid = existing.get_pid()
        logger.info("Updating {} to {}".format(oldPid, pid))
        if args.trial_run:
            return True
        try:
#            self._mn_client.reserveIdentifier(newPid)   # May not really need to do this            
            metadata = package.get_sysmeta()
            if not self._mn_client.update(oldPid, package.content_reader(), pid, metadata):
                logger.error("Failure to queue update for newPid {}".format(pid))
                error.error()
                return False

        except (d1_common.types.exceptions.NotAuthorized, d1_common.types.exceptions.InvalidRequest) as ex:
            logger.error("Update for {} Fails\n{}".format(pid, ex))
            error.error()
            return False
        except Exception as ex:
            logger.exception("Exception in update for {}".format(pid), exc_info = ex)
            error.error()
            return False
        return True
        
    def create(self, package):
        """
        Create a new package on the MN
        """
        newPid = package.get_pid()
        logger.info("Creating new with Pid {}".format(newPid))
        if args.trial_run:
            return True
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
            return False
        return True
            
    def get_package(self, ident):
        # Lazy evaluation of package - try to avoid the expense of getting the content
        if ident not in self._packages.keys():
            return None
        if self._packages[ident] is None:
            content = self._get_content_from_server(ident)
            package = self._data_content(content)
            self._packages[ident] = package
            if ident != package.package_id():
                logger.debug("Packageid: {} does not match ident: {}".format(package_id, ident))
                # We may want to provide for some option to not proceed if this does not match
                # This is a "should not happen"
                    
        return self._packages[ident]

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

        return self._mn_client.getSystemMetadata(ident)

        try:
            self._sysmeta.keys[package_id] = self._mn_client.getSystemMetadata(ident)
        except Exception as ex:
            logger.exception("Failure in getSystemMetadata", exc_info = ex)
            error.error()
        return self._sysmeta[package_id]

    
def perform_update(source, destination):
    same_content = 0
    updated_content = 0
    new_content = 0
    same_timestamp = 0
    
    logger.info("Update starts.")

    for new_package in source:
        logger.debug("Processing source package: {} with time {}".format(new_package.package_id(), new_package.timestamp()))
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
                        if destination.update(existing, new_package):
                            updated_content += 1
                else:
                    # Update no matter what - don't trust the checksum test.
                    logger.debug("Record {} :  Forcing update to exisiting content.".format(ident))
                    if destination.update(existing, new_package):
                        updated_content += 1
            else:
                same_timestamp += 1
                logger.debug("Record not newer for {} --  existing {} vs new {}.".format(ident, existing.timestamp(), new_package.timestamp()))
        else:
            logger.debug("Record {} :  New content, uploading".format(ident))
            if destination.create(new_package):
                new_content += 1

    # Logs at warning level - but this our defualt - so unless --quiet is specified we always generate this message
    logger.warning("Update completes. {} errors, {} new files uploaded, {} existing updated.  {} files with identical content and {} with same or earlier timestamp ignored."
                .format(error.error_count(), new_content, updated_content, same_content, same_timestamp)  )

        
def get_arg_parser():
    parser = argparse.ArgumentParser("dataone_pusher", description='Upload files to a DataOne server')
    parser.add_argument('-t', '--trial_run',  action = 'store_true', help = 'Do not upload to the DataOne server, can be used to describe what needs to be done')
    parser.add_argument('-v', '--verbose',    action = 'store_true', help = 'Enable informational messages')
    parser.add_argument('-V', '--debug',      action = 'store_true', help = 'Enable debugging level messages')
    parser.add_argument('-o', '--dataone_debug', action = 'store_true', help = 'Enable debugging messages for DataOne library. Produces lots of output.')
    parser.add_argument('-q', '--quiet',      action = 'store_true', help = 'Only log critical messages, turns off warning and error logging - usually means no log output')
    parser.add_argument('-i', '--intolerant', action = 'store_true', help = 'Terminate upload if any errors at all are found, tool is tolerant to 20 errors by default')
    parser.add_argument('-f', '--force_update',  action = 'store_true', help = 'Do not check if the content to be uploaded is identical to what is on the server.  Upload anyway.')
    parser.add_argument('-l', '--log_file',                         help = 'Log file. If not specified output goes to standard output')
    parser.add_argument('-c', '--config_log_file',                  help = 'Logging configuration file. Python logger format.')
    parser.add_argument('-C', '--cert_file',        help = 'Path to certificate file for access to DataOne node.')
#    parser.add_argument('-p', '--path',        help = 'Path to base of tree of MN of packages to synchronise. Not used.')    # DataOne does not provide for such a capability ATM
    parser.add_argument('-y', '--yaml_config',        help = 'Path to YAML format configuration file. Contents override internal defaults.')
    parser.add_argument('-Y', '--dump_yaml',    action = 'store_true', help = 'Dump full program configuration in YAML format to std_out. Useful start for writing a config file.')

    parser.add_argument('-F', '--format', default = 'any', choices = ['any', 'eml', 'gmd'],   help = 'Format of files to be uploaded. \'any\' will attempt to guess the format file by file.')
    
    dest_group = parser.add_mutually_exclusive_group(required = True)
    dest_group.add_argument('-D', '--destination_url',                  help = 'Hostname or IP number for destination DataOne server')
    dest_group.add_argument('-d', '--destination_dir',                  help = 'Path of destination directory tree for data files')    

    parser.add_argument('-s', '--source_dir',                       help = 'Path of source directory tree containing data files', required = True)    

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
    
    logger = logging.getLogger("dataone_pusher")
    logger.addHandler(handler)
    logging._srcfile = None

    logging_level = logging.WARNING
    if args.quiet:
        logging_level = logging.CRITICAL
    elif args.verbose:
        logging_level = logging.INFO
    elif args.debug:
        logging_level = logging.DEBUG
        

    logger.setLevel(logging_level)
    
    # This is added because errors inside the DataOne libraries will cause a console message if the logger does not exist
    pyxb_logger = logging.getLogger("pyxb.binding.basis")
    pyxb_logger.addHandler(handler)
    pyxb_logger.setLevel( logging_level )
    
    if args.dataone_debug:
        mnlogger = logging.getLogger('MemberNodeClient')
        mnlogger.addHandler(handler)
        mnlogger.setLevel( logging_level )
        
        mnlogger = logging.getLogger('DataONEBaseClient')
        mnlogger.addHandler(handler)
        mnlogger.setLevel( logging_level )
        
        mnlogger = logging.getLogger('DataONERestClient')
        mnlogger.addHandler(handler)
        mnlogger.setLevel( logging_level )
    
    logging.captureWarnings(True)
    return logger
    

def dataone_pusher():
    global args, logger, error, config

    try:
        parser = get_arg_parser()
        args = parser.parse_args()
        logger = build_logger()

        config = PusherConfiguration(args.yaml_config)
        if args.dump_yaml:
            print config
    
        error = errors(0 if args.intolerant else 20, InternalError)
        
        signal.signal(signal.SIGHUP, signal_exit_handler)    # Provide for a kill notification to go into the log file
    
        logger.info("DataOne Push starts.")
        if args.trial_run:
            logger.info("Trial run. No content will actually upload.")

        if args.format == 'eml':
            data_format = eml_component
        elif args.format == 'gmd':
            data_format = gmd_component
        else:
            data_format = universal_component

        source =      file_connector(args.source_dir, data_format)
        destination = dataone_connector(data_format) if args.destination_url is not None else file_connector(args.destination_dir, data_format)        
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
    dataone_pusher()
