We use a plugin for maven to generate the JAXB classes from the XSDs so simply running 'mvn generate-sources' is
enough to do it.

The EML XSD archive is downloaded from KNB. The XSDs are included in this repository so all the dependencies are
available here. It came from https://knb.ecoinformatics.org/software/dist/eml-2.1.1.tar.gz.

The System Metadata XSD comes from https://repository.dataone.org/software/cicore/trunk/d1_schemas/dataoneTypes.xsd

The 'datatypes.dtd' file comes from http://www.w3.org/2001/datatypes.dtd
The 'XMLSchema.dtd' file comes from http://www.w3.org/2009/XMLSchema/XMLSchema.dtd
The 'xml.xsd' file comes from http://www.w3.org/2001/03/xml.xsd