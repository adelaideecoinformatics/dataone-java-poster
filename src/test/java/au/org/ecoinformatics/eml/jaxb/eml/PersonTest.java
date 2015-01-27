package au.org.ecoinformatics.eml.jaxb.eml;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasFirstI18NContent;
import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasI18NContent;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.I18NNonEmptyStringType;
import au.org.ecoinformatics.eml.jaxb.eml.Person;

public class PersonTest {

	/**
	 * Can we build with a salutation?
	 */
	@Test
	public void testSalutation01() {
		Person result = new Person().withSalutation(new I18NNonEmptyStringType().withContent("Mr"));
		assertThat(result.getSalutation(), hasFirstI18NContent("Mr"));
	}

	/**
	 * Can we build with a given name?
	 */
	@Test
	public void testGivenName01() {
		Person result = new Person().withGivenName(new I18NNonEmptyStringType().withContent("John"));
		assertThat(result.getGivenName(), hasFirstI18NContent("John"));
	}

	/**
	 * Can we build with a surname?
	 */
	@Test
	public void testSurname01() {
		Person result = new Person().withSurName(new I18NNonEmptyStringType().withContent("Bourne"));
		assertThat(result.getSurName(), hasI18NContent("Bourne"));
	}
}
