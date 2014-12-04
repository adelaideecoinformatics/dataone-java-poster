package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.DatasetType;

public class DatasetTypeBuilder {

	private String datasetTitle;

	public DatasetTypeBuilder datasetTitle(String datasetTitle) {
		this.datasetTitle = datasetTitle;
		return this;
	}

	public DatasetType build() {
		DatasetType result = new DatasetType();
		result.getTitle().add(new I18NNonEmptyStringTypeBuilder(datasetTitle).build());
		return result;
	}
}
