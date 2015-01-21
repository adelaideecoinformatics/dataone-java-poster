package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.Coverage;
import au.org.ecoinformatics.eml.jaxb.GeographicCoverage;

public class CoverageBuilderTest {

	/**
	 * Can we build with two 'geographic coverage' elements?
	 */
	@Test
	public void testGeographicCoverage01() {
		String referencesText1 = "some ref";
		String referencesText2 = "other ref";
		CoverageBuilder objectUnderTest = new CoverageBuilder(
				new GeographicCoverageBuilder(new ReferencesBuilder(referencesText1)),
				new GeographicCoverageBuilder(new ReferencesBuilder(referencesText2)));
		Coverage result = objectUnderTest.build();
		assertThat(result.getGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage().size(), is(2));
		GeographicCoverage firstCoverage = (GeographicCoverage) result.getGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage().get(0);
		assertThat(firstCoverage.getReferences().getValue(), is(referencesText1));
		GeographicCoverage secondCoverage = (GeographicCoverage) result.getGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage().get(1);
		assertThat(secondCoverage.getReferences().getValue(), is(referencesText2));
	}
}
