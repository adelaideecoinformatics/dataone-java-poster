package au.org.ecoinformatics.eml;

import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.namespace.QName;

import au.org.ecoinformatics.eml.jaxb.eml.Eml;
import au.org.ecoinformatics.eml.jaxb.sysmeta.SystemMetadata;

public class JaxbPrettyPrinter<T> {

	private static final JaxbPrettyPrinter<Eml> EML_JAXB_PRETTY_PRINTER = 
			new JaxbPrettyPrinter<Eml>(Eml.class, new EmlNamespacePrefixMapper());
	private static final JaxbPrettyPrinter<SystemMetadata> SYS_META_JAXB_PRETTY_PRINTER = 
			new JaxbPrettyPrinter<SystemMetadata>(SystemMetadata.class, new SysMetaNamespacePrefixMapper());
	private final Class<T> jaxbType;
	private AbstractEcoinformaticsNamespacePrefixMapper prefixMapper;

	/**
	 * Constructs a new JaxbPrettyPrinter that can handle the supplied
	 * JAXB type. You have to pass the type as a generics arg AND a constructor
	 * arg because of erasure, sorry.
	 * <br /><br />
	 * Consider using one of {@link #getEmlJaxbPrettyPrinterInstance()} or
	 * {@link #getSysMetaJaxbPrettyPrinterInstance()} to get a preconfigured
	 * instance.
	 * 
	 * @param jaxbType	type of JAXB object to pretty print
	 */
	JaxbPrettyPrinter(Class<T> jaxbType, AbstractEcoinformaticsNamespacePrefixMapper prefixMapper) {
		this.jaxbType = jaxbType;
		this.prefixMapper = prefixMapper;
	}
	
	/**
	 * Pretty prints the supplied JAXB document to the supplied stream.
	 *
	 * @param jaxbObj	document to pretty print
	 * @param out		stream to print document to
	 */
	public void prettyPrint(T jaxbObj, OutputStream out) {
		try {
			Marshaller jaxbMarshaller = getConfiguredMarshaller();
			JAXBElement<T> root = wrapObjectAsRootElement(jaxbObj);
			jaxbMarshaller.marshal(root, out);
		} catch (JAXBException e) {
			throw new RuntimeException("Failed to pretty print the JAXB document", e);
		}
	}

	/**
	 * Some objects, namely {@link SystemMetadata}, don't declare themselves as a
	 * root XML object. This fixes that but also comes with the risk that you can
	 * force something that shouldn't be a root into being one.
	 * 
	 * @param jaxbObj	object to wrap as root element
	 * @return			root element
	 */
	private JAXBElement<T> wrapObjectAsRootElement(T jaxbObj) {
		QName qName = prefixMapper.getRootQName();
		JAXBElement<T> result = new JAXBElement<T>(qName, jaxbType, jaxbObj);
		return result;
	}

	private Marshaller getConfiguredMarshaller() throws JAXBException, PropertyException {
		JAXBContext jaxbContext = JAXBContext.newInstance(jaxbType);
		Marshaller result = jaxbContext.createMarshaller();
		result.setProperty("com.sun.xml.bind.namespacePrefixMapper", prefixMapper);
		result.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		return result;
	}
	
	/**
	 * @return	a pretty printer that's configured for {@link Eml}
	 */
	public static JaxbPrettyPrinter<Eml> getEmlJaxbPrettyPrinterInstance() {
		return EML_JAXB_PRETTY_PRINTER;
	}
	
	/**
	 * @return	a pretty printer that's configured for {@link SystemMetadata}
	 */
	public static JaxbPrettyPrinter<SystemMetadata> getSysMetaJaxbPrettyPrinterInstance() {
		return SYS_META_JAXB_PRETTY_PRINTER;
	}
}
