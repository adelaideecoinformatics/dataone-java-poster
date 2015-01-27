package au.org.ecoinformatics.eml.jaxb.eml;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasFirstI18NContent;
import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasI18NContent;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.Address;
import au.org.ecoinformatics.eml.jaxb.eml.I18NNonEmptyStringType;
import au.org.ecoinformatics.eml.jaxb.eml.ObjectFactory;
import au.org.ecoinformatics.eml.jaxb.eml.Person;
import au.org.ecoinformatics.eml.jaxb.eml.ResponsibleParty;

public class ResponsiblePartyTest {

	/**
	 * Do we not add null stuff to the result when they weren't supplied?
	 */
	@Test
	public void testBuild01() {
		ResponsibleParty result = new ResponsibleParty();
		assertThat(result.getIndividualNameOrOrganizationNameOrPositionName().size(), is(0));
		assertThat(result.getAddress().size(), is(0));
		assertThat(result.getPhone().size(), is(0));
		assertThat(result.getElectronicMailAddress().size(), is(0));
		assertThat(result.getOnlineUrl().size(), is(0));
		assertThat(result.getUserId().size(), is(0));
	}

	/**
	 * Can we build a responsible party object with an individual name?
	 */
	@Test
	public void testCreator01() {
		Person person = new Person()
			.withSalutation(new I18NNonEmptyStringType().withContent("Mr"))
			.withGivenName(new I18NNonEmptyStringType().withContent("Jason"))
			.withSurName(new I18NNonEmptyStringType().withContent("Bourne"));
		ResponsibleParty result = new ResponsibleParty()
			.withIndividualNameOrOrganizationNameOrPositionName(new ObjectFactory().createResponsiblePartyIndividualName(person));
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
		ResponsibleParty result = new ResponsibleParty()
			.withIndividualNameOrOrganizationNameOrPositionName(
					new ObjectFactory().createResponsiblePartyOrganizationName(
							new I18NNonEmptyStringType().withContent("EcoInf")));
		I18NNonEmptyStringType value = ((I18NNonEmptyStringType) result.getIndividualNameOrOrganizationNameOrPositionName().get(0).getValue());
		assertThat(value, hasI18NContent("EcoInf"));
	}

	/**
	 * Can we build a responsible party object with an position name?
	 */
	@Test
	public void testPositionName01() {
		ResponsibleParty result = new ResponsibleParty()
			.withIndividualNameOrOrganizationNameOrPositionName(
				new ObjectFactory().createResponsiblePartyPositionName(
						new I18NNonEmptyStringType().withContent("Big Boss")));
		I18NNonEmptyStringType value = ((I18NNonEmptyStringType) result.getIndividualNameOrOrganizationNameOrPositionName().get(0).getValue());
		assertThat(value, hasI18NContent("Big Boss"));
	}

	/**
	 * Can we build a responsible party object with an address?
	 */
	@Test
	public void testAddress01() {
		ResponsibleParty result = new ResponsibleParty()
			.withAddress(new Address().withCity(new I18NNonEmptyStringType().withContent("Adelaide")));
		Address value = result.getAddress().get(0);
		assertThat(value.getCity(), hasI18NContent("Adelaide"));
	}

	/**
	 * Can we build a responsible party object with a phone number?
	 */
	@Test
	public void testPhone01() {
		ResponsibleParty result = new ResponsibleParty()
			.withPhone(new ResponsibleParty.Phone().withValue("0412345678"));
		assertThat(result.getPhone().get(0).getValue(), is("0412345678"));
	}

	/**
	 * Can we build a responsible party object with an electronic mail address?
	 */
	@Test
	public void testElectronicMailAddress01() {
		ResponsibleParty result = new ResponsibleParty()
			.withElectronicMailAddress(new I18NNonEmptyStringType().withContent("blah@mail.com"));
		assertThat(result.getElectronicMailAddress(), hasFirstI18NContent("blah@mail.com"));
	}

	/**
	 * Can we build a responsible party object with two online URLs?
	 */
	@Test
	public void testAddOnlineUrl01() {
		ResponsibleParty result = new ResponsibleParty()
			.withOnlineUrl("http://www.ecoinformatics.org.au")
			.withOnlineUrl("http://www.aekos.org.au");
		assertThat(result.getOnlineUrl(), hasItems("http://www.ecoinformatics.org.au", "http://www.aekos.org.au"));
	}

	/**
	 * Can we build a responsible party object with two user IDs?
	 */
	@Test
	public void testAddUserId01() {
		ResponsibleParty result = new ResponsibleParty()
			.withUserId(
					new ResponsibleParty.UserId().withValue("user1"),
					new ResponsibleParty.UserId().withValue("userA"));
		// Not awesome to expect order but we don't have equals() implemented so we can't use hasItems()
		assertThat(result.getUserId().get(0).getValue(), is("user1"));
		assertThat(result.getUserId().get(1).getValue(), is("userA"));
	}
}
