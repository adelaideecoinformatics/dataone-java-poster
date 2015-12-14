package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.TextType;

public class TextTypeTest {

	/**
	 * Can we build a TextType with content?
	 */
	@Test
	public void testBuild01() {
		TextType result = new TextType()
			.withContent("some content");
		String firstContent = (String) result.getContent().get(0);
		assertThat(firstContent, is("some content"));
	}

	/**
	 * Can we build a TextType with extra content?
	 */
	@Test
	public void testAddPara01() {
		TextType result = new TextType()
			.withContent("some content")
			.withContent("more content");
		String firstContent = (String) result.getContent().get(0);
		assertThat(firstContent, is("some content"));
		String secondContent = (String) result.getContent().get(1);
		assertThat(secondContent, is("more content"));
	}
}
