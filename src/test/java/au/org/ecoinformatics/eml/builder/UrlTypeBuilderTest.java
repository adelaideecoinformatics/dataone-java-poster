package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.FunctionType;
import au.org.ecoinformatics.eml.jaxb.UrlType;

public class UrlTypeBuilderTest {

	/**
	 * Can we build with the mandatory anyUri element?
	 */
	@Test
	public void testAnyUri01() {
		String anyUri = "http://some.uri";
		UrlTypeBuilder objectUnderTest = new UrlTypeBuilder(anyUri);
		UrlType result = objectUnderTest.build();
		assertThat(result.getValue(), is(anyUri));
	}
	
	/**
	 * Can we build with a 'function' attribute?
	 */
	@Test
	public void testFunction01() {
		UrlTypeBuilder objectUnderTest = new UrlTypeBuilder("http://some.uri");
		UrlType result = objectUnderTest.function(FunctionType.INFORMATION).build();
		assertThat(result.getFunction(), is(FunctionType.INFORMATION));
	}
	
	/**
	 * Are all optional elements empty/defaulted when we don't supply them?
	 */
	@Test
	public void testBuild01() {
		UrlTypeBuilder objectUnderTest = new UrlTypeBuilder("http://some.uri");
		UrlType result = objectUnderTest.build();
		assertThat(result.getFunction(), is(FunctionType.DOWNLOAD));
	}
}
