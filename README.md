# EML API
## What is it?
This is a Java API that lets you programmatically create an EML document (or a SysMeta document) and write it out to an XML file. This file could then be loaded into DataONE amongst other things.

## How do I use it?
The repository is built and published to a maven repostory: http://maven.dataone.org/au/org/ecoinformatics/eml-api/

However, you can also clone this repo and then run `mvn clean install` and it'll be ready for you to use locally.

When it comes to calling it from your code, you'll probably want to create the Eml class:

        import au.org.ecoinformatics.eml.jaxb.eml.DatasetType;
        import au.org.ecoinformatics.eml.jaxb.eml.Eml;
        import au.org.ecoinformatics.eml.jaxb.eml.I18NNonEmptyStringType;
         
        Eml eml = new Eml()
          .withPackageId("some-unique-id")
          .withDataset(
            new DatasetType()
              .withTitle(new I18NNonEmptyStringType().withContent("best dataset ever"))
              // ...and all the other components);
        eml.validate();
        eml.write(System.out);

...or the SystemMetadata class:

        import au.org.ecoinformatics.eml.EmlApiInfo;
        import au.org.ecoinformatics.eml.jaxb.sysmeta.Identifier;
        import au.org.ecoinformatics.eml.jaxb.sysmeta.ReplicationPolicy;
        import au.org.ecoinformatics.eml.jaxb.sysmeta.SystemMetadata;
        
        SystemMetadata sysMeta = new SystemMetadata()
			.withIdentifier(new Identifier().withValue("some-unique-id"))
			.withFormatId(EmlApiInfo.EML_FORMAT_ID)
			// ...and all the other components
			.withReplicationPolicy(new ReplicationPolicy().withReplicationAllowed(true));
        sysMeta.validate();
        sysMeta.write(System.out);

Writing to System.out isn't very useful though so you can write to a file with something like this:

       Eml eml = // create your EML file
       try {
			Path path = Paths.get("/path/to/output", "my-first-eml.xml");
			OutputStream os = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
			eml.write(os);
		} catch (IOException e) {
			// handle this with your loggers, etc
		}

There are also some useful constants related to this version of the API, such as the version supported, stored in `au.org.ecoinformatics.eml.EmlApiInfo`.

## Who maintains it?
This project is maintained by TERN Eco-informatics (http://ecoinformatics.org.au/) who are based in Australia. This is **not** the same organisation as the Eco-informatics team that provide other products (such as Morpho) that work with EML.
 
## Why make it?
At the time we started this project, there was nothing available that would let you solve this problem. There were products available to do XSLT transformations from other well known XML-based formats to EML but nothing to create it programmatically in Java.

If you have any questions of comments, contact tom.saleeba (at) adelaide (dot) edu (dot) au.

## Maven, JAXB/XJC and patching
This project uses a maven plugin to generate JAXB classes from the XSDs and also adds in the fluent interfaces to make the classes more usable. There are a few cases where JAXB doesn't make the best code (JAXBElement) so as part of the build process, a patch is applied to the generate classes to add some nicer methods.

You can regenerate all the JAXB classes and apply the patch with:

        mvn clean test -Pjaxb

...or if you only want to apply the patch, use:

        mvn clean process-sources -Ppatch

There is a bash script `update-patch.sh` that will update the patch file. The workflow is:

 1. make some changes to the generated JAXB classes
 1. run `update-patch.sh` to update/regenerated the patch file from your changes
 1. check the patch file. The hunks associated with the `Generated on` comment usually don't work so use emacs in "diff" mode to split and edit the patch file to remove all of them. It's painfully manual but I haven't thought of a better way yet.
 1. run maven to apply the patch (see above) to make sure it works. Don't worry about reverting changes, they'll be steam rolled by JAXB
 1. add everything (including the JAXB classes, because it makes life easy) to version control and commit
