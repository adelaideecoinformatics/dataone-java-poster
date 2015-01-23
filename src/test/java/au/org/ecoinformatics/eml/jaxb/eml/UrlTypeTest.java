package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.FunctionType;
import au.org.ecoinformatics.eml.jaxb.eml.UrlType;

public class UrlTypeTest {

	/**
	 * Can we build with the mandatory anyUri element?
	 */
	@Test
	public void testAnyUri01() {
		String anyUri = "http://some.uri";
		UrlType result = new UrlType().withValue(anyUri);
		assertThat(result.getValue(), is(anyUri));
	}
	
	/**
	 * Can we build with a 'function' attribute?
	 */
	@Test
	public void testFunction01() {
		UrlType result = new UrlType().withFunction(FunctionType.INFORMATION);
		assertThat(result.getFunction(), is(FunctionType.INFORMATION));
	}
	
	/**
	 * Are all optional elements empty/defaulted when we don't supply them?
	 */
	@Test
	public void testBuild01() {
		UrlType result = new UrlType();
		assertThat(result.getFunction(), is(FunctionType.DOWNLOAD));
		assertNull(result.getValue());
	}
}
