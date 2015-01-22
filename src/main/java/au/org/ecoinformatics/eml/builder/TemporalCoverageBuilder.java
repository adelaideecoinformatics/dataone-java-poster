package au.org.ecoinformatics.eml.builder;

import java.util.ArrayList;
import java.util.List;

import au.org.ecoinformatics.eml.jaxb.Coverage.TemporalCoverage;
import au.org.ecoinformatics.eml.jaxb.SingleDateTimeType;
import au.org.ecoinformatics.eml.jaxb.TemporalCoverage.RangeOfDates;
import au.org.ecoinformatics.eml.jaxb.ViewType.References;

public class TemporalCoverageBuilder {

	private List<SingleDateTimeType> singleDateTimes = new ArrayList<SingleDateTimeType>();
	private RangeOfDates rangeOfDates;
	private References references;

	public TemporalCoverageBuilder(ReferencesBuilder referencesBuilder) {
		this.references = referencesBuilder.build();
	}
	
	//TODO support 'system' attribute
	//TODO support 'scope' attribute

	public TemporalCoverageBuilder(SingleDateTimeTypeBuilder singleDateTimeTypeBuilder,
			SingleDateTimeTypeBuilder...moreSingleDateTimeTypeBuilders) {
		this.singleDateTimes.add(singleDateTimeTypeBuilder.build());
		for (SingleDateTimeTypeBuilder currSingleDateTimeBuilder : moreSingleDateTimeTypeBuilders) {
			this.singleDateTimes.add(currSingleDateTimeBuilder.build());
		}
	}

	public TemporalCoverageBuilder(RangeOfDatesBuilder rangeOfDatesBuilder) {
		this.rangeOfDates = rangeOfDatesBuilder.build();
	}

	public TemporalCoverage build() {
		TemporalCoverage result = new TemporalCoverage();
		if (isSupplied(references)) {
			result.setReferences(references);
		}
		if (!singleDateTimes.isEmpty()) {
			result.getSingleDateTime().addAll(singleDateTimes);
		}
		if (isSupplied(rangeOfDates)) {
			result.setRangeOfDates(rangeOfDates);
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
