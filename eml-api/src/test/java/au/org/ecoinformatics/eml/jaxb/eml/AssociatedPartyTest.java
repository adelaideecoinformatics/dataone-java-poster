package au.org.ecoinformatics.eml.jaxb.eml;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasFirstI18NContent;
import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasI18NContent;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.ProtocolType.AssociatedParty;

public class AssociatedPartyTest {

	/**
	 * Can we build an associated party object with an individual name using the standard JAXB method?
	 */
	@Test
	public void testIndividualName01() {
		Person person = new Person()
			.withSalutation(new I18NNonEmptyStringType().withContent("Mr"))
			.withGivenName(new I18NNonEmptyStringType().withContent("Jason"))
			.withSurName(new I18NNonEmptyStringType().withContent("Bourne"));
		AssociatedParty result = new AssociatedParty()
			.withIndividualNameOrOrganizationNameOrPositionName(new ObjectFactory().createResponsiblePartyIndividualName(person));
		Person value = ((Person) result.getIndividualNameOrOrganizationNameOrPositionName().get(0).getValue());
		assertThat(value.getSalutation(), hasFirstI18NContent("Mr"));
		assertThat(value.getGivenName(), hasFirstI18NContent("Jason"));
		assertThat(value.getSurName(), hasI18NContent("Bourne"));
	}

	/**
	 * Can we build an associated party object with an individual name using the added method?
	 */
	@Test
	public void testIndividualName02() {
		Person person = new Person()
			.withSalutation(new I18NNonEmptyStringType().withContent("Mr"))
			.withGivenName(new I18NNonEmptyStringType().withContent("Jason"))
			.withSurName(new I18NNonEmptyStringType().withContent("Bourne"));
		AssociatedParty result = new AssociatedParty()
			.withIndividualName(person);
		Person value = ((Person) result.getIndividualNameOrOrganizationNameOrPositionName().get(0).getValue());
		assertThat(value.getSalutation(), hasFirstI18NContent("Mr"));
		assertThat(value.getGivenName(), hasFirstI18NContent("Jason"));
		assertThat(value.getSurName(), hasI18NContent("Bourne"));
	}
	
	/**
	 * Can we build an associated party object with an organization name using the standard JAXB method?
	 */
	@Test
	public void testOrganizationName01() {
		AssociatedParty result = new AssociatedParty()
			.withIndividualNameOrOrganizationNameOrPositionName(
					new ObjectFactory().createResponsiblePartyOrganizationName(
							new I18NNonEmptyStringType().withContent("EcoInf")));
		I18NNonEmptyStringType value = ((I18NNonEmptyStringType) result.getIndividualNameOrOrganizationNameOrPositionName().get(0).getValue());
		assertThat(value, hasI18NContent("EcoInf"));
	}

	/**
	 * Can we build an associated party object with an organization name using the added method?
	 */
	@Test
	public void testOrganizationName02() {
		AssociatedParty result = new AssociatedParty()
			.withOrganizationName(new I18NNonEmptyStringType().withContent("EcoInf"));
		I18NNonEmptyStringType value = ((I18NNonEmptyStringType) result.getIndividualNameOrOrganizationNameOrPositionName().get(0).getValue());
		assertThat(value, hasI18NContent("EcoInf"));
	}
	
	/**
	 * Can we build an associated party object with a position name using the standard JAXB method?
	 */
	@Test
	public void testPositionName01() {
		AssociatedParty result = new AssociatedParty()
			.withIndividualNameOrOrganizationNameOrPositionName(
				new ObjectFactory().createResponsiblePartyPositionName(
						new I18NNonEmptyStringType().withContent("Big Boss")));
		I18NNonEmptyStringType value = ((I18NNonEmptyStringType) result.getIndividualNameOrOrganizationNameOrPositionName().get(0).getValue());
		assertThat(value, hasI18NContent("Big Boss"));
	}

	/**
	 * Can we build an associated party object with a position name with the added method?
	 */
	@Test
	public void testPositionName02() {
		AssociatedParty result = new AssociatedParty()
			.withPositionName(new I18NNonEmptyStringType().withContent("Big Boss"));
		I18NNonEmptyStringType value = ((I18NNonEmptyStringType) result.getIndividualNameOrOrganizationNameOrPositionName().get(0).getValue());
		assertThat(value, hasI18NContent("Big Boss"));
	}
}
