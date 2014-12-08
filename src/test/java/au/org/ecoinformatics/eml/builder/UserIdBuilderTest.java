package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.ResponsibleParty.UserId;

public class UserIdBuilderTest {

	/**
	 * Can we build with the mandatory user ID?
	 */
	@Test
	public void testBuild01() {
		UserIdBuilder objectUnderTest = new UserIdBuilder("user1");
		UserId result = objectUnderTest.build();
		assertThat(result.getValue(), is("user1"));
	}
}
