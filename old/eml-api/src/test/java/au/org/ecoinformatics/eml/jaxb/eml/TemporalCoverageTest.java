package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.Coverage.TemporalCoverage;
import au.org.ecoinformatics.eml.jaxb.eml.SingleDateTimeType;
import au.org.ecoinformatics.eml.jaxb.eml.TemporalCoverage.RangeOfDates;
import au.org.ecoinformatics.eml.jaxb.eml.ViewType.References;


public class TemporalCoverageTest {

	/**
	 * Can we build with a 'references' element?
	 */
	@Test
	public void testReferences01() {
		TemporalCoverage result = new TemporalCoverage()
			.withReferences(new References().withValue("some ref stuff"));
		assertThat(result.getReferences().getValue(), is("some ref stuff"));
		assertThat(result.getSingleDateTime().size(), is(0));
	}
	
	/**
	 * Can we build with one 'SingleDateTime' element?
	 */
	@Test
	public void testSingleDateTime01() {
		String calendarDate = "2011-01-11";
		TemporalCoverage result = new TemporalCoverage()
			.withSingleDateTime(new SingleDateTimeType().withCalendarDate(calendarDate));
		assertThat(result.getSingleDateTime().size(), is(1));
		assertThat(result.getSingleDateTime().get(0).getCalendarDate(), is(calendarDate));
	}
	
	/**
	 * Can we build with two 'SingleDateTime' elements?
	 */
	@Test
	public void testSingleDateTime02() {
		String calendarDate1 = "2011-01-11";
		String calendarDate2 = "2022-02-22";
		TemporalCoverage result = new TemporalCoverage()
			.withSingleDateTime(new SingleDateTimeType().withCalendarDate(calendarDate1))
			.withSingleDateTime(new SingleDateTimeType().withCalendarDate(calendarDate2));
		assertThat(result.getSingleDateTime().size(), is(2));
		assertThat(result.getSingleDateTime().get(0).getCalendarDate(), is(calendarDate1));
		assertThat(result.getSingleDateTime().get(1).getCalendarDate(), is(calendarDate2));
	}
	
	/**
	 * Can we build with a 'range of dates' element?
	 */
	@Test
	public void testRangeOfDates01() {
		String beginDate = "2011-01-11";
		String endDate = "2022-02-22";
		TemporalCoverage result = new TemporalCoverage()
			.withRangeOfDates(
					new RangeOfDates()
						.withBeginDate(new SingleDateTimeType().withCalendarDate(beginDate))
						.withEndDate(new SingleDateTimeType().withCalendarDate(endDate)));
		assertThat(result.getRangeOfDates().getBeginDate().getCalendarDate(), is(beginDate));
		assertThat(result.getRangeOfDates().getEndDate().getCalendarDate(), is(endDate));
	}
}
