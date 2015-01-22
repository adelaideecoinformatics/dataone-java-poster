package au.org.ecoinformatics.eml.builder;

import javax.xml.datatype.XMLGregorianCalendar;

import au.org.ecoinformatics.eml.jaxb.SingleDateTimeType;
import au.org.ecoinformatics.eml.jaxb.SingleDateTimeType.AlternativeTimeScale;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class SingleDateTimeTypeBuilder {

	private String calendarDate;
	private boolean isTimeSupplied = false;
	private int hour;
	private int minute;
	private int second;
	private AlternativeTimeScale alternativeTimeScale;

	public SingleDateTimeTypeBuilder(String calendarDate) {
		this.calendarDate = calendarDate;
	}
	
	public SingleDateTimeTypeBuilder(AlternativeTimeScaleBuilder alternativeTimeScaleBuilder) {
		this.alternativeTimeScale = alternativeTimeScaleBuilder.build();
	}

	public SingleDateTimeTypeBuilder time(int hour, int minute, int second) {
		// FIXME this could be changed to take an XMLGregorianCalendarBuilder to keep the pattern
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.isTimeSupplied = true;
		return this;
	}

	public SingleDateTimeType build() {
		SingleDateTimeType result = new SingleDateTimeType();
		result.setCalendarDate(calendarDate);
		if (isTimeSupplied) {
			XMLGregorianCalendar value = new XMLGregorianCalendarImpl();
			value.setHour(hour);
			value.setMinute(minute);
			value.setSecond(second);
			result.setTime(value);
		}
		if (isSupplied(alternativeTimeScale)) {
			result.setAlternativeTimeScale(alternativeTimeScale);
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
