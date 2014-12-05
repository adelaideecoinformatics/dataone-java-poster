package au.org.ecoinformatics.eml.builder;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasFirstI18NContent;
import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasI18NContent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.Address;
import au.org.ecoinformatics.eml.jaxb.I18NNonEmptyStringType;
import au.org.ecoinformatics.eml.jaxb.Person;
import au.org.ecoinformatics.eml.jaxb.ResponsibleParty;

public class ResponsiblePartyBuilderTest {

	/**
	 * Do we not add null stuff to the result when they weren't supplied?
	 */
	@Test
	public void testBuild01() {
		ResponsiblePartyBuilder objectUnderTest = new ResponsiblePartyBuilder();
		ResponsibleParty result = objectUnderTest.build();
		assertThat(result.getIndividualNameOrOrganizationNameOrPositionName().size(), is(0));
		assertThat(result.getAddress().size(), is(0));
	}

	/**
	 * Can we build a responsible party object with a person?
	 */
	@Test
	public void testCreator01() {
		ResponsiblePartyBuilder objectUnderTest = new ResponsiblePartyBuilder();
		Person person = new PersonBuilder().salutation("Mr").givenName("Jason").surName("Bourne").build();
		ResponsibleParty result = objectUnderTest.person(person).build();
		Person value = ((Person) result.getIndividualNameOrOrganizationNameOrPositionName().get(0).getValue());
		assertThat(value.getSalutation(), hasFirstI18NContent("Mr"));
		assertThat(value.getGivenName(), hasFirstI18NContent("Jason"));
		assertThat(value.getSurName(), hasI18NContent("Bourne"));
	}

	/**
	 * Can we build a responsible party object with an organization name?
	 */
	@Test
	public void testOrgName01() {
		ResponsiblePartyBuilder objectUnderTest = new ResponsiblePartyBuilder();
		ResponsibleParty result = objectUnderTest.orgName("EcoInf").build();
		I18NNonEmptyStringType value = ((I18NNonEmptyStringType) result.getIndividualNameOrOrganizationNameOrPositionName().get(0).getValue());
		assertThat(value, hasI18NContent("EcoInf"));
	}

	/**
	 * Can we build a responsible party object with an position name?
	 */
	@Test
	public void testPositionName01() {
		ResponsiblePartyBuilder objectUnderTest = new ResponsiblePartyBuilder();
		ResponsibleParty result = objectUnderTest.positionName("Big Boss").build();
		I18NNonEmptyStringType value = ((I18NNonEmptyStringType) result.getIndividualNameOrOrganizationNameOrPositionName().get(0).getValue());
		assertThat(value, hasI18NContent("Big Boss"));
	}

	/**
	 * Can we build a responsible party object with an address?
	 */
	@Test
	public void testAddress01() {
		ResponsiblePartyBuilder objectUnderTest = new ResponsiblePartyBuilder();
		Address address = new AddressBuilder().city("Adelaide").build();
		ResponsibleParty result = objectUnderTest.address(address).build();
		Address value = result.getAddress().get(0);
		assertThat(value.getCity(), hasI18NContent("Adelaide"));
	}
}
