package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.SingleDateTimeType.AlternativeTimeScale;

public class AlternativeTimeScaleBuilder {

	private String timeScaleName;
	private String timeScaleAgeEstimate;
	private String timeScaleAgeUncertainty;
	private String timeScaleAgeExplanation;

	public AlternativeTimeScaleBuilder(String timeScaleName, String timeScaleAgeEstimate) {
		this.timeScaleName = timeScaleName;
		this.timeScaleAgeEstimate = timeScaleAgeEstimate;
	}
	
	public AlternativeTimeScaleBuilder timeScaleAgeUncertainty(String timeScaleAgeUncertainty) {
		this.timeScaleAgeUncertainty = timeScaleAgeUncertainty;
		return this;
	}
	
	public AlternativeTimeScaleBuilder timeScaleAgeExplanation(String timeScaleAgeExplanation) {
		this.timeScaleAgeExplanation = timeScaleAgeExplanation;
		return this;
	}

	// TODO support the 'timeScaleCitation' element of type CitationType
	
	public AlternativeTimeScale build() {
		AlternativeTimeScale result = new AlternativeTimeScale();
		result.setTimeScaleName(timeScaleName);
		result.setTimeScaleAgeEstimate(timeScaleAgeEstimate);
		if (isSupplied(timeScaleAgeUncertainty)) {
			result.setTimeScaleAgeUncertainty(timeScaleAgeUncertainty);
		}
		if (isSupplied(timeScaleAgeExplanation)) {
			result.setTimeScaleAgeExplanation(timeScaleAgeExplanation);
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
