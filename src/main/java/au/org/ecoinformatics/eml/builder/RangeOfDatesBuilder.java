package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.SingleDateTimeType;
import au.org.ecoinformatics.eml.jaxb.TemporalCoverage.RangeOfDates;

public class RangeOfDatesBuilder {

	private SingleDateTimeType beginDate;
	private SingleDateTimeType endDate;

	public RangeOfDatesBuilder(SingleDateTimeTypeBuilder beginDateBuilder,
			SingleDateTimeTypeBuilder endDateBuilder) {
		this.beginDate = beginDateBuilder.build();
		this.endDate = endDateBuilder.build();
	}

	public RangeOfDates build() {
		RangeOfDates result = new RangeOfDates();
		result.setBeginDate(beginDate);
		result.setEndDate(endDate);
		return result;
	}
}
