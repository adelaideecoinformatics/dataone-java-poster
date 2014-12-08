package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.ResponsibleParty.Phone;

public class PhoneBuilderTest {

	/**
	 * Can we build a phone object with a number?
	 */
	@Test
	public void testPhoneNumber01() {
		PhoneBuilder objectUnderTest = new PhoneBuilder();
		Phone result = objectUnderTest.phoneNumber("0412345678").build();
		assertThat(result.getValue(), is("0412345678"));
	}

	/**
	 * Can we build a phone object with a phone type?
	 */
	@Test
	public void testPhoneType01() {
		PhoneBuilder objectUnderTest = new PhoneBuilder();
		Phone result = objectUnderTest.phoneType(PhoneType.facsimile).build();
		assertThat(result.getPhonetype(), is(PhoneType.facsimile.name()));
	}

	/**
	 * Does building an empty phone object not set any fields that it shouldn't?
	 */
	@Test
	public void testBuild01() {
		PhoneBuilder objectUnderTest = new PhoneBuilder();
		Phone result = objectUnderTest.build();
		assertNull(result.getValue());
		String defaultPhoneType = PhoneType.voice.name();
		assertThat(result.getPhonetype(), is(defaultPhoneType));
	}
}
