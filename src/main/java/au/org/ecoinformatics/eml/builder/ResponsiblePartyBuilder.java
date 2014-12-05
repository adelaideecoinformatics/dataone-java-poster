package au.org.ecoinformatics.eml.builder;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import au.org.ecoinformatics.eml.jaxb.Address;
import au.org.ecoinformatics.eml.jaxb.I18NNonEmptyStringType;
import au.org.ecoinformatics.eml.jaxb.Person;
import au.org.ecoinformatics.eml.jaxb.ResponsibleParty;

public class ResponsiblePartyBuilder {

	private Person person;
	private String orgName;
	private String positionName;
	private Address address;

	public ResponsiblePartyBuilder person(Person person) {
		this.person = person;
		return this;
	}

	public ResponsiblePartyBuilder orgName(String orgName) {
		this.orgName = orgName;
		return this;
	}

	public ResponsiblePartyBuilder positionName(String positionName) {
		this.positionName = positionName;
		return this;
	}

	public ResponsiblePartyBuilder address(Address address) {
		this.address = address;
		return this;
	}

	public ResponsibleParty build() {
		ResponsibleParty result = new ResponsibleParty();
		if (isSupplied(person)) {
			JAXBElement<?> e = new JAXBElement<Person>(new QName("person"), Person.class, null, person);
			result.getIndividualNameOrOrganizationNameOrPositionName().add(e);
		}
		if (isSupplied(orgName)) {
			I18NNonEmptyStringType jaxbOrgName = new I18NNonEmptyStringTypeBuilder(orgName).build();
			JAXBElement<?> e = new JAXBElement<I18NNonEmptyStringType>(new QName("organizationName"), I18NNonEmptyStringType.class, null, jaxbOrgName);
			result.getIndividualNameOrOrganizationNameOrPositionName().add(e);
		}
		if (isSupplied(positionName)) {
			I18NNonEmptyStringType jaxbPositionName = new I18NNonEmptyStringTypeBuilder(positionName).build();
			JAXBElement<?> e = new JAXBElement<I18NNonEmptyStringType>(new QName("positionName"), I18NNonEmptyStringType.class, null, jaxbPositionName);
			result.getIndividualNameOrOrganizationNameOrPositionName().add(e);
		}
		if (isSupplied(address)) {
			result.getAddress().add(address);
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
