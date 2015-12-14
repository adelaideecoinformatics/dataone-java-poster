package au.org.ecoinformatics.eml;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Validator that can support validation of both EML and sys-meta documents
 *
 * @param <T>	Type to validate
 */
public abstract class AbstractValidator<T> {

	private final JaxbUtils<T> jaxbUtils;

	public AbstractValidator(JaxbUtils<T> jaxbUtils) {
		this.jaxbUtils = jaxbUtils;
	}

	public void validate(T obj) throws EmlValidationException {
		try {
			Marshaller marshaller = jaxbUtils.getValidationMarshaller();
			marshaller.marshal(obj, new DefaultHandler());
		} catch (SAXException e) {
			throw new EmlValidationException("Programmer error: couldn't load the schema", e);
		} catch (JAXBException e) {
			throw new EmlValidationException("Validation error: document failed validation", e);
		}
	}
}
