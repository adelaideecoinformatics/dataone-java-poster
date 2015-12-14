package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.ViewType;
import au.org.ecoinformatics.eml.jaxb.eml.ViewType.References;

public class ReferencesTest {

	/**
	 * Can we build with a references element?
	 */
	@Test
	public void testReference01() {
		String references = "some reference text";
		References result = new ViewType.References().withValue(references);
		assertThat(result.getValue(), is(references));
	}
	
	/**
	 * Can we build with a 'system' attribute?
	 */
	@Test
	public void testSystem01() {
		String system = "some sys";
		References result = new ViewType.References()
			.withSystem(system);
		assertThat(result.getSystem().get(0), is(system));
	}
	
	/**
	 * Are all the optional items not present when we don't supply them?
	 */
	@Test
	public void testBuild01() {
		References result = new ViewType.References();
		assertThat(result.getSystem().size(), is(0));
		assertNull(result.getValue());
	}
}
