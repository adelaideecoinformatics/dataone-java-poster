#!/usr/bin/env python

# pylint: disable=broad-except

import requests
from xml.etree import ElementTree
import sys, os
import subprocess
import traceback
import json
import xmltodict

class XmlDiff(object):
    def __init__(self, xml1, xml2):
        self.dict1 = json.loads(json.dumps((xmltodict.parse(xml1))))
        self.dict2 = json.loads(json.dumps((xmltodict.parse(xml2))))

    def equal(self):
        return self.dict1 == self.dict2

debug=1

def debug(msg):
    global debug
    if debug==0:
        return
    print msg
    
def run_eml_poster(host, filename, update):
    # generator sysmeta
    sysmeta_generator_cmd="../sysmeta-generator/launch-generator.sh %s -sysmeta"%filename
    try:
        sp = subprocess.Popen(sysmeta_generator_cmd, shell=True,
                   stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        (cmd_out, cmd_err) = sp.communicate(input=None)
        returncode = sp.returncode
        if returncode != 0:
            print("Error running sysmeta-generator: %s, returncode %s"% (sysmeta_generator_cmd, returncode))
            print("cmd_out %s cmd_err %s" % (cmd_out,cmd_err))
        debug("return code %d, cmd_out %s"%(returncode, cmd_out))
    except:
        print("Problem running poster: %s, unexpected error..." % sysmeta_generator_cmd)
        traceback.print_exc()
        return
    
    sysmeta_filename=filename+'-sysmeta'
    poster_cmd="../dataone-java-poster/launch-dataone-poster.sh -e https://%s/mn -s %s -f %s" %(host,sysmeta_filename,filename)
    if update:
        poster_cmd=poster_cmd+" -o update"
    try:
        sp = subprocess.Popen(poster_cmd, shell=True,
                   stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        (cmd_out, cmd_err) = sp.communicate(input=None)
        returncode = sp.returncode
        if returncode != 0:
            print("Error running poster: %s, returncode %s"% (poster_cmd, returncode))
            print("cmd_out %s cmd_err %s" % (cmd_out,cmd_err))
        debug("cmd %s return code %d, cmd_out %s"%(poster_cmd, returncode, cmd_out))
    except:
        print("Problem running poster: %s, unexpected error..." % poster_cmd)
        traceback.print_exc()

def trim_package_id(content):
    first_char=content.index("packageId")
    last_char=content.index(" ", first_char)
    return content[0:first_char]+content[last_char:]

def main(host, directory):
    url = "https://"+host+"/mn/v1/object"
    response = requests.get(url+"?count=1000000", verify=False)
    ids_in_server = []
    tree = ElementTree.fromstring(response.content)
    for e in tree.iter('identifier'):
        ids_in_server.append(e.text)
    debug('ids_in_server %s'%ids_in_server)
    update=True
    try:
        for (path, dirs, files) in os.walk(directory):
            for file in files:
                if not file.endswith('.xml'):
                    continue
                filename = os.path.join(path, file)
                debug("filename: %s"%filename)
                e = ElementTree.parse(filename).getroot()
                package_id = e.attrib['packageId']
                package_id_no_version = package_id[0:len(package_id)-8]
                old_records = sorted([id for id in ids_in_server if package_id_no_version==id[0:len(id)-8]])
                if len(old_records)>0:
                    debug("found existing records: %s" % old_records)
                    if package_id in old_records:
                        debug("this record already exists.")
                        continue
                    last_id=old_records[-1]
                    last_record_content = requests.get(url+"/"+last_id, verify=False).content.strip()
                    debug(last_record_content[:200])
                    server_content_without_id=trim_package_id(last_record_content)
                    with open(filename) as f:
                        local_content = f.read().strip()
                    local_content_without_id=trim_package_id(local_content)
                    same_xml=(XmlDiff(server_content_without_id, local_content_without_id).equal())
                    debug("same? %s" % same_xml)
                    if same_xml: #server_content_without_id==local_content_without_id:
                        debug("content is identical to the last record in server, skip it")
                        continue
                    else:
                        update=True
                else:
                    update=False
                run_eml_poster(host, filename, update)
                #sys.exit(0)
    except KeyboardInterrupt:
        raise
    
if __name__ == "__main__":
    if len(sys.argv)<3:
        print "Usage: %s [host] [eml-path]   host is the hostname or IP of the DataOne server." % sys.argv[0]
        sys.exit(1)
    main(sys.argv[1], sys.argv[2])
    sys.exit(0)