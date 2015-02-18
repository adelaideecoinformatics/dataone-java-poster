package au.org.ecoinformatics.eml;

import java.io.OutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import au.org.ecoinformatics.eml.jaxb.eml.Eml;
import au.org.ecoinformatics.eml.jaxb.sysmeta.SystemMetadata;

public class JaxbPrettyPrinter<T> {

	private static final JaxbPrettyPrinter<Eml> EML_JAXB_PRETTY_PRINTER = 
			new JaxbPrettyPrinter<Eml>(JaxbUtils.newEmlInstance());
	private static final JaxbPrettyPrinter<SystemMetadata> SYS_META_JAXB_PRETTY_PRINTER = 
			new JaxbPrettyPrinter<SystemMetadata>(JaxbUtils.newSysMetaInstance());
	private final JaxbUtils<T> jaxbUtils;

	/**
	 * Constructs a new JaxbPrettyPrinter that can handle the supplied
	 * JAXB type. You have to pass the type as a generics arg AND a constructor
	 * arg because of erasure, sorry.
	 * <br /><br />
	 * Consider using one of {@link #getEmlJaxbPrettyPrinterInstance()} or
	 * {@link #getSysMetaJaxbPrettyPrinterInstance()} to get a preconfigured
	 * instance.
	 * 
	 * @param jaxbUtils		helper that handles all the low level details
	 */
	JaxbPrettyPrinter(JaxbUtils<T> jaxbUtils) {
		this.jaxbUtils = jaxbUtils;
	}
	
	/**
	 * Pretty prints the supplied JAXB document to the supplied stream.
	 *
	 * @param jaxbObj	document to pretty print
	 * @param out		stream to print document to
	 */
	public void prettyPrint(T jaxbObj, OutputStream out) {
		try {
			Marshaller jaxbMarshaller = jaxbUtils.getPrettyPrintMarshaller();
			jaxbMarshaller.marshal(jaxbObj, out);
		} catch (JAXBException e) {
			throw new RuntimeException("Failed to pretty print the JAXB document", e);
		}
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
