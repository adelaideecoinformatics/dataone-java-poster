package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.I18NNonEmptyStringType;

public class I18NNonEmptyStringTypeBuilder {

	private final String content;

	public I18NNonEmptyStringTypeBuilder(String content) {
		this.content = content;
	}

	public I18NNonEmptyStringType build() {
		I18NNonEmptyStringType result = new I18NNonEmptyStringType();
		result.getContent().add(content);
		return result;
	}

}
