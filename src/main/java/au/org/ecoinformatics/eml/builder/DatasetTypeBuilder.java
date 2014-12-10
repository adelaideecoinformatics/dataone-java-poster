package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.DatasetType;
import au.org.ecoinformatics.eml.jaxb.ResponsibleParty;
import au.org.ecoinformatics.eml.jaxb.TextType;

public class DatasetTypeBuilder {

	private String datasetTitle;
	private ResponsibleParty creator;
	private TextType abstractPara;

	public DatasetTypeBuilder datasetTitle(String datasetTitle) {
		this.datasetTitle = datasetTitle;
		return this;
	}

	public DatasetTypeBuilder creator(ResponsibleParty creator) {
		this.creator = creator;
		return this;
	}

	public DatasetTypeBuilder abstractPara(TextType abstractPara) {
		this.abstractPara = abstractPara;
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
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
