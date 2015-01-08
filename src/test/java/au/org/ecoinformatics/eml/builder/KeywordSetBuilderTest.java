package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.ProtocolType.KeywordSet;

public class KeywordSetBuilderTest {

	/**
	 * Can we build an empty object?
	 */
	@Test
	public void testBuild01() {
		KeywordSetBuilder objectUnderTest = new KeywordSetBuilder();
		KeywordSet result = objectUnderTest.build();
		assertThat(result.getKeyword().size(), is(0));
		assertNull(result.getKeywordThesaurus());
	}

	/**
	 * Can we build a keyword set with three keywords?
	 */
	@Test
	public void testAddKeyword01() {
		KeywordSetBuilder objectUnderTest = new KeywordSetBuilder();
		KeywordSet result = objectUnderTest
			.addKeyword("keyword1")
			.addKeyword("keywordTwo")
			.addKeyword("keywordIII")
			.build();
		assertThat(((String)result.getKeyword().get(0).getContent().get(0)), is("keyword1"));
		assertThat(((String)result.getKeyword().get(1).getContent().get(0)), is("keywordTwo"));
		assertThat(((String)result.getKeyword().get(2).getContent().get(0)), is("keywordIII"));
	}
	
	/**
	 * Can we build a keyword set with a thesaurus?
	 */
	@Test
	public void testAddKeywordThesaurus01() {
		KeywordSetBuilder objectUnderTest = new KeywordSetBuilder();
		KeywordSet result = objectUnderTest
			.keywordThesaurus("KNBRegistry")
			.build();
		assertThat(result.getKeywordThesaurus(), is("KNBRegistry"));
	}
}
