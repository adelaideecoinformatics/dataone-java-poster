package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.SingleDateTimeType.AlternativeTimeScale;

public class SingleDateTimeTypeTest {

	/**
	 * Can we build with a 'calendar date' element?
	 */
	@Test
	public void testCalendarDate01() {
		String calendarDate = "2014-09-12";
		SingleDateTimeType result = new SingleDateTimeType().withCalendarDate(calendarDate);
		assertThat(result.getCalendarDate(), is(calendarDate));
	}
	
	/**
	 * Can we build with a 'time' element?
	 */
	@Test
	public void testTime01() throws DatatypeConfigurationException {
		int hour = 9;
		int minute = 30;
		int second = 54;
		XMLGregorianCalendar calendar = newCalendar();
		calendar.setHour(hour);
		calendar.setMinute(minute);
		calendar.setSecond(second);
		SingleDateTimeType result = new SingleDateTimeType().withTime(calendar);
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
		AlternativeTimeScale alternativeTimeScale = new AlternativeTimeScale()
			.withTimeScaleName(timeScaleName)
			.withTimeScaleAgeEstimate(timeScaleAgeEstimate);
		SingleDateTimeType result = new SingleDateTimeType().withAlternativeTimeScale(alternativeTimeScale);
		assertThat(result.getAlternativeTimeScale().getTimeScaleName(), is(timeScaleName));
		assertThat(result.getAlternativeTimeScale().getTimeScaleAgeEstimate(), is(timeScaleAgeEstimate));
	}
	
	/**
	 * Are optional items empty when we build without them?
	 */
	@Test
	public void testBuild01() {
		SingleDateTimeType result = new SingleDateTimeType();
		assertNull(result.getAlternativeTimeScale());
		assertNull(result.getTime());
		assertNull(result.getCalendarDate());
	}
	
	private XMLGregorianCalendar newCalendar() throws DatatypeConfigurationException {
		return DatatypeFactory.newInstance().newXMLGregorianCalendar();
	}
}
