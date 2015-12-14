package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.ResponsibleParty.Phone;

public class PhoneTest {

	/**
	 * Can we build a phone object with a number?
	 */
	@Test
	public void testPhoneNumber01() {
		Phone result = new Phone().withValue("0412345678");
		assertThat(result.getValue(), is("0412345678"));
	}

	/**
	 * Can we build a phone object with a phone type?
	 */
	@Test
	public void testPhoneType01() {
		Phone result = new Phone().withPhonetype("facsimile");
		assertThat(result.getPhonetype(), is("facsimile"));
	}

	/**
	 * Does building an empty phone object not set any fields that it shouldn't?
	 */
	@Test
	public void testBuild01() {
		Phone result = new Phone();
		assertNull(result.getValue());
		String defaultPhoneType = "voice";
		assertThat(result.getPhonetype(), is(defaultPhoneType));
	}
}
