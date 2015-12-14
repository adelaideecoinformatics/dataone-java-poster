package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.GeographicCoverage;
import au.org.ecoinformatics.eml.jaxb.eml.GeographicCoverage.BoundingCoordinates;
import au.org.ecoinformatics.eml.jaxb.eml.GeographicCoverage.DatasetGPolygon;
import au.org.ecoinformatics.eml.jaxb.eml.GeographicCoverage.DatasetGPolygon.DatasetGPolygonOuterGRing;
import au.org.ecoinformatics.eml.jaxb.eml.ViewType.References;

public class GeographicCoverageTest {

	/**
	 * Can we build with 'geographic description' and 'bounding coordinates' elements?
	 */
	@Test
	public void testBuild01() {
		String geographicDescription = "some desc";
		GeographicCoverage result = new GeographicCoverage()
			.withBoundingCoordinates(new BoundingCoordinates().withEastBoundingCoordinate(new BigDecimal(2)))
			.withGeographicDescription(geographicDescription);
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
		String gRing = "POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))";
		GeographicCoverage result = new GeographicCoverage()
			.withDatasetGPolygon(new DatasetGPolygon().withDatasetGPolygonOuterGRing(new DatasetGPolygonOuterGRing().withGRing(gRing)));
		assertThat(result.getDatasetGPolygon().size(), is(1));
		assertThat(result.getDatasetGPolygon().get(0).getDatasetGPolygonOuterGRing().getGRing(), is(gRing));
	}
	
	/**
	 * Can we build with a 'references' element?
	 */
	@Test
	public void testReferences01() {
		String referencesText = "some ref";
		GeographicCoverage result = new GeographicCoverage()
			.withReferences(new References().withValue(referencesText));
		assertThat(result.getReferences().getValue(), is(referencesText));
		assertNull(result.getGeographicDescription());
		assertNull(result.getBoundingCoordinates());
		assertThat(result.getDatasetGPolygon().size(), is(0));
	}
}
