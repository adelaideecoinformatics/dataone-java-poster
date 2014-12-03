package au.org.ecoinformatics.eml.matchers;

import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import au.org.ecoinformatics.eml.jaxb.I18NNonEmptyStringType;

class HasI18NContentMatcher extends BaseMatcher<List<I18NNonEmptyStringType>> {

	private static final int FIRST_ITEM = 0;
	private final String expectedContent;

	public HasI18NContentMatcher(String expectedContent) {
		this.expectedContent = expectedContent;
	}

	@Override
	public boolean matches(Object item) {
		List<I18NNonEmptyStringType> castItem = (List<I18NNonEmptyStringType>) item;
		if (castItem.isEmpty()) {
			return false;
		}
		if (castItem.get(FIRST_ITEM).getContent().isEmpty()) {
			return false;
		}
		if (castItem.get(FIRST_ITEM).getContent().get(FIRST_ITEM).equals(expectedContent)) {
			return true;
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("something with the content '" + expectedContent + "'");
	}
}
