package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.GeographicCoverage.BoundingCoordinates.BoundingAltitudes;
import au.org.ecoinformatics.eml.jaxb.LengthUnitType;

public class BoundingAltitudesBuilderTest {

	/**
	 * Can we build with the bare minimum elements?
	 */
	@Test
	public void testBuild01() {
		int altitudeMinimum = 11;
		int altitudeMaxiumum = 22;
		LengthUnitType altitudeUnits = LengthUnitType.CENTIMETER;
		BoundingAltitudesBuilder objectUnderTest = new BoundingAltitudesBuilder(altitudeMinimum, altitudeMaxiumum, altitudeUnits);
		BoundingAltitudes result = objectUnderTest.build();
		assertThat(result.getAltitudeMinimum().intValue(), is(11));
		assertThat(result.getAltitudeMaximum().intValue(), is(22));
		assertThat(result.getAltitudeUnits(), is(LengthUnitType.CENTIMETER));
	}
}
