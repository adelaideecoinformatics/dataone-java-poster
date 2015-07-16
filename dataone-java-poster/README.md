# DataONE-Java-POSTer
## What is it?

This is a utility written in Java that lets you upload (a RESTful POST) EML documents and their corresponding sysmeta to a DataONE instance. It's actually just a wrapper around `d1_libclient_java` (http://maven.dataone.org/org/dataone/d1_libclient_java/). The member node implementation that it was written against is GMN http://pythonhosted.org/dataone.generic_member_node/index.html.

## How do I use it?

At this stage, it isn't hosted in any maven repository but stay tuned as it should be soon.

To use this project locally, follow these *first time only* set up steps:

        git clone <this repo URL>
        cd dataone-java-poster/
        mvn clean install
        # Add server public certificate to Java keyring, run with no args for help
        sudo ./trust-dataone-cert.sh /usr/lib/jvm/java-7-oracle/jre/bin
        # Build a certificate to allow you to write to DataONE, run with -h for help. Replace IP with your DateONE node.
        ./build-certificate.sh -i tern-projects.pem -c 130.220.209.107

Then, you can run the tool like this:

        cd dataone-java-poster/
        # Run with no args for help. Replace IP with your DataONE node.
        ./launch-eml-poster.sh https://130.220.209.107/mn /tmp/sysmeta.xml /tmp/eml.xml

You can also delete an existing entry on the DataONE server by passing the optional `operation` arugment:

        # Delete the existing document that has the pid in the supplied sysmeta file
        ./launch-eml-poster.sh https://130.220.209.107/mn /tmp/sysmeta.xml /tmp/eml.xml delete

The update currently isn't supported by this tool because it's the superceed type of update i.e. create a new document. If you want to replace an existing document then simply delete it then recreate it.

If you want to run the tool on another machine, use the `build-bin-tarball.sh` script to create an archive that you can SCP/FTP to another box. You can then run against a single file as shown above or run against a whole directory with the `run-for-all-xml-in-dir.sh` script.

## Who maintains it?

This project is maintained by TERN Eco-informatics (http://www.ecoinformatics.org.au/) who are based in Australia. This is not the same organisation as the Eco-informatics team that provide other products (such as Morpho) that work with EML. 

## Why make it?

`d1_libclient_java` was a bit low level for our needs so this tool makes it easier for us to upload EML and sysmeta files from the command line.

If you have any questions or comments, contact tom.saleeba (at) adelaide (dot) edu (dot) au.
