package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.TextType;

public class AbstractParaBuilderTest {

	/**
	 * Can we build an abstract with the mandatory content?
	 */
	@Test
	public void testBuild01() {
		AbstractParaBuilder objectUnderTest = new AbstractParaBuilder("some content");
		TextType result = objectUnderTest.build();
		String firstContent = (String) result.getContent().get(0);
		assertThat(firstContent, is("some content"));
	}

	/**
	 * Can we build an abstract with extra content?
	 */
	@Test
	public void testAddPara01() {
		AbstractParaBuilder objectUnderTest = new AbstractParaBuilder("some content");
		TextType result = objectUnderTest.addPara("more content").build();
		String firstContent = (String) result.getContent().get(0);
		assertThat(firstContent, is("some content"));
		String secondContent = (String) result.getContent().get(1);
		assertThat(secondContent, is("more content"));
	}
}
