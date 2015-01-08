package au.org.ecoinformatics.eml.builder;


public class IntellectualRightsBuilder extends AbstractTextTypeBuilder<IntellectualRightsBuilder> {

	public IntellectualRightsBuilder(String content) {
		super(content);
	}

	@Override
	public IntellectualRightsBuilder addPara(String extraContent) {
		super.addContent(extraContent);
		return this;
	}
}
