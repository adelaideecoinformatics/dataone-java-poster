package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.GeographicCoverage.DatasetGPolygon;

public class DatasetGPolygonBuilderTest {

	/**
	 * Can we build with the mandatory 'DatasetGPolygonOuterGRing' element?
	 */
	@Test
	public void testDatasetGPolygonOuterGRing01() {
		String gRing = "POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))";
		DatasetGPolygonOuterGRingBuilder datasetGPolygonOuterGRing = new DatasetGPolygonOuterGRingBuilder(gRing);
		DatasetGPolygonBuilder objectUnderTest = new DatasetGPolygonBuilder(datasetGPolygonOuterGRing);
		DatasetGPolygon result = objectUnderTest.build();
		assertThat(result.getDatasetGPolygonOuterGRing().getGRing(), is(gRing));
		assertThat(result.getDatasetGPolygonExclusionGRing().size(), is(0));
	}
	
	/**
	 * Can we build with one 'datasetGPolygonExclusionGRing' element?
	 */
	@Test
	public void testAddDatasetGPolygonExclusionGRing01() {
		DatasetGPolygonBuilder objectUnderTest = new DatasetGPolygonBuilder(
				new DatasetGPolygonOuterGRingBuilder("POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))"));
		DatasetGPolygonExclusionGRingBuilder datasetGPolygonExclusionGRingBuilder = new DatasetGPolygonExclusionGRingBuilder(new GRingPointTypeBuilder(1, 2));
		DatasetGPolygon result = objectUnderTest.addDatasetGPolygonExclusionGRing(datasetGPolygonExclusionGRingBuilder).build();
		assertThat(result.getDatasetGPolygonExclusionGRing().size(), is(1));
		assertThat(result.getDatasetGPolygonExclusionGRing().get(0).getGRingPoint().get(0).getGRingLatitude().intValue(), is(1));
	}
}
