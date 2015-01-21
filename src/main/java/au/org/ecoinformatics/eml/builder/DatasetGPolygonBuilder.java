package au.org.ecoinformatics.eml.builder;

import java.util.ArrayList;
import java.util.List;

import au.org.ecoinformatics.eml.jaxb.GeographicCoverage.DatasetGPolygon;
import au.org.ecoinformatics.eml.jaxb.GeographicCoverage.DatasetGPolygon.DatasetGPolygonExclusionGRing;
import au.org.ecoinformatics.eml.jaxb.GeographicCoverage.DatasetGPolygon.DatasetGPolygonOuterGRing;

public class DatasetGPolygonBuilder {

	private DatasetGPolygonOuterGRing datasetGPolygonOuterGRing;
	private List<DatasetGPolygonExclusionGRing> datasetGPolygonExclusionGRings = new ArrayList<DatasetGPolygonExclusionGRing>();

	public DatasetGPolygonBuilder(DatasetGPolygonOuterGRingBuilder datasetGPolygonOuterGRingBuilder) {
		this.datasetGPolygonOuterGRing = datasetGPolygonOuterGRingBuilder.build();
	}
	
	public DatasetGPolygonBuilder addDatasetGPolygonExclusionGRing(
			DatasetGPolygonExclusionGRingBuilder datasetGPolygonExclusionGRingBuilder) {
		this.datasetGPolygonExclusionGRings.add(datasetGPolygonExclusionGRingBuilder.build());
		return this;
	}

	public DatasetGPolygon build() {
		DatasetGPolygon result = new DatasetGPolygon();
		result.setDatasetGPolygonOuterGRing(datasetGPolygonOuterGRing);
		result.getDatasetGPolygonExclusionGRing().addAll(datasetGPolygonExclusionGRings);
		return result;
	}
}
