package au.org.ecoinformatics.eml;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

import au.org.ecoinformatics.eml.jaxb.eml.Eml;
import au.org.ecoinformatics.eml.jaxb.sysmeta.SystemMetadata;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

class JaxbUtils<T> {

	private static final String PREFIX_MAPPER_PROPERTY_NAME = "com.sun.xml.bind.namespacePrefixMapper";
	private static final String SYSMETA_XSD_LOCATION = "au/org/ecoinformatics/eml/xsd/dataoneTypes.xsd";
	private static final String EML_XSD_BASE = "au/org/ecoinformatics/eml/xsd/eml-2.1.1/";
	private static final String EML_XSD_LOCATION = EML_XSD_BASE + "eml.xsd";
	
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
		// Could load from https://knb.ecoinformatics.org/knb/schema/eml-2.1.1/eml.xsd too, but it's sloooooooow
		return new JaxbUtils<Eml>(Eml.class, new EmlNamespacePrefixMapper(), EML_XSD_LOCATION, new EmlInJarLSResourceResolver(EML_XSD_BASE));
	}
	
	public static JaxbUtils<SystemMetadata> newSysMetaInstance() {
		return new JaxbUtils<SystemMetadata>(SystemMetadata.class, new SysMetaNamespacePrefixMapper(), SYSMETA_XSD_LOCATION);
	}
}

/**
 * A resource resolver to handle loading referenced schema files from a JAR on the classpath.
 * 
 * Modified from http://code.google.com/p/xmlsanity/source/browse/src/com/arc90/xmlsanity/validation/ResourceResolver.java?r=03a92d97f15904b3892922e45724bb086d54fa4e
 * Original credit to:
 * From http://pbin.oogly.co.uk/listings/viewlistingdetail/2a70d763929ce3053085bfaa1d78e2
 * From http://stackoverflow.com/questions/1094893/validate-an-xml-file-against-multiple-schema-definitions/1105871#1105871
 * @author Jon http://stackoverflow.com/users/82865/jon
 */
class EmlInJarLSResourceResolver implements LSResourceResolver {
	
	private static final Logger logger = LoggerFactory.getLogger(EmlInJarLSResourceResolver.class);
	
	private final String basePath;

	public EmlInJarLSResourceResolver(String basePath) {
		this.basePath = basePath;
	}
	
	@Override
	public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
		String fullXsdPath = basePath + systemId;
		logger.debug(String.format("Attempting to load requested XSD '%s' from %s", systemId, fullXsdPath));
		InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fullXsdPath);
	    return new LSInputImpl(publicId, systemId, resourceAsStream);
	}
}

class LSInputImpl implements LSInput {

    private static final Logger logger = LoggerFactory.getLogger(LSInputImpl.class);
	private String publicId;
    private String systemId;
    private final BufferedInputStream inputStream;

    public LSInputImpl(String publicId, String sysId, InputStream input) {
        this.publicId = publicId;
        this.systemId = sysId;
        this.inputStream = new BufferedInputStream(input);
    }
    
    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }
    
    public String getStringData() {
    	synchronized (inputStream) {
    		try {
    			byte[] input = new byte[inputStream.available()];
    			inputStream.read(input);
    			String contents = new String(input);
    			return contents;
    		} catch (IOException e) {
    			logger.error("Problem reading the input stream", e);
    			return null;
    		}
    	}
    }
    
    public String getBaseURI() { return null; }
    public InputStream getByteStream() { return null; }
    public boolean getCertifiedText() { return false; }
    public Reader getCharacterStream() { return null; }
    public String getEncoding() { return null; }

    public void setBaseURI(String baseURI) { }
    public void setByteStream(InputStream byteStream) { }
    public void setCertifiedText(boolean certifiedText) { }
    public void setCharacterStream(Reader characterStream) { }
    public void setEncoding(String encoding) { }
    public void setStringData(String stringData) { }
}