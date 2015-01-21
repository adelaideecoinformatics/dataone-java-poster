package au.org.ecoinformatics.eml.builder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import au.org.ecoinformatics.eml.jaxb.Coverage;
import au.org.ecoinformatics.eml.jaxb.DatasetType;
import au.org.ecoinformatics.eml.jaxb.DistributionType;
import au.org.ecoinformatics.eml.jaxb.ProtocolType.KeywordSet;
import au.org.ecoinformatics.eml.jaxb.ResponsibleParty;
import au.org.ecoinformatics.eml.jaxb.TextType;

public class DatasetTypeBuilder {

	private String datasetTitle;
	private ResponsibleParty creator;
	private TextType abstractPara;
	private List<KeywordSet> keywordSets = new LinkedList<KeywordSet>();
	private TextType intellectualRights;
	private List<DistributionType> distributions = new ArrayList<DistributionType>();
	private Coverage coverage;

	public DatasetTypeBuilder datasetTitle(String datasetTitle) {
		//FIXME support maxOccurs="unbounded"
		this.datasetTitle = datasetTitle;
		return this;
	}

	public DatasetTypeBuilder creator(ResponsiblePartyBuilder creatorBuilder) {
		//FIXME support maxOccurs="unbounded"
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

	public DatasetTypeBuilder addDistribution(DistributionTypeBuilder distributionBuilder) {
		this.distributions.add(distributionBuilder.build());
		return this;
	}

	public DatasetTypeBuilder coverage(CoverageBuilder coverageBuilder) {
		this.coverage = coverageBuilder.build();
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
		result.getKeywordSet().addAll(keywordSets);
		if (isSupplied(intellectualRights)) {
			result.setIntellectualRights(intellectualRights);
		}
		result.getDistribution().addAll(distributions);
		if (isSupplied(coverage)) {
			result.setCoverage(coverage);
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
