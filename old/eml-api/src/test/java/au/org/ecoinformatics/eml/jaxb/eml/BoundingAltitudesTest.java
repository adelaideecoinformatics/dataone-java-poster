package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.GeographicCoverage.BoundingCoordinates.BoundingAltitudes;
import au.org.ecoinformatics.eml.jaxb.eml.LengthUnitType;

public class BoundingAltitudesTest {

	/**
	 * Can we build with all the elements?
	 */
	@Test
	public void testBuild01() {
		LengthUnitType altitudeUnits = LengthUnitType.CENTIMETER;
		BoundingAltitudes result = new BoundingAltitudes()
			.withAltitudeMinimum(new BigDecimal(11))
			.withAltitudeMaximum(new BigDecimal(22))
			.withAltitudeUnits(altitudeUnits);
		assertThat(result.getAltitudeMinimum().intValue(), is(11));
		assertThat(result.getAltitudeMaximum().intValue(), is(22));
		assertThat(result.getAltitudeUnits(), is(LengthUnitType.CENTIMETER));
	}
}
