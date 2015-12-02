#!/usr/bin/env python

# pylint: disable=broad-except

import requests
from xml.etree import ElementTree
import sys, os
import subprocess
import traceback
import json
import xmltodict
import argparse
import logging
import signal
from abc import ABCMeta
from urlparse import urljoin
import urllib



class errors:
    """
    Provides a simple class to encapsulate a limit on the number of run-time recoverable errors, to log this
    and to the program.
    """
    def __init__(self, limit, exception = None):
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
                exit(1)
            
def signal_exit_handler(signum, frame):
    """
    Provide a mechanism to log if the program is terminated via an external signal (ie a kill command).
    """
    logger.info("Exiting with signal {}".format(signum))
    exit(0)



def create_sysmeta_content(filename):
     # generator sysmeta
    sysmeta_generator_cmd = "../sysmeta-generator/launch-generator.sh {} -sysmeta".format(filename)
    try:
        sp = subprocess.Popen(sysmeta_generator_cmd,
                              shell = True,
                              stdout = subprocess.PIPE,
                              stderr = subprocess.PIPE)
        
        (cmd_out, cmd_err) = sp.communicate(input = None)
        returncode = sp.returncode

        if returncode != 0:
            logger.error("Error running sysmeta-generator: {}, returncode {}".format(sysmeta_generator_cmd, returncode))
            logger.error("cmd_out {} cmd_err {}".format(cmd_out,cmd_err))
            error.error()
            return False
        logger.debug("Sysmeta generator: return code {}, cmd_out {}".format(returncode, cmd_out))
    except:
        logger.exception("Problem running sysmeta generator: {}, unexpected error.".format(sysmeta_generator_cmd))
        error.error()
        return False
    return True
       

    
def run_eml_poster(host, filename, update):
    if args.trial_run:
        return
    
    if not create_sysmeta_content(filename):
        return False


    # There seems to be zero reason not to post content ourselves rather than go the the expense of calling this program
    
    sysmeta_filename = filename + '-sysmeta'
    poster_cmd = "../dataone-java-poster/launch-dataone-poster.sh -e https://{}/mn -s {} -f {}".format(host, sysmeta_filename, filename)
    if update:
        poster_cmd = poster_cmd + " -o update"
    try:
        sp = subprocess.Popen(poster_cmd,
                              shell  = True,
                              stdout = subprocess.PIPE,
                              stderr = subprocess.PIPE)
        (cmd_out, cmd_err) = sp.communicate(input=None)
        returncode = sp.returncode
        
        if returncode != 0:
            logger.error("Error running poster: {}, returncode {}".format(poster_cmd, returncode))
            logger.error("    cmd_out {}\n cmd_err {}".format(cmd_out, cmd_err))
            error.error()
            return False
            
        logger.debug("cmd {} return code {}, cmd_out {}".format(poster_cmd, returncode, cmd_out))
    except:
        logger.exception("Problem running poster: {}, unexpected error.".format(poster_cmd))
        error.error()
        return False


    
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
            
    def update(self, package):
        pass
            

