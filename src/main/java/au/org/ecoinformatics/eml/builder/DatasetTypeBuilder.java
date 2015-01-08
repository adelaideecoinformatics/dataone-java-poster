package au.org.ecoinformatics.eml.builder;

import java.util.LinkedList;
import java.util.List;

import au.org.ecoinformatics.eml.jaxb.DatasetType;
import au.org.ecoinformatics.eml.jaxb.ProtocolType.KeywordSet;
import au.org.ecoinformatics.eml.jaxb.ResponsibleParty;
import au.org.ecoinformatics.eml.jaxb.TextType;

public class DatasetTypeBuilder {

	private String datasetTitle;
	private ResponsibleParty creator;
	private TextType abstractPara;
	private List<KeywordSet> keywordSets = new LinkedList<KeywordSet>();
	private TextType intellectualRights;

	public DatasetTypeBuilder datasetTitle(String datasetTitle) {
		this.datasetTitle = datasetTitle;
		return this;
	}

	public DatasetTypeBuilder creator(ResponsiblePartyBuilder creatorBuilder) {
		this.creator = creatorBuilder.build();
		return this;
	}

	public DatasetTypeBuilder abstractPara(AbstractParaBuilder abstractParaBuilder) {
		this.abstractPara = abstractParaBuilder.build();
		return this;
	}

	public DatasetTypeBuilder addKeywordSet(KeywordSetBuilder keywordSetBuilder) {
		this.keywordSets.add(keywordSetBuilder.build());
		return this;
	}
	
	public DatasetTypeBuilder intellectualRights(IntellectualRightsBuilder intellectualRightsBuilder) {
		this.intellectualRights = intellectualRightsBuilder.build();
		return this;
	}

	public DatasetType build() {
		DatasetType result = new DatasetType();
		if (isSupplied(datasetTitle)) {
			result.getTitle().add(new I18NNonEmptyStringTypeBuilder(datasetTitle).build());
		}
		if (isSupplied(creator)) {
			result.getCreator().add(creator);
		}
		if (isSupplied(abstractPara)) {
			result.setAbstract(abstractPara);
		}
		if (!keywordSets.isEmpty()) {
			result.getKeywordSet().addAll(keywordSets);
		}
		if (isSupplied(intellectualRights)) {
			result.setIntellectualRights(intellectualRights);
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
