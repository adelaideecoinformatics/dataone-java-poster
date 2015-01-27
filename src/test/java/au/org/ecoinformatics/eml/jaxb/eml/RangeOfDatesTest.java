package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.SingleDateTimeType;
import au.org.ecoinformatics.eml.jaxb.eml.TemporalCoverage.RangeOfDates;


public class RangeOfDatesTest {

	/**
	 * Can we build with the mandatory elements?
	 */
	@Test
	public void testBuild01() {
		String beginDate = "2011-01-11";
		String endDate = "2022-02-22";
		RangeOfDates result = new RangeOfDates()
			.withBeginDate(new SingleDateTimeType().withCalendarDate(beginDate))
			.withEndDate(new SingleDateTimeType().withCalendarDate(endDate));
		assertThat(result.getBeginDate().getCalendarDate(), is(beginDate));
		assertThat(result.getEndDate().getCalendarDate(), is(endDate));
	}
}
