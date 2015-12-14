package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.GRingPointType;
import au.org.ecoinformatics.eml.jaxb.eml.GeographicCoverage.DatasetGPolygon;
import au.org.ecoinformatics.eml.jaxb.eml.GeographicCoverage.DatasetGPolygon.DatasetGPolygonExclusionGRing;
import au.org.ecoinformatics.eml.jaxb.eml.GeographicCoverage.DatasetGPolygon.DatasetGPolygonOuterGRing;

public class DatasetGPolygonTest {

	/**
	 * Can we build with the mandatory 'DatasetGPolygonOuterGRing' element?
	 */
	@Test
	public void testDatasetGPolygonOuterGRing01() {
		String gRing = "POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))";
		DatasetGPolygon result = new DatasetGPolygon()
			.withDatasetGPolygonOuterGRing(new DatasetGPolygonOuterGRing().withGRing(gRing));
		assertThat(result.getDatasetGPolygonOuterGRing().getGRing(), is(gRing));
		assertThat(result.getDatasetGPolygonExclusionGRing().size(), is(0));
	}
	
	/**
	 * Can we build with one 'datasetGPolygonExclusionGRing' element?
	 */
	@Test
	public void testAddDatasetGPolygonExclusionGRing01() {
		DatasetGPolygon result = new DatasetGPolygon()
			.withDatasetGPolygonExclusionGRing(new DatasetGPolygonExclusionGRing().withGRingPoint(new GRingPointType().withGRingLatitude(new BigDecimal(111))));
		assertThat(result.getDatasetGPolygonExclusionGRing().size(), is(1));
		assertThat(result.getDatasetGPolygonExclusionGRing().get(0).getGRingPoint().get(0).getGRingLatitude().intValue(), is(111));
	}
}
