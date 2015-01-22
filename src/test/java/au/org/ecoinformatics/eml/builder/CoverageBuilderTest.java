package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.Coverage;
import au.org.ecoinformatics.eml.jaxb.Coverage.TemporalCoverage;
import au.org.ecoinformatics.eml.jaxb.GeographicCoverage;

public class CoverageBuilderTest {

	/**
	 * Can we build with two 'geographic coverage' elements?
	 */
	@Test
	public void testGeographicCoverage01() {
		String referencesText1 = "some ref";
		String referencesText2 = "other ref";
		CoverageBuilder objectUnderTest = new CoverageBuilder();
		Coverage result = objectUnderTest
				.geographicCoverage(new GeographicCoverageBuilder(new ReferencesBuilder(referencesText1)))
				.geographicCoverage(new GeographicCoverageBuilder(new ReferencesBuilder(referencesText2)))
				.build();
		assertThat(result.getGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage().size(), is(2));
		GeographicCoverage firstCoverage = (GeographicCoverage) result.getGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage().get(0);
		assertThat(firstCoverage.getReferences().getValue(), is(referencesText1));
		GeographicCoverage secondCoverage = (GeographicCoverage) result.getGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage().get(1);
		assertThat(secondCoverage.getReferences().getValue(), is(referencesText2));
	}
	
	/**
	 * Can we build with two 'temporal coverage' elements?
	 */
	@Test
	public void testTemporalCoverage01() {
		String calendarDate1 = "2011-11-11";
		String calendarDate2 = "2022-02-22";
		CoverageBuilder objectUnderTest = new CoverageBuilder();
		Coverage result = objectUnderTest
				.temporalCoverage(new TemporalCoverageBuilder(new SingleDateTimeTypeBuilder(calendarDate1)))
				.temporalCoverage(new TemporalCoverageBuilder(new SingleDateTimeTypeBuilder(calendarDate2)))
				.build();
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
		CoverageBuilder objectUnderTest = new CoverageBuilder(new ReferencesBuilder(references));
		Coverage result = objectUnderTest.build();
		assertThat(result.getReferences().getValue(), is(references));
	}
}
