package au.org.ecoinformatics.eml.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.DatasetType;
import au.org.ecoinformatics.eml.jaxb.I18NNonEmptyStringType;

public class HasFirstI18NContentMatcherTest {

	/**
	 * Can we tell when content matches?
	 */
	@Test
	public void testMatches01() {
		HasFirstI18NContentMatcher objectUnderTest = new HasFirstI18NContentMatcher("blah");
		List<I18NNonEmptyStringType> item = getI18NListWithContent("blah");
		boolean result = objectUnderTest.matches(item);
		assertTrue("should be considered matching", result);
	}

	/**
	 * Can we tell when content DOESN'T match?
	 */
	@Test
	public void testMatches02() {
		HasFirstI18NContentMatcher objectUnderTest = new HasFirstI18NContentMatcher("something");
		List<I18NNonEmptyStringType> item = getI18NListWithContent("another thing");
		boolean result = objectUnderTest.matches(item);
		assertFalse("should NOT be considered matching", result);
	}

	/**
	 * Can we handle a null expectation?
	 */
	@Test
	public void testMatches03() {
		HasFirstI18NContentMatcher objectUnderTest = new HasFirstI18NContentMatcher(null);
		List<I18NNonEmptyStringType> item = getI18NListWithContent("another thing");
		boolean result = objectUnderTest.matches(item);
		assertFalse("should NOT be considered matching", result);
	}

	/**
	 * Can we handle an item with no content items?
	 */
	@Test
	public void testMatches04() {
		HasFirstI18NContentMatcher objectUnderTest = new HasFirstI18NContentMatcher("something");
		List<I18NNonEmptyStringType> item = getEmptyI18NList();
		boolean result = objectUnderTest.matches(item);
		assertFalse("should NOT be considered matching", result);
	}

	/**
	 * Can we handle an item with one content item but without anything inside it?
	 */
	@Test
	public void testMatches05() {
		HasFirstI18NContentMatcher objectUnderTest = new HasFirstI18NContentMatcher("something");
		List<I18NNonEmptyStringType> item = getI18NListWithOneEmptyItem();
		boolean result = objectUnderTest.matches(item);
		assertFalse("should NOT be considered matching", result);
	}

	/**
	 * Can we survive a list with one item that contains content but it is null content?
	 */
	@Test
	public void testMatches06() {
		HasFirstI18NContentMatcher objectUnderTest = new HasFirstI18NContentMatcher("something");
		List<I18NNonEmptyStringType> item = getI18NListWithOneItemWithNullContent();
		boolean result = objectUnderTest.matches(item);
		assertFalse("should NOT be considered matching", result);
	}

	private List<I18NNonEmptyStringType> getI18NListWithContent(String text) {
		DatasetType dt = new DatasetType();
		I18NNonEmptyStringType content = new I18NNonEmptyStringType();
		content.getContent().add(text);
		dt.getTitle().add(content);
		return dt.getTitle();
	}

	private List<I18NNonEmptyStringType> getEmptyI18NList() {
		return new DatasetType().getTitle();
	}

	private List<I18NNonEmptyStringType> getI18NListWithOneEmptyItem() {
		DatasetType dt = new DatasetType();
		I18NNonEmptyStringType content = new I18NNonEmptyStringType();
		dt.getTitle().add(content);
		return dt.getTitle();
	}

	private List<I18NNonEmptyStringType> getI18NListWithOneItemWithNullContent() {
		DatasetType dt = new DatasetType();
		I18NNonEmptyStringType content = new I18NNonEmptyStringType();
		content.getContent().add(null);
		dt.getTitle().add(content);
		return dt.getTitle();
	}
}
