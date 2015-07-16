package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.GeographicCoverage.BoundingCoordinates;
import au.org.ecoinformatics.eml.jaxb.eml.GeographicCoverage.BoundingCoordinates.BoundingAltitudes;
import au.org.ecoinformatics.eml.jaxb.eml.LengthUnitType;

public class BoundingCoordinatesTest {

	/**
	 * Can we build with all the bounding coordinates?
	 */
	@Test
	public void testBoundingCoordinates01() {
		BigDecimal westBoundingCoordinate = new BigDecimal(11);
		BigDecimal eastBoundingCoordinate = new BigDecimal(22);
		BigDecimal northBoundingCoordinate = new BigDecimal(33);
		BigDecimal southBoundingCoordinate = new BigDecimal(44);
		BoundingCoordinates result = new BoundingCoordinates()
			.withWestBoundingCoordinate(westBoundingCoordinate)
			.withEastBoundingCoordinate(eastBoundingCoordinate)
			.withNorthBoundingCoordinate(northBoundingCoordinate)
			.withSouthBoundingCoordinate(southBoundingCoordinate);
		assertThat(result.getWestBoundingCoordinate().intValue(), is(11));
		assertThat(result.getEastBoundingCoordinate().intValue(), is(22));
		assertThat(result.getNorthBoundingCoordinate().intValue(), is(33));
		assertThat(result.getSouthBoundingCoordinate().intValue(), is(44));
	}
	
	/**
	 * Can we build with a 'bounding altitudes' element?
	 */
	@Test
	public void testBoundingAltitudes01() {
		BoundingCoordinates result = new BoundingCoordinates()
			.withBoundingAltitudes(
					new BoundingAltitudes()
						.withAltitudeMinimum(new BigDecimal(1))
						.withAltitudeMaximum(new BigDecimal(2))
						.withAltitudeUnits(LengthUnitType.DEKAMETER));
		assertThat(result.getBoundingAltitudes().getAltitudeMinimum().intValue(), is(1));
	}
	
	/**
	 * Are all items empty when they aren't supplied?
	 */
	@Test
	public void testBuild01() {
		BoundingCoordinates result = new BoundingCoordinates();
		assertNull(result.getBoundingAltitudes());
		assertNull(result.getEastBoundingCoordinate());
		assertNull(result.getWestBoundingCoordinate());
		assertNull(result.getNorthBoundingCoordinate());
		assertNull(result.getSouthBoundingCoordinate());
	}
}
