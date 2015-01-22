package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.Coverage.TemporalCoverage;


public class TemporalCoverageBuilderTest {

	/**
	 * Can we build with a 'references' element?
	 */
	@Test
	public void testReferences01() {
		ReferencesBuilder referencesBuilder = new ReferencesBuilder("some ref stuff");
		TemporalCoverageBuilder objectUnderTest = new TemporalCoverageBuilder(referencesBuilder);
		TemporalCoverage result = objectUnderTest.build();
		assertThat(result.getReferences().getValue(), is("some ref stuff"));
		assertThat(result.getSingleDateTime().size(), is(0));
	}
	
	/**
	 * Can we build with one 'SingleDateTime' element?
	 */
	@Test
	public void testSingleDateTime01() {
		String calendarDate = "2011-01-11";
		TemporalCoverageBuilder objectUnderTest = new TemporalCoverageBuilder(
				new SingleDateTimeTypeBuilder(calendarDate));
		TemporalCoverage result = objectUnderTest.build();
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
		TemporalCoverageBuilder objectUnderTest = new TemporalCoverageBuilder(
				new SingleDateTimeTypeBuilder(calendarDate1),
				new SingleDateTimeTypeBuilder(calendarDate2));
		TemporalCoverage result = objectUnderTest.build();
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
		TemporalCoverageBuilder objectUnderTest = new TemporalCoverageBuilder(
				new RangeOfDatesBuilder(new SingleDateTimeTypeBuilder(beginDate), new SingleDateTimeTypeBuilder(endDate)));
		TemporalCoverage result = objectUnderTest.build();
		assertThat(result.getRangeOfDates().getBeginDate().getCalendarDate(), is(beginDate));
		assertThat(result.getRangeOfDates().getEndDate().getCalendarDate(), is(endDate));
	}
}
