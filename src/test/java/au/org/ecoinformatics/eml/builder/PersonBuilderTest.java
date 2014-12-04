package au.org.ecoinformatics.eml.builder;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasFirstI18NContent;
import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasI18NContent;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.Person;

public class PersonBuilderTest {

	/**
	 * Can we build with a salutation?
	 */
	@Test
	public void testSalutation01() {
		PersonBuilder objectUnderTest = new PersonBuilder();
		Person result = objectUnderTest.salutation("Mr").build();
		assertThat(result.getSalutation(), hasFirstI18NContent("Mr"));
	}

	/**
	 * Can we build with a given name?
	 */
	@Test
	public void testGivenName01() {
		PersonBuilder objectUnderTest = new PersonBuilder();
		Person result = objectUnderTest.givenName("John").build();
		assertThat(result.getGivenName(), hasFirstI18NContent("John"));
	}

	/**
	 * Can we build with a surname?
	 */
	@Test
	public void testSurname01() {
		PersonBuilder objectUnderTest = new PersonBuilder();
		Person result = objectUnderTest.surName("Bourne").build();
		assertThat(result.getSurName(), hasI18NContent("Bourne"));
	}
}