class eml_component:
    """
    Class encapulates the eml data being processed and provides a few utility functions
    to help with processing.
    """
    
    def __init__(self, content):
        if content is None or len(content) == 0:
            logger.debug("Empty content")
            raise ValueError
        self._content = content
        self._dict = None
        try:
            e = ElementTree.fromstring(content)
            self._full_package_id = e.attrib['packageId']
        except KeyError as ex:
            # whilst this is a .xml file, it would not appear to be the right content
            logger.debug("No package id")
            raise ValueError
        except ElementTree.ParseError as ex:
            # Something bad has happened.
            logger.debug("Failure in parsing XML {}".format(ex))
            raise ValueError
        except Exception as ex:
            print type(ex)
            logger.exception("Exception in XML parse.", exc_info = ex)
            raise ValueError
        
        try:
            self._package_id = self._full_package_id[0: -9]
            self._timestamp = int(self._full_package_id[-8:])
        except ValueError, IndexError:
            # Assume that the full package id is corrupt
            logger.debug("Bad package id string {}".format(self._full_package_id))
            raise ValueError
        # some simple sanity checking
        if self._timestamp <= 0:
            logger.debug("Bad timestamp value {}".format(self._timestamp))
            raise ValueError
        if len(self._package_id) == 0:
            logger.debug("Empty package id")
            raise ValueError


    def _get_dict(self):
        # Do this lazily - it probably won't be needed all that often
        if self._dict is None:
            self._dict = json.loads(json.dumps((xmltodict.parse(self._content_no_package_id()))))
        return self._dict

    def same_as(self, other):
        return self._get_dict() == other._get_dict()
                
    def package_id(self):
        return self._package_id

    def timestamp(self):
        return self._timestamp
    
    def content(self):
        return self._content

    def _content_no_package_id(self):
        # Elide the string "packageId ....  ' ' " from the string
        # Effectively removes the XML definition of the packageid
        try:
            first_char = self._content.index("packageId")
            last_char  = self._content.index(" ", first_char)
            return self._content[0 : first_char] + content[last_char:]
        except (KeyError, IndexError):
            # We will assume there was never a package id present
            return self._content 
        
    def create_sysmeta(self):
        # Not complete - we may elect to rewrite the sysmeta generation in python.
        filename = ""
        create_sysmeta_data(filename)
        return filename
 
    def write(self, file_path):
        try:
            with open(os.join(file_path, self._package_id), "w") as the_file:
                the_file.write(self.content)
        except Exception as ex:
            logger.exception("Error during writing of package contents {} to {}".format(self._package_id, file_path), exc_info = ex)
            error.error()


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
                        new_component = eml_component(content)
                    except ValueError:
                        logger.info("Skipped bad file {}".format(filename))
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
            exit()
        logger.info("File connector finds {} eml objects".format(len(self._eml_packages)))

    def update(self, package, update):
        if args.trial_run:
            return
        if package is None:
            return
        try:
            filename = urllib.quote(package.package_id(), safe = "" ) + ".xml"
        except UnicodeEncodeError as ex:
            logger.exception("Unencodable byte in package name.  File not saved.", ex)
            return
            
        try:
            with open( os.path.join(self._root_dir, filename), "w" ) as the_file:
                the_file.write(package.content())
        except Exception as ex:
            logger.exception("Failure to create package file {} in {}".format(filename, self._root_dir), exc_info = ex)
            error.error()
            return
        # We could consider adding the package to the list of packages we have
        # Little point right now however

                
class dataone_connector(Connector):
    """
    Class provides for sources and sinks of eml data that are provided by a DataOne repository
    """
    def __init__(self, host, root_path):
        super(dataone_connector, self).__init__()
        self._host = host
        self._root_path = root_path
        self._host_url = urljoin("https://", self._host)
        self._url = urljoin(self.host_url, self._root_path)

        try:
            if not args.trial_run:
                response = requests.get(url + "?count=1000000", verify=False)                

            if response is not None:
                tree = ElementTree.fromstring(response.content)
                for e in tree.iter('identifier'):
                    self._eml_packages[e.text] = None

            if args.trial_run:
                logger.info("Trial run. Server not contacted.")
            logger.info('Found {} ids in server'.format(len(self._eml_packages)))
                
        except:
            logger.exception("Error talking to host {}".format(self._host))
            error.error()
            return None


    def _get_content_from_server(self, ident):
        try:
            record_content = requests.get( urljoin(url, ident), verify = False).content.strip()
        except Exception as ex:
            logger.exception("Error in GET to server", exc_info = ex)
            error.error()
            return None
        return record_content

    def update(self, content, update):
        if args.trial_run:
            return
        temp_filename = "/var/tmp/eml_pusher_temp"
        with open(temp_filename, "w") as the_file:
            the_file.write(content)
            try:
                run_eml_poster(self._host, temp_filename, update)
            except:
                pass            
        
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



