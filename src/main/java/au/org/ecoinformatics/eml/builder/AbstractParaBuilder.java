package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.TextType;


/**
 * Builds {@link TextType} objects for the <code>&lt;abstract&gt;</code> element.
 * 
 * @author Tom Saleeba
 */
public class AbstractParaBuilder extends AbstractTextTypeBuilder<AbstractParaBuilder> {

	public AbstractParaBuilder(String content) {
		super(content);
	}

	@Override
	public AbstractParaBuilder addPara(String extraContent) {
		super.addContent(extraContent);
		return this;
	}
}
