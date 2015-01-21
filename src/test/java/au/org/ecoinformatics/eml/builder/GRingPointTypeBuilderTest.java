package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.GRingPointType;

public class GRingPointTypeBuilderTest {

	/**
	 * Can we build with the bare miminum mandatory elements?
	 */
	@Test
	public void testLatLong01() {
		int gRingLatitude = 11;
		int gRingLongitude = 22;
		GRingPointTypeBuilder objectUnderTest = new GRingPointTypeBuilder(gRingLatitude, gRingLongitude);
		GRingPointType result = objectUnderTest.build();
		assertThat(result.getGRingLatitude().intValue(), is(11));
		assertThat(result.getGRingLongitude().intValue(), is(22));
	}
}
