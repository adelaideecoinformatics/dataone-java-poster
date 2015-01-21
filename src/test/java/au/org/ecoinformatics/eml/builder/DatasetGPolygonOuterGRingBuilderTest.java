package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.GeographicCoverage.DatasetGPolygon.DatasetGPolygonOuterGRing;

public class DatasetGPolygonOuterGRingBuilderTest {

	/**
	 * Can we build with the minimum valid points (3)?
	 */
	@Test
	public void testGRingPoint01() {
		GRingPointTypeBuilder gRingPoint1 = new GRingPointTypeBuilder(1, 11);
		GRingPointTypeBuilder gRingPoint2 = new GRingPointTypeBuilder(2, 22);
		GRingPointTypeBuilder gRingPoint3 = new GRingPointTypeBuilder(3, 33);
		DatasetGPolygonOuterGRingBuilder objectUnderTest = new DatasetGPolygonOuterGRingBuilder(gRingPoint1, gRingPoint2, gRingPoint3);
		DatasetGPolygonOuterGRing result = objectUnderTest.build();
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
		DatasetGPolygonOuterGRingBuilder objectUnderTest = new DatasetGPolygonOuterGRingBuilder(
				new GRingPointTypeBuilder(1, 11), 
				new GRingPointTypeBuilder(2, 22), 
				new GRingPointTypeBuilder(3, 33),
				new GRingPointTypeBuilder(4, 44),
				new GRingPointTypeBuilder(5, 55));
		DatasetGPolygonOuterGRing result = objectUnderTest.build();
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
		DatasetGPolygonOuterGRingBuilder objectUnderTest = new DatasetGPolygonOuterGRingBuilder(gRing);
		DatasetGPolygonOuterGRing result = objectUnderTest.build();
		assertThat(result.getGRing(), is(gRing));
		assertThat(result.getGRingPoint().size(), is(0));
	}
}
