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

If you have any questions of comments, contact tom.saleeba (at) adelaide (dot) edu (dot) au.

## Maven, JAXB/XJC and patching
This project uses a maven plugin to generate JAXB classes from the XSDs and also adds in the fluent interfaces to make the classes more usable. There are a few cases where JAXB doesn't make the best code (JAXBElement) so as part of the build process, a patch is applied to the generate classes to add some nicer methods.

You can regenerate all the JAXB classes and apply the patch with:

        mvn clean package -Pjaxb

...or if you only want to apply the patch, use:

        mvn clean package -Ppatch

There is a bash script `update-patch.sh` that will update the patch file. The workflow is:

 1. make some changes to the generated JAXB classes
 1. run `update-patch.sh` to update the patch file from your changes
 1. check the patch file and optionally remove any patch hunks you don't think should be there (comments about generation date)
 1. revert your changed to the JAXB classes
 1. run maven to apply the patch (see above) to make sure it works
 1. add everything (including the JAXB classes, because it makes life easy) to version control and commit
