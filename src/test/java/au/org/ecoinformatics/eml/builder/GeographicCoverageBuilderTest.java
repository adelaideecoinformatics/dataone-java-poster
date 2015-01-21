package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.GeographicCoverage;

public class GeographicCoverageBuilderTest {

	/**
	 * Can we build with 'geographic description' and 'bounding coordinates' elements?
	 */
	@Test
	public void testBuild01() {
		String geographicDescription = "some desc";
		BoundingCoordinatesBuilder boundingCoordinatesBuilder = new BoundingCoordinatesBuilder(new BigDecimal(1), new BigDecimal(2), new BigDecimal(3), new BigDecimal(4));
		GeographicCoverageBuilder objectUnderTest = new GeographicCoverageBuilder(geographicDescription, boundingCoordinatesBuilder);
		GeographicCoverage result = objectUnderTest.build();
		assertThat(result.getGeographicDescription(), is(geographicDescription));
		assertThat(result.getBoundingCoordinates().getEastBoundingCoordinate(), is(new BigDecimal(2)));
		assertThat(result.getDatasetGPolygon().size(), is(0));
		assertNull(result.getReferences());
	}
	
	/**
	 * Can we build with one 'datasetGPolygon' element?
	 */
	@Test
	public void testGeographicDescription01() {
		GeographicCoverageBuilder objectUnderTest = new GeographicCoverageBuilder("some desc", 
				new BoundingCoordinatesBuilder(new BigDecimal(1), new BigDecimal(2), new BigDecimal(3), new BigDecimal(4)));
		String gRing = "POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))";
		DatasetGPolygonBuilder datasetGPolygonBuilder = new DatasetGPolygonBuilder(new DatasetGPolygonOuterGRingBuilder(gRing));
		GeographicCoverage result = objectUnderTest.addDatasetGPolygon(datasetGPolygonBuilder).build();
		assertThat(result.getDatasetGPolygon().size(), is(1));
		assertThat(result.getDatasetGPolygon().get(0).getDatasetGPolygonOuterGRing().getGRing(), is(gRing));
	}
	
	/**
	 * Can we build with a 'references' element?
	 */
	@Test
	public void testReferences01() {
		String referencesText = "some ref";
		ReferencesBuilder referencesBuilder = new ReferencesBuilder(referencesText);
		GeographicCoverageBuilder objectUnderTest = new GeographicCoverageBuilder(referencesBuilder);
		GeographicCoverage result = objectUnderTest.build();
		assertThat(result.getReferences().getValue(), is(referencesText));
		assertNull(result.getGeographicDescription());
		assertNull(result.getBoundingCoordinates());
		assertThat(result.getDatasetGPolygon().size(), is(0));
	}
}
