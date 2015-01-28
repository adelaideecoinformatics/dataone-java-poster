package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.ViewType.References;

public class CoverageTest {

	/**
	 * Can we build with two 'geographic coverage' elements?
	 */
	@Test
	public void testGeographicCoverage01() {
		String id1 = "geoCov1";
		String id2 = "geoCov2";
		Coverage result = new Coverage()
			.withGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage(
					new GeographicCoverage().withId(id1))
			.withGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage(
					new GeographicCoverage().withId(id2));
		assertThat(result.getGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage().size(), is(2));
		GeographicCoverage firstCoverage = (GeographicCoverage) result.getGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage().get(0);
		assertThat(firstCoverage.getId().get(0), is(id1));
		GeographicCoverage secondCoverage = (GeographicCoverage) result.getGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage().get(1);
		assertThat(secondCoverage.getId().get(0), is(id2));
	}
	
	/**
	 * Can we build with two 'temporal coverage' elements?
	 */
	@Test
	public void testTemporalCoverage01() {
		String calendarDate1 = "2011-11-11";
		String calendarDate2 = "2022-02-22";
		Coverage result = new Coverage()
			.withGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage(
					new ObjectFactory().createTemporalCoverage().withSingleDateTime(new SingleDateTimeType().withCalendarDate(calendarDate1)))
			.withGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage(
					new ObjectFactory().createTemporalCoverage().withSingleDateTime(new SingleDateTimeType().withCalendarDate(calendarDate2)));
		assertThat(result.getGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage().size(), is(2));
		TemporalCoverage firstCoverage = (TemporalCoverage) result.getGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage().get(0);
		assertThat(firstCoverage.getSingleDateTime().get(0).getCalendarDate(), is(calendarDate1));
		TemporalCoverage secondCoverage = (TemporalCoverage) result.getGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage().get(1);
		assertThat(secondCoverage.getSingleDateTime().get(0).getCalendarDate(), is(calendarDate2));
	}
	
	/**
	 * Can we build with a 'references' element?
	 */
	@Test
	public void testReferences01() {
		String references = "some ref";
		Coverage result = new Coverage()
			.withReferences(new References().withValue(references));
		assertThat(result.getReferences().getValue(), is(references));
	}
}
