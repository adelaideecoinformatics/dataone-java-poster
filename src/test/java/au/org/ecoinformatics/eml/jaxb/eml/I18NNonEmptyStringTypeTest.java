package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.I18NNonEmptyStringType;

public class I18NNonEmptyStringTypeTest {

	/**
	 * Can we build with no lang, only content?
	 */
	@Test
	public void testBuild01() {
		String content = "some content";
		I18NNonEmptyStringType result = new I18NNonEmptyStringType().withContent(content);
		assertThat((String) (result.getContent().get(0)), is("some content"));
	}

}
