package au.org.ecoinformatics.eml.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.I18NNonEmptyStringType;

public class HasI18NContentMatcherTest {

	/**
	 * Can we tell when content matches?
	 */
	@Test
	public void testMatches01() {
		HasI18NContentMatcher objectUnderTest = new HasI18NContentMatcher("blah");
		I18NNonEmptyStringType item = getI18NWithContent("blah");
		boolean result = objectUnderTest.matches(item);
		assertTrue("should be considered matching", result);
	}

	/**
	 * Can we tell when content DOESN'T match?
	 */
	@Test
	public void testMatches02() {
		HasI18NContentMatcher objectUnderTest = new HasI18NContentMatcher("something");
		I18NNonEmptyStringType item = getI18NWithContent("another thing");
		boolean result = objectUnderTest.matches(item);
		assertFalse("should NOT be considered matching", result);
	}

	/**
	 * Can we handle a null expectation?
	 */
	@Test
	public void testMatches03() {
		HasI18NContentMatcher objectUnderTest = new HasI18NContentMatcher(null);
		I18NNonEmptyStringType item = getI18NWithContent("another thing");
		boolean result = objectUnderTest.matches(item);
		assertFalse("should NOT be considered matching", result);
	}

	/**
	 * Can we handle a null item?
	 */
	@Test
	public void testMatches04() {
		HasI18NContentMatcher objectUnderTest = new HasI18NContentMatcher("something");
		I18NNonEmptyStringType item = null;
		boolean result = objectUnderTest.matches(item);
		assertFalse("should NOT be considered matching", result);
	}

	/**
	 * Can we handle an item with no content?
	 */
	@Test
	public void testMatches05() {
		HasI18NContentMatcher objectUnderTest = new HasI18NContentMatcher("something");
		I18NNonEmptyStringType item = getI18NWithoutContent();
		boolean result = objectUnderTest.matches(item);
		assertFalse("should NOT be considered matching", result);
	}

	/**
	 * Can we survive an item that contains content but it is null?
	 */
	@Test
	public void testMatches06() {
		HasI18NContentMatcher objectUnderTest = new HasI18NContentMatcher("something");
		I18NNonEmptyStringType item = getI18NWithOneNullContent();
		boolean result = objectUnderTest.matches(item);
		assertFalse("should NOT be considered matching", result);
	}

	private I18NNonEmptyStringType getI18NWithContent(String text) {
		I18NNonEmptyStringType result = new I18NNonEmptyStringType();
		result.getContent().add(text);
		return result;
	}

	private I18NNonEmptyStringType getI18NWithoutContent() {
		return new I18NNonEmptyStringType();
	}

	private I18NNonEmptyStringType getI18NWithOneNullContent() {
		I18NNonEmptyStringType result = new I18NNonEmptyStringType();
		result.getContent().add(null);
		return result;
	}
}
