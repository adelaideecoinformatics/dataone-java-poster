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
        ./build-certificate.sh -i /path/to/key-for-vm.pem -c 130.220.209.107

Then, you can run the tool like this:

        cd dataone-java-poster/
        # Run with -h for help. Replace IP with your DataONE node's IP or hostname.
        ./launch-eml-poster.sh -e https://130.220.209.107/mn -s /tmp/sysmeta.xml -f /tmp/eml.xml

You can also delete an existing entry on the DataONE server by passing the optional `-o` (operation) arugment:

        # Delete the existing document that has the pid in the supplied sysmeta file
        ./launch-eml-poster.sh -e https://130.220.209.107/mn -s /tmp/sysmeta.xml -f /tmp/eml.xml -o delete

The update is activated by using `-o update` and will perform an update if an existing version exists, otherwise it falls back to performing a create. The update operation is only supported for AEKOS identifiers with the reverse gregorian date suffix as the version.

If you want to run the tool on another machine, use the `build-bin-tarball.sh` script to create an archive that you can SCP/FTP to another box.

You run against a single file as shown above or run against a whole directory with the `-d /some/dir` argument. See the help for more details.

## Who maintains it?

This project is maintained by TERN Eco-informatics (http://www.ecoinformatics.org.au/) who are based in Australia. This is not the same organisation as the Eco-informatics team that provide other products (such as Morpho) that work with EML. 

## Why make it?

`d1_libclient_java` was a bit low level for our needs so this tool makes it easier for us to upload EML and sysmeta files from the command line. It's also fairly tailored towards the identifiers we frequently deal with so your mileage may vary when using it.

If you have any questions or comments, contact tom.saleeba (at) adelaide (dot) edu (dot) au.
