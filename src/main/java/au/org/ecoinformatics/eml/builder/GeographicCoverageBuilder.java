package au.org.ecoinformatics.eml.builder;

import java.util.ArrayList;
import java.util.List;

import au.org.ecoinformatics.eml.jaxb.GeographicCoverage;
import au.org.ecoinformatics.eml.jaxb.GeographicCoverage.BoundingCoordinates;
import au.org.ecoinformatics.eml.jaxb.GeographicCoverage.DatasetGPolygon;
import au.org.ecoinformatics.eml.jaxb.ViewType.References;

public class GeographicCoverageBuilder {

	private String geographicDescription;
	private References references;
	private BoundingCoordinates boundingCoordinates;
	private List<DatasetGPolygon> datasetGPolygons = new ArrayList<DatasetGPolygon>();

	public GeographicCoverageBuilder(String geographicDescription, BoundingCoordinatesBuilder boundingCoordinatesBuilder) {
		this.geographicDescription = geographicDescription;
		this.boundingCoordinates = boundingCoordinatesBuilder.build();
	}

	public GeographicCoverageBuilder(ReferencesBuilder referencesBuilder) {
		this.references = referencesBuilder.build();
	}
	
	public GeographicCoverageBuilder addDatasetGPolygon(DatasetGPolygonBuilder datasetGPolygonBuilder) {
		this.datasetGPolygons.add(datasetGPolygonBuilder.build());
		return this;
	}

	public GeographicCoverage build() {
		GeographicCoverage result = new GeographicCoverage();
		if (isSupplied(geographicDescription)) {
			result.setGeographicDescription(geographicDescription);
		}
		if (isSupplied(references)) {
			result.setReferences(references);
		}
		if (isSupplied(boundingCoordinates)) {
			result.setBoundingCoordinates(boundingCoordinates);
		}
		result.getDatasetGPolygon().addAll(datasetGPolygons);
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
