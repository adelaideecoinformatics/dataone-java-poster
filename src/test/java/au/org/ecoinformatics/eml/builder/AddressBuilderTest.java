package au.org.ecoinformatics.eml.builder;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasFirstI18NContent;
import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasI18NContent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.Address;

public class AddressBuilderTest {

	/**
	 * Do we not add null stuff to the result when they weren't supplied?
	 */
	@Test
	public void testBuild01() {
		AddressBuilder objectUnderTest = new AddressBuilder();
		Address result = objectUnderTest.build();
		assertThat(result.getDeliveryPoint().size(), is(0));
	}

	/**
	 * Can we create an address with a delivery point?
	 */
	@Test
	public void testDeliveryPoint01() {
		AddressBuilder objectUnderTest = new AddressBuilder();
		Address result = objectUnderTest.deliveryPoint("123 Fake St").build();
		assertThat(result.getDeliveryPoint(), hasFirstI18NContent("123 Fake St"));
	}

	/**
	 * Can we create an address with a city?
	 */
	@Test
	public void testCity01() {
		AddressBuilder objectUnderTest = new AddressBuilder();
		Address result = objectUnderTest.city("Adelaide").build();
		assertThat(result.getCity(), hasI18NContent("Adelaide"));
	}

	/**
	 * Can we create an address with an administrative area?
	 */
	@Test
	public void testAdministrativeArea01() {
		AddressBuilder objectUnderTest = new AddressBuilder();
		Address result = objectUnderTest.administrativeArea("SA").build();
		assertThat(result.getAdministrativeArea(), hasI18NContent("SA"));
	}

	/**
	 * Can we create an address with a postal code?
	 */
	@Test
	public void testPostalCode01() {
		AddressBuilder objectUnderTest = new AddressBuilder();
		Address result = objectUnderTest.postalCode("5005").build();
		assertThat(result.getPostalCode(), hasI18NContent("5005"));
	}

	/**
	 * Can we create an address with a country?
	 */
	@Test
	public void testCountry01() {
		AddressBuilder objectUnderTest = new AddressBuilder();
		Address result = objectUnderTest.country("Australia").build();
		assertThat(result.getCountry(), hasI18NContent("Australia"));
	}
}
