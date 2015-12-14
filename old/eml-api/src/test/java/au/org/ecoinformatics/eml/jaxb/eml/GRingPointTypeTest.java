package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.GRingPointType;

public class GRingPointTypeTest {

	/**
	 * Can we build with the bare miminum mandatory elements?
	 */
	@Test
	public void testLatLong01() {
		int gRingLatitude = 11;
		int gRingLongitude = 22;
		GRingPointType result = new GRingPointType()
			.withGRingLatitude(new BigDecimal(gRingLatitude))
			.withGRingLongitude(new BigDecimal(gRingLongitude));
		assertThat(result.getGRingLatitude().intValue(), is(11));
		assertThat(result.getGRingLongitude().intValue(), is(22));
	}
}
