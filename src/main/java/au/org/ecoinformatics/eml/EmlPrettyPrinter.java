package au.org.ecoinformatics.eml;

import java.io.PrintStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import au.org.ecoinformatics.eml.jaxb.Eml;

public class EmlPrettyPrinter {

	/**
	 * Pretty prints the supplied EML document to the supplied stream.
	 *
	 * @param eml	document to pretty print
	 * @param out	stream to print document to
	 */
	public void prettyPrint(Eml eml, PrintStream out) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Eml.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(eml, out);
		} catch (JAXBException e) {
			throw new RuntimeException("Failed to pretty print the EML document", e);
		}
	}

}
