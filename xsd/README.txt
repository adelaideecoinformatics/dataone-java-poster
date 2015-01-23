We use a plugin for maven to generate the JAXB classes from the XSDs so simply running 'mvn generate-sources' is
enough to do it.

The EML XSD archive is downloaded from KNB. The XSDs are included in this repository so all the dependencies are
available here. It came from https://knb.ecoinformatics.org/software/dist/eml-2.1.1.tar.gz.

The System Metadata XSD comes from https://repository.dataone.org/software/cicore/trunk/d1_schemas/dataoneTypes.xsd
