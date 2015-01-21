package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.ConnectionType;

public class ConnectionTypeBuilderTest {

	/**
	 * Can we build with a ConnectionDefinitionType?
	 */
	@Test
	public void testConnectionDefinitionType01() {
		ConnectionDefinitionTypeBuilder connectionDefinitionTypeBuilder = new ConnectionDefinitionTypeBuilder("someSchema");
		ConnectionTypeBuilder objectUnderTest = new ConnectionTypeBuilder(connectionDefinitionTypeBuilder);
		ConnectionType result = objectUnderTest.build();
		assertThat(result.getConnectionDefinition().getSchemeName().getValue(), is("someSchema"));
	}

	/**
	 * Can we build with references?
	 */
	@Test
	public void testReferences01() {
		String referencesText = "some ref";
		ReferencesBuilder referencesBuilder = new ReferencesBuilder(referencesText);
		ConnectionTypeBuilder objectUnderTest = new ConnectionTypeBuilder(referencesBuilder);
		ConnectionType result = objectUnderTest.build();
		assertThat(result.getReferences().getValue(), is(referencesText));
	}
}
