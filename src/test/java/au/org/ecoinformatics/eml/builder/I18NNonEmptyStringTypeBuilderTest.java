package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.I18NNonEmptyStringType;

public class I18NNonEmptyStringTypeBuilderTest {

	/**
	 * Can we build with no lang, only content?
	 */
	@Test
	public void testBuild01() {
		String content = "some content";
		I18NNonEmptyStringTypeBuilder objectUnderTest = new I18NNonEmptyStringTypeBuilder(content);
		I18NNonEmptyStringType result = objectUnderTest.build();
		assertThat((String) (result.getContent().get(0)), is("some content"));
	}

}
