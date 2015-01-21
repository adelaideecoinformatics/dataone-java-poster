package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.ViewType.References;

public class ReferencesBuilderTest {

	/**
	 * Can we build with a references element?
	 */
	@Test
	public void testReference01() {
		String references = "some reference text";
		ReferencesBuilder objectUnderTest = new ReferencesBuilder(references);
		References result = objectUnderTest.build();
		assertThat(result.getValue(), is(references));
	}
	
	/**
	 * Can we build with a 'system' attribute?
	 */
	@Test
	public void testSystem01() {
		ReferencesBuilder objectUnderTest = new ReferencesBuilder("some reference text");
		String system = "some sys";
		References result = objectUnderTest.system(system).build();
		assertThat(result.getSystem().get(0), is(system));
	}
	
	/**
	 * Are all the optional items not present when we don't supply them?
	 */
	@Test
	public void testBuild01() {
		ReferencesBuilder objectUnderTest = new ReferencesBuilder("some reference text");
		References result = objectUnderTest.build();
		assertThat(result.getSystem().size(), is(0));
	}
}
