package au.org.ecoinformatics.eml.matchers;

import java.io.Serializable;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import au.org.ecoinformatics.eml.jaxb.I18NNonEmptyStringType;

class HasI18NContentMatcher extends BaseMatcher<I18NNonEmptyStringType> {

	private static final int FIRST_ITEM = 0;
	private final String expectedContent;

	public HasI18NContentMatcher(String expectedContent) {
		this.expectedContent = expectedContent;
	}

	@Override
	public boolean matches(Object item) {
		if (item == null) {
			return false;
		}
		I18NNonEmptyStringType castItem = (I18NNonEmptyStringType) item;
		if (castItem.getContent().isEmpty()) {
			return false;
		}
		Serializable contentOfFirstItem = castItem.getContent().get(FIRST_ITEM);
		if (contentOfFirstItem == null) {
			return false;
		}
		if (contentOfFirstItem.equals(expectedContent)) {
			return true;
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("something with the content '" + expectedContent + "'");
	}
}
