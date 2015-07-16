package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.GRingPointType;
import au.org.ecoinformatics.eml.jaxb.eml.GeographicCoverage.DatasetGPolygon.DatasetGPolygonExclusionGRing;

public class DatasetGPolygonExclusionGRingTest {

	/**
	 * Can we build with a 'gRing'?
	 */
	@Test
	public void testGRing01() {
		String gRing = "POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))";
		DatasetGPolygonExclusionGRing result = new DatasetGPolygonExclusionGRing()
			.withGRing(gRing);
		assertThat(result.getGRing(), is(gRing));
	}
	
	/**
	 * Can we build with 'gRingPoint's?
	 */
	@Test
	public void testGRingPoint01() {
		DatasetGPolygonExclusionGRing result = new DatasetGPolygonExclusionGRing()
			.withGRingPoint(new GRingPointType().withGRingLatitude(new BigDecimal(1)))
			.withGRingPoint(new GRingPointType().withGRingLatitude(new BigDecimal(2)))
			.withGRingPoint(new GRingPointType().withGRingLatitude(new BigDecimal(3)));
		assertNull(result.getGRing());
		assertThat(result.getGRingPoint().size(), is(3));
		assertThat(result.getGRingPoint().get(0).getGRingLatitude().intValue(), is(1));
		assertThat(result.getGRingPoint().get(1).getGRingLatitude().intValue(), is(2));
		assertThat(result.getGRingPoint().get(2).getGRingLatitude().intValue(), is(3));
	}
}
