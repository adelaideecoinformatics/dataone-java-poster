package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.I18NNonEmptyStringType;
import au.org.ecoinformatics.eml.jaxb.Person;

public class PersonBuilder {

	private String salutation;
	private String givenName;
	private String surName;

	public PersonBuilder salutation(String salutation) {
		// TODO support multiple salutations
		this.salutation = salutation;
		return this;
	}

	public PersonBuilder givenName(String givenName) {
		// TODO support multiple given names
		this.givenName = givenName;
		return this;
	}

	public PersonBuilder surName(String surName) {
		this.surName = surName;
		return this;
	}

	public Person build() {
		Person result = new Person();
		I18NNonEmptyStringType salutationObj = new I18NNonEmptyStringTypeBuilder(salutation).build();
		result.getSalutation().add(salutationObj);
		I18NNonEmptyStringType givenNameObj = new I18NNonEmptyStringTypeBuilder(givenName).build();
		result.getGivenName().add(givenNameObj);
		I18NNonEmptyStringType surNameObj = new I18NNonEmptyStringTypeBuilder(surName).build();
		result.setSurName(surNameObj);
		return result;
	}
}
