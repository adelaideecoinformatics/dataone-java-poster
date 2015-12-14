package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.ConnectionDefinitionType;
import au.org.ecoinformatics.eml.jaxb.eml.ConnectionDefinitionType.SchemeName;
import au.org.ecoinformatics.eml.jaxb.eml.ConnectionType;
import au.org.ecoinformatics.eml.jaxb.eml.ViewType.References;

public class ConnectionTypeTest {

	/**
	 * Can we build with a ConnectionDefinitionType?
	 */
	@Test
	public void testConnectionDefinitionType01() {
		ConnectionType result = new ConnectionType()
			.withConnectionDefinition(new ConnectionDefinitionType().withSchemeName(new SchemeName().withValue("someSchema")));
		assertThat(result.getConnectionDefinition().getSchemeName().getValue(), is("someSchema"));
	}

	/**
	 * Can we build with references?
	 */
	@Test
	public void testReferences01() {
		String referencesText = "some ref";
		ConnectionType result = new ConnectionType()
			.withReferences(new References().withValue(referencesText));
		assertThat(result.getReferences().getValue(), is(referencesText));
	}
}
