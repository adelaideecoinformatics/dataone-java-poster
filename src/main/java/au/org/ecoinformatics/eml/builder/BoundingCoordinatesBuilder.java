package au.org.ecoinformatics.eml.builder;

import java.math.BigDecimal;

import au.org.ecoinformatics.eml.jaxb.GeographicCoverage.BoundingCoordinates;
import au.org.ecoinformatics.eml.jaxb.GeographicCoverage.BoundingCoordinates.BoundingAltitudes;

public class BoundingCoordinatesBuilder {

	private BigDecimal westBoundingCoordinate;
	private BigDecimal eastBoundingCoordinate;
	private BigDecimal northBoundingCoordinate;
	private BigDecimal southBoundingCoordinate;
	private BoundingAltitudes boundingAltitudes;

	public BoundingCoordinatesBuilder(
			BigDecimal westBoundingCoordinate, BigDecimal eastBoundingCoordinate,
			BigDecimal northBoundingCoordinate, BigDecimal southBoundingCoordinate) {
		this.westBoundingCoordinate = westBoundingCoordinate;
		this.eastBoundingCoordinate = eastBoundingCoordinate;
		this.northBoundingCoordinate = northBoundingCoordinate;
		this.southBoundingCoordinate = southBoundingCoordinate;
	}
	
	public BoundingCoordinatesBuilder boundingAltitudes(BoundingAltitudesBuilder boundingAltitudesBuilder) {
		this.boundingAltitudes = boundingAltitudesBuilder.build();
		return this;
	}

	public BoundingCoordinates build() {
		BoundingCoordinates result = new BoundingCoordinates();
		result.setWestBoundingCoordinate(westBoundingCoordinate);
		result.setEastBoundingCoordinate(eastBoundingCoordinate);
		result.setNorthBoundingCoordinate(northBoundingCoordinate);
		result.setSouthBoundingCoordinate(southBoundingCoordinate);
		if (isSupplied(boundingAltitudes)) {
			result.setBoundingAltitudes(boundingAltitudes);
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
