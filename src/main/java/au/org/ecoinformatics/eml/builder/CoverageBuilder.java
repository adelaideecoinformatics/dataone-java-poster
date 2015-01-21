package au.org.ecoinformatics.eml.builder;

import java.util.ArrayList;
import java.util.List;

import au.org.ecoinformatics.eml.jaxb.Coverage;
import au.org.ecoinformatics.eml.jaxb.GeographicCoverage;

public class CoverageBuilder {
	
	private List<GeographicCoverage> geographicCoverages = new ArrayList<GeographicCoverage>();

	public CoverageBuilder(GeographicCoverageBuilder...geographicCoverageBuilders) {
		for (GeographicCoverageBuilder currGeographicCoverageBuilder : geographicCoverageBuilders) {
			this.geographicCoverages.add(currGeographicCoverageBuilder.build());
		}
	}
	
	// TODO support a 'references' element
	
	// TODO support a 'temporalCoverage' element
	
	// TODO support a 'taxonomicCoverage' element

	public Coverage build() {
		Coverage result = new Coverage();
		result.getGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage().addAll(geographicCoverages);
		return result;
	}
}
