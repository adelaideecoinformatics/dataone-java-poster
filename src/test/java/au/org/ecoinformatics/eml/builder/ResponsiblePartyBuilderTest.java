package au.org.ecoinformatics.eml.builder;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasFirstI18NContent;
import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasI18NContent;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.Address;
import au.org.ecoinformatics.eml.jaxb.I18NNonEmptyStringType;
import au.org.ecoinformatics.eml.jaxb.Person;
import au.org.ecoinformatics.eml.jaxb.ResponsibleParty;
import au.org.ecoinformatics.eml.jaxb.ResponsibleParty.Phone;
import au.org.ecoinformatics.eml.jaxb.ResponsibleParty.UserId;

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
		assertThat(result.getPhone().size(), is(0));
		assertThat(result.getElectronicMailAddress().size(), is(0));
		assertThat(result.getOnlineUrl().size(), is(0));
		assertThat(result.getUserId().size(), is(0));
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

	/**
	 * Can we build a responsible party object with a phone number?
	 */
	@Test
	public void testPhone01() {
		ResponsiblePartyBuilder objectUnderTest = new ResponsiblePartyBuilder();
		Phone phone = new PhoneBuilder().phoneNumber("0412345678").phoneType(PhoneType.tdd).build();
		ResponsibleParty result = objectUnderTest.phone(phone).build();
		assertThat(result.getPhone().get(0).getValue(), is("0412345678"));
	}

	/**
	 * Can we build a responsible party object with an electronic mail address?
	 */
	@Test
	public void testElectronicMailAddress01() {
		ResponsiblePartyBuilder objectUnderTest = new ResponsiblePartyBuilder();
		ResponsibleParty result = objectUnderTest.email("blah@mail.com").build();
		assertThat(result.getElectronicMailAddress(), hasFirstI18NContent("blah@mail.com"));
	}

	/**
	 * Can we build a responsible party object with two online URLs?
	 */
	@Test
	public void testAddOnlineUrl01() {
		ResponsiblePartyBuilder objectUnderTest = new ResponsiblePartyBuilder();
		ResponsibleParty result = objectUnderTest
				.addOnlineUrl("http://www.ecoinformatics.org.au")
				.addOnlineUrl("http://www.aekos.org.au")
				.build();
		assertThat(result.getOnlineUrl(), hasItems("http://www.ecoinformatics.org.au", "http://www.aekos.org.au"));
	}

	/**
	 * Can we build a responsible party object with two user IDs?
	 */
	@Test
	public void testAddUserId01() {
		ResponsiblePartyBuilder objectUnderTest = new ResponsiblePartyBuilder();
		UserId user1 = new UserIdBuilder("user1").build();
		UserId userA = new UserIdBuilder("userA").build();
		ResponsibleParty result = objectUnderTest
				.addUserId(user1)
				.addUserId(userA)
				.build();
		// Not awesome to expect order but we don't have equals() implemented so we can't use hasItems()
		assertThat(result.getUserId().get(0).getValue(), is("user1"));
		assertThat(result.getUserId().get(1).getValue(), is("userA"));
	}
}
