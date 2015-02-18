package au.org.ecoinformatics.eml;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import au.org.ecoinformatics.eml.jaxb.eml.Eml;
import au.org.ecoinformatics.eml.jaxb.sysmeta.SystemMetadata;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

class JaxbUtils<T> {

	// FIXME these paths won't work once packaged as a JAR
	private static final String EML_XSD_LOCATION = "xsd/eml-2.1.1/eml.xsd";
	private static final String SYSMETA_XSD_LOCATION = "xsd/dataoneTypes.xsd";
	
	private final Class<T> jaxbType;
	private final NamespacePrefixMapper prefixMapper;
	private final String xsdLocation;
	
	private JaxbUtils(Class<T> jaxbType, NamespacePrefixMapper prefixMapper, String xsdLocation) {
		this.jaxbType = jaxbType;
		this.prefixMapper = prefixMapper;
		this.xsdLocation = xsdLocation;
	}
	
	/**
	 * @return	Marshaller that's configured for pretty printing
	 * @throws JAXBException	when we cannot create an marshaller
	 */
	Marshaller getPrettyPrintMarshaller() throws JAXBException {
		Marshaller result = getMarshaller();
		result.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		return result;
	}

	/**
	 * @return	Marshaller that's configured for validating a document
	 * @throws SAXException		when we cannot load the schema
	 * @throws JAXBException	when we cannot create an marshaller	
	 */
	Marshaller getValidationMarshaller() throws SAXException, JAXBException {
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(new File(xsdLocation));
		Marshaller marshaller = getMarshaller();
		marshaller.setSchema(schema);
		return marshaller;
	}
	
	private Marshaller getMarshaller() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(jaxbType);
		Marshaller result = jaxbContext.createMarshaller();
		result.setProperty("com.sun.xml.bind.namespacePrefixMapper", prefixMapper);
		return result;
	}
	
	public static JaxbUtils<Eml> newEmlInstance() {
		return new JaxbUtils<Eml>(Eml.class, new EmlNamespacePrefixMapper(), EML_XSD_LOCATION);
	}
	
	public static JaxbUtils<SystemMetadata> newSysMetaInstance() {
		return new JaxbUtils<SystemMetadata>(SystemMetadata.class, new SysMetaNamespacePrefixMapper(), SYSMETA_XSD_LOCATION);
	}
}
