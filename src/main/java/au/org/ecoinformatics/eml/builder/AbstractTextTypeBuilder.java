package au.org.ecoinformatics.eml.builder;

import java.util.LinkedList;
import java.util.List;

import au.org.ecoinformatics.eml.jaxb.TextType;

/**
 * Builds {@link TextType} objects. Allows us to centralise the code
 * for doing so but still have intuitively named builders for each element.
 * 
 * @author Tom Saleeba
 */
abstract class AbstractTextTypeBuilder<T extends AbstractTextTypeBuilder<T>> {

	private List<String> content = new LinkedList<String>();

	public AbstractTextTypeBuilder(String content) {
		this.content.add(content);
	}

	/**
	 * Adds extra content to this TextType. Should be implemented like this:
	 * <pre>
	 * public YourTypeBuilder addPara(String extraContent) {
	 *   super.addContent(extraContent);
	 *   return this;
	 * }
	 * </pre>
	 * 
	 * @param extraContent	content to add
	 * @return				reference to this so builder calls can be chained.
	 */
	public abstract T addPara(String extraContent);
	
	protected void addContent(String extraContent) {
		this.content.add(extraContent);
	}

	public TextType build() {
		TextType result = new TextType();
		if (isSupplied(content)) {
			result.getContent().addAll(content);
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
