package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.GeographicCoverage.DatasetGPolygon.DatasetGPolygonExclusionGRing;

public class DatasetGPolygonExclusionGRingBuilderTest {

	/**
	 * Can we build with a 'gRing'?
	 */
	@Test
	public void testGRing01() {
		String gRing = "POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))";
		DatasetGPolygonExclusionGRingBuilder objectUnderTest = new DatasetGPolygonExclusionGRingBuilder(gRing);
		DatasetGPolygonExclusionGRing result = objectUnderTest.build();
		assertThat(result.getGRing(), is(gRing));
	}
	
	/**
	 * Can we build with 'gRingPoint's?
	 */
	@Test
	public void testGRingPoint01() {
		GRingPointTypeBuilder gRingPoint1 = new GRingPointTypeBuilder(1, 11);
		GRingPointTypeBuilder gRingPoint2 = new GRingPointTypeBuilder(2, 22);
		GRingPointTypeBuilder gRingPoint3 = new GRingPointTypeBuilder(3, 33);
		DatasetGPolygonExclusionGRingBuilder objectUnderTest = new DatasetGPolygonExclusionGRingBuilder(
				gRingPoint1, gRingPoint2, gRingPoint3);
		DatasetGPolygonExclusionGRing result = objectUnderTest.build();
		assertNull(result.getGRing());
		assertThat(result.getGRingPoint().size(), is(3));
		assertThat(result.getGRingPoint().get(0).getGRingLatitude().intValue(), is(1));
		assertThat(result.getGRingPoint().get(1).getGRingLatitude().intValue(), is(2));
		assertThat(result.getGRingPoint().get(2).getGRingLatitude().intValue(), is(3));
	}
}
