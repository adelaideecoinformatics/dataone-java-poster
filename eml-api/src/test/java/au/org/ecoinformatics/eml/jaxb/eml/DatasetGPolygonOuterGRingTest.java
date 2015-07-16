package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.GRingPointType;
import au.org.ecoinformatics.eml.jaxb.eml.GeographicCoverage.DatasetGPolygon.DatasetGPolygonOuterGRing;

public class DatasetGPolygonOuterGRingTest {

	/**
	 * Can we build with the minimum valid points (3)?
	 */
	@Test
	public void testGRingPoint01() {
		DatasetGPolygonOuterGRing result = new DatasetGPolygonOuterGRing()
			.withGRingPoint(new GRingPointType().withGRingLatitude(new BigDecimal(1)))
			.withGRingPoint(new GRingPointType().withGRingLatitude(new BigDecimal(2)))
			.withGRingPoint(new GRingPointType().withGRingLatitude(new BigDecimal(3)));
		assertThat(result.getGRingPoint().size(), is(3));
		assertThat(result.getGRingPoint().get(0).getGRingLatitude().intValue(), is(1));
		assertThat(result.getGRingPoint().get(1).getGRingLatitude().intValue(), is(2));
		assertThat(result.getGRingPoint().get(2).getGRingLatitude().intValue(), is(3));
	}
	
	/**
	 * Can we build with more than the minimum valid points?
	 */
	@Test
	public void testGRingPoint02() {
		DatasetGPolygonOuterGRing result = new DatasetGPolygonOuterGRing()
			.withGRingPoint(new GRingPointType().withGRingLatitude(new BigDecimal(1)))
			.withGRingPoint(new GRingPointType().withGRingLatitude(new BigDecimal(2)))
			.withGRingPoint(new GRingPointType().withGRingLatitude(new BigDecimal(3)))
			.withGRingPoint(new GRingPointType().withGRingLatitude(new BigDecimal(4)))
			.withGRingPoint(new GRingPointType().withGRingLatitude(new BigDecimal(5)));
		assertThat(result.getGRingPoint().size(), is(5));
		assertThat(result.getGRingPoint().get(3).getGRingLatitude().intValue(), is(4));
		assertThat(result.getGRingPoint().get(4).getGRingLatitude().intValue(), is(5));
	}
	
	/**
	 * Can we build with a 'gRing'?
	 */
	@Test
	public void testGRing01() {
		String gRing = "POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))";
		DatasetGPolygonOuterGRing result = new DatasetGPolygonOuterGRing()
			.withGRing(gRing);
		assertThat(result.getGRing(), is(gRing));
		assertThat(result.getGRingPoint().size(), is(0));
	}
}
