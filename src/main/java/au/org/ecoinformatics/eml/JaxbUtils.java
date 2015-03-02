package au.org.ecoinformatics.eml;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import au.org.ecoinformatics.eml.jaxb.eml.Eml;
import au.org.ecoinformatics.eml.jaxb.sysmeta.SystemMetadata;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

class JaxbUtils<T> {

	private static final String PREFIX_MAPPER_PROPERTY_NAME = "com.sun.xml.bind.namespacePrefixMapper";
	private static final String SYSMETA_XSD_LOCATION = "au/org/ecoinformatics/eml/xsd/dataoneTypes.xsd";
	private static final String XSD_BASE = "au/org/ecoinformatics/eml/xsd/";
	private static final String EML_SUBDIR = "eml-2.1.1/";
	private static final String EML_XSD_LOCATION = XSD_BASE + EML_SUBDIR + "eml.xsd";
	
	private final Class<T> jaxbType;
	private final NamespacePrefixMapper prefixMapper;
	private final String xsdLocation;
	private final EmlInJarLSResourceResolver resourceResolver;
	
	private JaxbUtils(Class<T> jaxbType, NamespacePrefixMapper prefixMapper, String xsdLocation) {
		this(jaxbType, prefixMapper, xsdLocation, null);
	}
	
	private JaxbUtils(Class<T> jaxbType, NamespacePrefixMapper prefixMapper, String xsdLocation, EmlInJarLSResourceResolver resourceResolver) {
		this.jaxbType = jaxbType;
		this.prefixMapper = prefixMapper;
		this.xsdLocation = xsdLocation;
		this.resourceResolver = resourceResolver;
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
	 * @throws SAXException			when we cannot load the schema
	 * @throws JAXBException		when we cannot create an marshaller	
	 */
	Marshaller getValidationMarshaller() throws SAXException, JAXBException {
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		schemaFactory.setResourceResolver(resourceResolver);
		StreamSource source = new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream(xsdLocation));
		Schema schema = schemaFactory.newSchema(source);
		Marshaller marshaller = getMarshaller();
		marshaller.setSchema(schema);
		return marshaller;
	}

	private Marshaller getMarshaller() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(jaxbType);
		Marshaller result = jaxbContext.createMarshaller();
		result.setProperty(PREFIX_MAPPER_PROPERTY_NAME, prefixMapper);
		return result;
	}
	
	public static JaxbUtils<Eml> newEmlInstance() {
		// Could load XSDs over the network (e.g: https://knb.ecoinformatics.org/knb/schema/eml-2.1.1/eml.xsd) too, but it's sloooooooow
		return new JaxbUtils<Eml>(Eml.class, new EmlNamespacePrefixMapper(), EML_XSD_LOCATION, new EmlInJarLSResourceResolver(XSD_BASE, EML_SUBDIR));
	}
	
	public static JaxbUtils<SystemMetadata> newSysMetaInstance() {
		return new JaxbUtils<SystemMetadata>(SystemMetadata.class, new SysMetaNamespacePrefixMapper(), SYSMETA_XSD_LOCATION);
	}
}