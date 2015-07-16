package au.org.ecoinformatics.eml;

import au.org.ecoinformatics.eml.jaxb.eml.Eml;

public class EmlValidator extends AbstractValidator<Eml> {

	public EmlValidator() {
		super(JaxbUtils.newEmlInstance());
	}
}