def perform_update(source, destination):
    same_content = 0
    updated_content = 0
    new_content = 0
    same_timestamp = 0
    
    logger.info("EML Update starts.")

    try:
        for package in source:
            logger.info("Processing source package: {}".format(package.package_id()))
            new_ident = package.package_id()
            if new_ident in destination.idents():
                existing = destination.get_package(new_ident)
                if package.timestamp() > existing.timestamp():
                    if not args.force_update:
                        # Note - structured to avoid the same_as check unless we really need to.
                        if not package.same_as(existing):
                            same_content += 1
                            logger.debug("Record {} :  Identical content already on server. Not updated.".format(new_ident))
                        else:
                            logger.debug("Record {} :  New content for existing record. Updating.".format(new_ident))
                            updated_content += 1
                            destination.update(package, True)
                    else:
                        logger.debug("Record {} :  Forcing update to exisiting content.".format(new_ident))
                        updated_content += 1
                        destination.update(package, True)
                else:
                    same_timestamp += 1
                    logger.debug("Record {} : Timestamps same.".format(new_ident))
            else:
                logger.debug("Record {} :  New content, uploading".format(new_ident))
                new_content += 1
                destination.update(package, False)

    except KeyboardInterrupt:
        logger.info("Keyboard interrupt halts processing.\n    {} new files uploaded, {} existing updated, and {} files with identical content.".format(new_content, updated_content, same_content))
        raise
    except Exception as ex:
        logger.exception("Unhandled exception. Exiting.", exc_info = ex)
        exit()
    logger.info("EML Update completes.  {} new files uploaded, {} existing updated.  {} files with identical content and {} with same timestamp ignored."
                .format(new_content, updated_content, same_content, same_timestamp)  )

        
def get_arg_parser():
    parser = argparse.ArgumentParser("eml_pusher", description='Create list of EML files that must be uploaded to a DataOne server')
    parser.add_argument('-t', '--trial_run',  action = 'store_true', help = 'Do not contact DataOne server, only check local files')
    parser.add_argument('-v', '--verbose',    action = 'store_true', help = 'Enable informational messages')
    parser.add_argument('-V', '--debug',      action = 'store_true', help = 'Enable debugging messages')
    parser.add_argument('-q', '--quiet',      action = 'store_true', help = 'Do not output anything to standard output or standard error')
    parser.add_argument('-i', '--intolerant', action = 'store_true', help = 'Do not perform any uploads if any errors at all are found')
    parser.add_argument('-f', '--force_update',  action = 'store_true', help = 'Do not check if the content to be uploaded is identical to what is on the server.  Upload anyway.')
    parser.add_argument('-l', '--log_file',                         help = 'Log file. If not specified output goes to standard output')
    parser.add_argument('-c', '--config_log_file',                  help = 'Logging configuration file. Python logger format.')

    dest_group = parser.add_mutually_exclusive_group(required = True)
    dest_group.add_argument('-D', '--destination_url',                  help = 'Hostname or IP number for destination DataOne server')
    dest_group.add_argument('-d', '--destination_dir',                  help = 'Path of destination directory tree for EML')    

    source_group = parser.add_mutually_exclusive_group(required = True)
    source_group.add_argument('-S', '--source_url',                       help = 'Hostname or IP number for source DataOne server')
    source_group.add_argument('-s', '--source_dir',                       help = 'Path of source directory tree containing EML files')    

    return parser


def build_logger(args):
    """
    Simple logger configuration that allows logging to nothing, standard output, a file, or allows use of a configuration file for more advanced
    logging destinations. Captures warnings to the log as well.
    """
    if args.config_log_file is not None:
        handler = logging.FileConfig(args.config_file_handler)
    if args.log_file is not None:
        handler = logging.FileHandler(args.log_file, delay = True)
    else:
        if args.quiet:
            handler = logging.NullHandler()
        else:
            handler = logging.StreamHandler()

    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
    handler.setFormatter(formatter)
    
    logger = logging.getLogger('eml_pusher')
    logger.addHandler(handler)

    # Turn off source file inclusion in the output unless we are debugging
    if not args.debug:
        logging._srcfile = None
    
    logging.captureWarnings(True)
    logger.setLevel( logging.INFO if args.verbose else logging.WARNING )
    logger.setLevel( logging.DEBUG if args.debug else logger.getEffectiveLevel() )
    return logger
    
if __name__ == "__main__":

    error = errors(20)    
    parser = get_arg_parser()
    args = parser.parse_args()
    logger = build_logger(args)
 
    signal.signal(signal.SIGHUP, signal_exit_handler)    # Provide for a kill notification to go into the log file

    logger.info("EML Push starts.")
    source =      dataone_connector(args.source_url)      if args.source_url is not None      else file_connector(args.source_dir)
    destination = dataone_connector(args.destination_url) if args.destination_url is not None else file_connector(args.destination_dir)
    
    perform_update(source, destination)
    
    sys.exit(0)
