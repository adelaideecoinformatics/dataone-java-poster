package au.org.ecoinformatics.eml.builder;

import java.util.LinkedList;
import java.util.List;

import au.org.ecoinformatics.eml.jaxb.TextType;

public class AbstractParaBuilder {

	private List<String> content = new LinkedList<String>();

	public AbstractParaBuilder(String content) {
		this.content.add(content);
	}

	public AbstractParaBuilder addPara(String extraContent) {
		this.content.add(extraContent);
		return this;
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
