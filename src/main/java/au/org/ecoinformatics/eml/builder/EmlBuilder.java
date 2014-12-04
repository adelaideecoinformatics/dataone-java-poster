package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.DatasetType;
import au.org.ecoinformatics.eml.jaxb.Eml;

public class EmlBuilder {

	private final String packageId;
	private DatasetType dataset;

	public EmlBuilder(String packageId) {
		this.packageId = packageId;
	}

	public EmlBuilder dataset(DatasetType dataset) {
		this.dataset = dataset;
		return this;
	}

	public Eml build() {
		Eml result = new Eml();
		result.setPackageId(packageId);
		result.setDataset(dataset);
		return result;
	}

}
