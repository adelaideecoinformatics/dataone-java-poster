package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.TemporalCoverage.RangeOfDates;

public class RangeOfDatesBuilderTest {

	/**
	 * Can we build with the mandatory elements?
	 */
	@Test
	public void testBuild01() {
		String beginDate = "2011-01-11";
		String endDate = "2022-02-22";
		RangeOfDatesBuilder objectUnderTest = new RangeOfDatesBuilder(
				new SingleDateTimeTypeBuilder(beginDate), new SingleDateTimeTypeBuilder(endDate));
		RangeOfDates result = objectUnderTest.build();
		assertThat(result.getBeginDate().getCalendarDate(), is(beginDate));
		assertThat(result.getEndDate().getCalendarDate(), is(endDate));
	}
}
