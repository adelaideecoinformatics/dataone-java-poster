package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.TextType;

/**
 * Builds {@link TextType} objects for the <code>&lt;intellectualRights&gt;</code> element.
 * 
 * @author Tom Saleeba
 */
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
