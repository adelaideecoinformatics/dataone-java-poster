package au.org.ecoinformatics.eml.builder;

import java.util.LinkedList;
import java.util.List;

import au.org.ecoinformatics.eml.jaxb.ProtocolType.KeywordSet;
import au.org.ecoinformatics.eml.jaxb.ProtocolType.KeywordSet.Keyword;

public class KeywordSetBuilder {

	private List<Keyword> keywords = new LinkedList<Keyword>();

	public KeywordSetBuilder addKeyword(String keywordText) {
		Keyword keywordObj = new Keyword();
		keywordObj.getContent().add(keywordText);
		this.keywords.add(keywordObj);
		return this;
	}

	public KeywordSet build() {
		KeywordSet result = new KeywordSet();
		if (!keywords.isEmpty()) {
			result.getKeyword().addAll(keywords);
		}
		return result;
	}

}
