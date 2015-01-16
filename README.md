# EML POSTer
## What is it?
This is a utility written in Java that lets you upload (a RESTful POST) EML documents and their corresponding sysmeta to a DataONE instance. It's actually just a wrapper around `d1_libclient_java` (http://maven.dataone.org/org/dataone/d1_libclient_java/).
## How do I use it?
Clone this repo and `mvn clean install` then it'll be ready for you to use locally.

At this stage, it isn't hosted in any maven repository but stay tuned as it should be soon.
## Who maintains it?
This project is maintained by TERN Eco-informatics (http://ecoinformatics.org.au/) who are based in Australia. This is not the same organisation as the Eco-informatics team that provide other products (such as Morpho) that work with EML. 
## Why make it?
`d1_libclient_java` was a bit low level for our needs so this tool makes it easier for us to upload EML and sysmeta files from the command line.

If you have any questions or comments, contact tom.saleeba (at) adelaide (dot) edu (dot) au.