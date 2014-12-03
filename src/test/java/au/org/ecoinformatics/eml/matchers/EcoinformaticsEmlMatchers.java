package au.org.ecoinformatics.eml.matchers;

import java.util.List;

import org.hamcrest.Matcher;

import au.org.ecoinformatics.eml.jaxb.I18NNonEmptyStringType;

public class EcoinformaticsEmlMatchers {
	public static Matcher<? super List<I18NNonEmptyStringType>> hasI18NContent(String expectedContent) {
		return new HasI18NContentMatcher(expectedContent);
	}
}
