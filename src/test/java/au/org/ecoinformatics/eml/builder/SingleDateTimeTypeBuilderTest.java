package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.SingleDateTimeType;

public class SingleDateTimeTypeBuilderTest {

	/**
	 * Can we build with a 'calendar date' element?
	 */
	@Test
	public void testCalendarDate01() {
		String calendarDate = "2014-09-12";
		SingleDateTimeTypeBuilder objectUnderTest = new SingleDateTimeTypeBuilder(calendarDate);
		SingleDateTimeType result = objectUnderTest.build();
		assertThat(result.getCalendarDate(), is(calendarDate));
	}
	
	/**
	 * Can we build with a 'time' element?
	 */
	@Test
	public void testTime01() {
		SingleDateTimeTypeBuilder objectUnderTest = new SingleDateTimeTypeBuilder("2014-09-12");
		int hour = 9;
		int minute = 30;
		int second = 54;
		SingleDateTimeType result = objectUnderTest
				.time(hour,minute,second)
				.build();
		assertThat(result.getTime().getHour(), is(hour));
		assertThat(result.getTime().getMinute(), is(minute));
		assertThat(result.getTime().getSecond(), is(second));
	}
	
	/**
	 * Can we build with an 'alternative time scale' element?
	 */
	@Test
	public void testAlternativeTimeScale01() {
		String timeScaleName = "some scale";
		String timeScaleAgeEstimate = "really old";
		AlternativeTimeScaleBuilder alternativeTimeScaleBuilder = new AlternativeTimeScaleBuilder(timeScaleName, timeScaleAgeEstimate);
		SingleDateTimeTypeBuilder objectUnderTest = new SingleDateTimeTypeBuilder(alternativeTimeScaleBuilder);
		SingleDateTimeType result = objectUnderTest.build();
		assertThat(result.getAlternativeTimeScale().getTimeScaleName(), is(timeScaleName));
		assertThat(result.getAlternativeTimeScale().getTimeScaleAgeEstimate(), is(timeScaleAgeEstimate));
	}
	
	/**
	 * Are optional items empty when we build without them?
	 */
	@Test
	public void testBuild01() {
		SingleDateTimeTypeBuilder objectUnderTest = new SingleDateTimeTypeBuilder("2014-09-12");
		SingleDateTimeType result = objectUnderTest.build();
		assertNull(result.getAlternativeTimeScale());
		assertNull(result.getTime());
	}
	
	/**
	 * Are optional items empty when we build without them?
	 */
	@Test
	public void testBuild02() {
		SingleDateTimeTypeBuilder objectUnderTest = new SingleDateTimeTypeBuilder(
				new AlternativeTimeScaleBuilder("some scale", "really old"));
		SingleDateTimeType result = objectUnderTest.build();
		assertNull(result.getCalendarDate());
	}
}
