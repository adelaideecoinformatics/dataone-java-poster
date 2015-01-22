package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.SingleDateTimeType.AlternativeTimeScale;

public class AlternativeTimeScaleBuilderTest {

	/**
	 * Can we build with the minimum mandatory elements?
	 */
	@Test
	public void testBuild01() {
		String timeScaleName = "some scale";
		String timeScaleAgeEstimate = "really old";
		AlternativeTimeScaleBuilder objectUnderTest = new AlternativeTimeScaleBuilder(timeScaleName, timeScaleAgeEstimate);
		AlternativeTimeScale result = objectUnderTest.build();
		assertThat(result.getTimeScaleName(), is(timeScaleName));
		assertThat(result.getTimeScaleAgeEstimate(), is(timeScaleAgeEstimate));
	}
	
	/**
	 * Can we build with a 'time scale age uncertainty' element??
	 */
	@Test
	public void testTimeScaleAgeUncertainty01() {
		AlternativeTimeScaleBuilder objectUnderTest = new AlternativeTimeScaleBuilder("some scale", "really old");
		String timeScaleAgeUncertainty = "I guessed it";
		AlternativeTimeScale result = objectUnderTest
				.timeScaleAgeUncertainty(timeScaleAgeUncertainty)
				.build();
		assertThat(result.getTimeScaleAgeUncertainty(), is(timeScaleAgeUncertainty));
	}
	
	/**
	 * Can we build with a 'time scale age explanation' element??
	 */
	@Test
	public void testTimeScaleAgeExplanation01() {
		AlternativeTimeScaleBuilder objectUnderTest = new AlternativeTimeScaleBuilder("some scale", "really old");
		String timeScaleAgeExplanation = "just because";
		AlternativeTimeScale result = objectUnderTest
				.timeScaleAgeExplanation(timeScaleAgeExplanation)
				.build();
		assertThat(result.getTimeScaleAgeExplanation(), is(timeScaleAgeExplanation));
	}
	
	/**
	 * Are optional item empty when we don't supply them?
	 */
	@Test
	public void testBuild02() {
		AlternativeTimeScaleBuilder objectUnderTest = new AlternativeTimeScaleBuilder("some scale", "really old");
		AlternativeTimeScale result = objectUnderTest.build();
		assertNull(result.getTimeScaleAgeUncertainty());
		assertNull(result.getTimeScaleAgeExplanation());
		assertThat(result.getTimeScaleCitation().size(), is(0));
	}
}
