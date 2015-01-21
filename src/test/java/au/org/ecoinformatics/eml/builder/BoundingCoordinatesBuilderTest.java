package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.GeographicCoverage.BoundingCoordinates;
import au.org.ecoinformatics.eml.jaxb.LengthUnitType;

public class BoundingCoordinatesBuilderTest {

	/**
	 * Can we build with all the mandatory bounding coordinates?
	 */
	@Test
	public void testBoundingCoordinates01() {
		BigDecimal westBoundingCoordinate = new BigDecimal(11);
		BigDecimal eastBoundingCoordinate = new BigDecimal(22);
		BigDecimal northBoundingCoordinate = new BigDecimal(33);
		BigDecimal southBoundingCoordinate = new BigDecimal(44);
		BoundingCoordinatesBuilder objectUnderTest = new BoundingCoordinatesBuilder(
				westBoundingCoordinate, eastBoundingCoordinate, northBoundingCoordinate, southBoundingCoordinate);
		BoundingCoordinates result = objectUnderTest.build();
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
		BoundingCoordinatesBuilder objectUnderTest = new BoundingCoordinatesBuilder(
				new BigDecimal(11), new BigDecimal(22), new BigDecimal(33), new BigDecimal(44));
		BoundingAltitudesBuilder boundingAltitudesBuilder = new BoundingAltitudesBuilder(1, 2, LengthUnitType.DEKAMETER);
		BoundingCoordinates result = objectUnderTest.boundingAltitudes(boundingAltitudesBuilder).build();
		assertThat(result.getBoundingAltitudes().getAltitudeMinimum().intValue(), is(1));
	}
	
	/**
	 * Are all optional items empty when they aren't supplied?
	 */
	@Test
	public void testBuild01() {
		BoundingCoordinatesBuilder objectUnderTest = new BoundingCoordinatesBuilder(
				new BigDecimal(11), new BigDecimal(22), new BigDecimal(33), new BigDecimal(44));
		BoundingCoordinates result = objectUnderTest.build();
		assertNull(result.getBoundingAltitudes());
	}
}
