package au.org.ecoinformatics.eml.builder;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasFirstI18NContent;
import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasI18NContent;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.Person;
import au.org.ecoinformatics.eml.jaxb.ResponsibleParty;

public class ResponsiblePartyBuilderTest {

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
}
