package au.org.ecoinformatics.eml.builder;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import au.org.ecoinformatics.eml.jaxb.Person;
import au.org.ecoinformatics.eml.jaxb.ResponsibleParty;

public class ResponsiblePartyBuilder {

	private Person person;

	public ResponsiblePartyBuilder person(Person person) {
		this.person = person;
		return this;
	}

	public ResponsibleParty build() {
		ResponsibleParty result = new ResponsibleParty();
		JAXBElement<?> e = new JAXBElement<Person>(new QName("person"), Person.class, null, person);
		result.getIndividualNameOrOrganizationNameOrPositionName().add(e);
		return result;
	}

}
