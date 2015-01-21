package au.org.ecoinformatics.eml.builder;

import java.util.ArrayList;
import java.util.List;

import au.org.ecoinformatics.eml.jaxb.GRingPointType;
import au.org.ecoinformatics.eml.jaxb.GeographicCoverage.DatasetGPolygon.DatasetGPolygonExclusionGRing;

public class DatasetGPolygonExclusionGRingBuilder {

	private String gRing;
	private List<GRingPointType> gRingPoints = new ArrayList<GRingPointType>();

	public DatasetGPolygonExclusionGRingBuilder(String gRing) {
		this.gRing = gRing;
	}

	public DatasetGPolygonExclusionGRingBuilder(GRingPointTypeBuilder...gRingPoints) {
		for (GRingPointTypeBuilder currPoint : gRingPoints) {
			this.gRingPoints.add(currPoint.build());
		}
	}

	public DatasetGPolygonExclusionGRing build() {
		DatasetGPolygonExclusionGRing result = new DatasetGPolygonExclusionGRing();
		if (isSupplied(gRing)) {
			result.setGRing(gRing);
		}
		if (!gRingPoints.isEmpty()) {
			result.getGRingPoint().addAll(gRingPoints);
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
