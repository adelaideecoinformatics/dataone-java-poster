package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.ResponsibleParty.UserId;

public class UserIdTest {

	/**
	 * Can we build with the mandatory user ID?
	 */
	@Test
	public void testBuild01() {
		UserId result = new UserId().withValue("user1");
		assertThat(result.getValue(), is("user1"));
	}
}
