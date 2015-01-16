# EML API
## What is it?
This is a Java API that lets you programmatically create an EML document and write it out to an XML file. This file could then be loaded into DataONE amongst other things.
## How do I use it?
The repository is built and published to a maven repostory: http://maven.dataone.org/au/org/ecoinformatics/eml-api/

However, you can also clone this repo and the `mvn clean install` and it'll be ready for you to use locally.
## Who maintains it?
This project is maintained by TERN Eco-informatics (http://ecoinformatics.org.au/) who are based in Australia. This is not the same organisation as the Eco-informatics team that provide other products (such as Morpho) that work with EML. 
## Why make it?
At the time we started this project, there was nothing available that would let you solve this problem. There were products available to do XSLT transformations from other well known XML-based formats to EML but nothing to create it directly.