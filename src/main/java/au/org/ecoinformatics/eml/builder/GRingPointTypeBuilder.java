package au.org.ecoinformatics.eml.builder;

import java.math.BigDecimal;

import au.org.ecoinformatics.eml.jaxb.GRingPointType;

public class GRingPointTypeBuilder {

	private int gRingLatitude;
	private int gRingLongitude;

	public GRingPointTypeBuilder(int gRingLatitude, int gRingLongitude) {
		this.gRingLatitude = gRingLatitude;
		this.gRingLongitude = gRingLongitude;
		// TODO validate arguments are in range
	}

	public GRingPointType build() {
		GRingPointType result = new GRingPointType();
		result.setGRingLatitude(new BigDecimal(gRingLatitude));
		result.setGRingLongitude(new BigDecimal(gRingLongitude));
		return result;
	}

}
