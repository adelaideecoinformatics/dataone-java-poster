package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.ProtocolType.KeywordSet;
import au.org.ecoinformatics.eml.jaxb.eml.ProtocolType.KeywordSet.Keyword;

public class KeywordSetTest {

	/**
	 * Can we build an empty object?
	 */
	@Test
	public void testBuild01() {
		KeywordSet result = new KeywordSet();
		assertThat(result.getKeyword().size(), is(0));
		assertNull(result.getKeywordThesaurus());
	}

	/**
	 * Can we build a keyword set with three keywords?
	 */
	@Test
	public void testAddKeyword01() {
		KeywordSet result = new KeywordSet()
			.withKeyword(new Keyword().withContent("keyword1"))
			.withKeyword(new Keyword().withContent("keywordTwo"), 
					new Keyword().withContent("keywordIII"));
		assertThat(((String)result.getKeyword().get(0).getContent().get(0)), is("keyword1"));
		assertThat(((String)result.getKeyword().get(1).getContent().get(0)), is("keywordTwo"));
		assertThat(((String)result.getKeyword().get(2).getContent().get(0)), is("keywordIII"));
	}
	
	/**
	 * Can we build a keyword set with a thesaurus?
	 */
	@Test
	public void testAddKeywordThesaurus01() {
		KeywordSet result = new KeywordSet()
			.withKeywordThesaurus("KNBRegistry");
		assertThat(result.getKeywordThesaurus(), is("KNBRegistry"));
	}
}
