package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.DatasetType;
import au.org.ecoinformatics.eml.jaxb.ResponsibleParty;

public class DatasetTypeBuilder {

	private String datasetTitle;
	private ResponsibleParty creator;

	public DatasetTypeBuilder datasetTitle(String datasetTitle) {
		this.datasetTitle = datasetTitle;
		return this;
	}

	public DatasetTypeBuilder creator(ResponsibleParty creator) {
		this.creator = creator;
		return this;
	}

	public DatasetType build() {
		DatasetType result = new DatasetType();
		result.getTitle().add(new I18NNonEmptyStringTypeBuilder(datasetTitle).build());
		result.getCreator().add(creator);
		return result;
	}
}
