package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.SingleDateTimeType.AlternativeTimeScale;

public class AlternativeTimeScaleTest {

	/**
	 * Can we build with a 'time scale age uncertainty' element??
	 */
	@Test
	public void testTimeScaleAgeUncertainty01() {
		String timeScaleAgeUncertainty = "I guessed it";
		AlternativeTimeScale result = new AlternativeTimeScale()
			.withTimeScaleAgeUncertainty(timeScaleAgeUncertainty);
		assertThat(result.getTimeScaleAgeUncertainty(), is(timeScaleAgeUncertainty));
	}
	
	/**
	 * Can we build with a 'time scale age explanation' element??
	 */
	@Test
	public void testTimeScaleAgeExplanation01() {
		String timeScaleAgeExplanation = "just because";
		AlternativeTimeScale result = new AlternativeTimeScale()
			.withTimeScaleAgeExplanation(timeScaleAgeExplanation);
		assertThat(result.getTimeScaleAgeExplanation(), is(timeScaleAgeExplanation));
	}
	
	/**
	 * Are optional item empty when we don't supply them?
	 */
	@Test
	public void testBuild01() {
		AlternativeTimeScale result = new AlternativeTimeScale();
		assertNull(result.getTimeScaleName());
		assertNull(result.getTimeScaleAgeEstimate());
		assertNull(result.getTimeScaleAgeUncertainty());
		assertNull(result.getTimeScaleAgeExplanation());
		assertThat(result.getTimeScaleCitation().size(), is(0));
	}
}
