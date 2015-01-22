package au.org.ecoinformatics.eml.builder;

import java.util.ArrayList;
import java.util.List;

import au.org.ecoinformatics.eml.jaxb.Coverage;
import au.org.ecoinformatics.eml.jaxb.Coverage.TemporalCoverage;
import au.org.ecoinformatics.eml.jaxb.GeographicCoverage;
import au.org.ecoinformatics.eml.jaxb.ViewType.References;

public class CoverageBuilder {
	
	private List<GeographicCoverage> geographicCoverages = new ArrayList<GeographicCoverage>();
	private List<TemporalCoverage> temporalCoverages = new ArrayList<TemporalCoverage>();
	private References references;

	/**
	 * Having this constructor isn't ideal but there needs to be a way to pass
	 * an arbitrary number of geographic, temporal or taxonomic coverages.
	 */
	public CoverageBuilder() { }
	
	public CoverageBuilder(ReferencesBuilder referencesBuilder) {
		this.references = referencesBuilder.build();
	}

	public CoverageBuilder geographicCoverage(GeographicCoverageBuilder geographicCoverageBuilder) {
		this.geographicCoverages.add(geographicCoverageBuilder.build());
		return this;
	}
	
	public CoverageBuilder temporalCoverage(TemporalCoverageBuilder temporalCoverageBuilder) {
		this.temporalCoverages.add(temporalCoverageBuilder.build());
		return this;
	}
	
	// TODO support a 'taxonomicCoverage' element

	public Coverage build() {
		Coverage result = new Coverage();
		result.getGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage().addAll(geographicCoverages);
		result.getGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage().addAll(temporalCoverages);
		if (isSupplied(references)) {
			result.setReferences(references);
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
