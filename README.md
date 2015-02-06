face

# EML POSTer
## What is it?

This is a utility written in Java that lets you upload (a RESTful POST) EML documents and their corresponding sysmeta to a DataONE instance. It's actually just a wrapper around `d1_libclient_java` (http://maven.dataone.org/org/dataone/d1_libclient_java/).

## How do I use it?

At this stage, it isn't hosted in any maven repository but stay tuned as it should be soon.

To use this project locally, follow these *first time only* set up steps:

        git clone <this repo URL>
        cd eml-poster/
        mvn clean install
        # Add server public certificate to Java keyring, run with no args for help
        sudo ./trust-dataone-cert.sh /usr/lib/jvm/java-7-oracle/jre/bin
        # Build a certificate to allow you to write to DataONE, run with -h for help
        ./build_certificate.sh -i tern-projects.pem -c 130.220.209.107

Then, you can run the tool like this:

        cd eml-poster/
        # Run with no args for help
        ./launch-eml-poster.sh /tmp sysmeta.xml eml.xml

## Who maintains it?

This project is maintained by TERN Eco-informatics (http://www.ecoinformatics.org.au/) who are based in Australia. This is not the same organisation as the Eco-informatics team that provide other products (such as Morpho) that work with EML. 

## Why make it?

`d1_libclient_java` was a bit low level for our needs so this tool makes it easier for us to upload EML and sysmeta files from the command line.

If you have any questions or comments, contact tom.saleeba (at) adelaide (dot) edu (dot) au.
