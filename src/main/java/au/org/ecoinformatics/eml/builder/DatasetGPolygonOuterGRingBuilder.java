package au.org.ecoinformatics.eml.builder;

import java.util.ArrayList;
import java.util.List;

import au.org.ecoinformatics.eml.jaxb.GRingPointType;
import au.org.ecoinformatics.eml.jaxb.GeographicCoverage.DatasetGPolygon.DatasetGPolygonOuterGRing;

public class DatasetGPolygonOuterGRingBuilder {

	private List<GRingPointType> gRingPoints = new ArrayList<GRingPointType>();
	private String gRing;

	public DatasetGPolygonOuterGRingBuilder(
			GRingPointTypeBuilder gRingPoint1, GRingPointTypeBuilder gRingPoint2,
			GRingPointTypeBuilder gRingPoint3, GRingPointTypeBuilder...morePoints) {
		this.gRingPoints.add(gRingPoint1.build());
		this.gRingPoints.add(gRingPoint2.build());
		this.gRingPoints.add(gRingPoint3.build());
		for (GRingPointTypeBuilder currPoint : morePoints) {
			this.gRingPoints.add(currPoint.build());
		}
	}

	public DatasetGPolygonOuterGRingBuilder(String gRing) {
		this.gRing = gRing;
	}

	public DatasetGPolygonOuterGRing build() {
		DatasetGPolygonOuterGRing result = new DatasetGPolygonOuterGRing();
		result.getGRingPoint().addAll(gRingPoints);
		if (isSupplied(gRing)) {
			result.setGRing(gRing);
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
