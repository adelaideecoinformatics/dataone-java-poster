package au.org.ecoinformatics.eml.jaxb.eml;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasFirstI18NContent;
import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasI18NContent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.Address;
import au.org.ecoinformatics.eml.jaxb.eml.I18NNonEmptyStringType;

public class AddressTest {

	/**
	 * Are unsupplied items empty?
	 */
	@Test
	public void testBuild01() {
		Address result = new Address();
		assertThat(result.getDeliveryPoint().size(), is(0));
	}

	/**
	 * Can we create an address with a delivery point?
	 */
	@Test
	public void testDeliveryPoint01() {
		Address result = new Address()
			.withDeliveryPoint(new I18NNonEmptyStringType().withContent("123 Fake St"));
		assertThat(result.getDeliveryPoint(), hasFirstI18NContent("123 Fake St"));
	}

	/**
	 * Can we create an address with a city?
	 */
	@Test
	public void testCity01() {
		Address result = new Address()
			.withCity(new I18NNonEmptyStringType().withContent("Adelaide"));
		assertThat(result.getCity(), hasI18NContent("Adelaide"));
	}

	/**
	 * Can we create an address with an administrative area?
	 */
	@Test
	public void testAdministrativeArea01() {
		Address result = new Address()
			.withAdministrativeArea(new I18NNonEmptyStringType().withContent("SA"));
		assertThat(result.getAdministrativeArea(), hasI18NContent("SA"));
	}

	/**
	 * Can we create an address with a postal code?
	 */
	@Test
	public void testPostalCode01() {
		Address result = new Address()
			.withPostalCode(new I18NNonEmptyStringType().withContent("5005"));
		assertThat(result.getPostalCode(), hasI18NContent("5005"));
	}

	/**
	 * Can we create an address with a country?
	 */
	@Test
	public void testCountry01() {
		Address result = new Address()
			.withCountry(new I18NNonEmptyStringType().withContent("Australia"));
		assertThat(result.getCountry(), hasI18NContent("Australia"));
	}
}
