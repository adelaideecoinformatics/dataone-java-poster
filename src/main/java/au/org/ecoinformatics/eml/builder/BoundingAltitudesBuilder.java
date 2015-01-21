package au.org.ecoinformatics.eml.builder;

import java.math.BigDecimal;

import au.org.ecoinformatics.eml.jaxb.GeographicCoverage.BoundingCoordinates.BoundingAltitudes;
import au.org.ecoinformatics.eml.jaxb.LengthUnitType;

public class BoundingAltitudesBuilder {

	private int altitudeMinimum;
	private int altitudeMaxiumum;
	private LengthUnitType altitudeUnits;

	public BoundingAltitudesBuilder(int altitudeMinimum, int altitudeMaxiumum, LengthUnitType altitudeUnits) {
		this.altitudeMinimum = altitudeMinimum;
		this.altitudeMaxiumum = altitudeMaxiumum;
		this.altitudeUnits = altitudeUnits;
	}

	public BoundingAltitudes build() {
		BoundingAltitudes result = new BoundingAltitudes();
		result.setAltitudeMinimum(new BigDecimal(altitudeMinimum));
		result.setAltitudeMaximum(new BigDecimal(altitudeMaxiumum));
		result.setAltitudeUnits(altitudeUnits);
		return result;
	}

}
