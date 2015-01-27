package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.ConnectionDefinitionType;
import au.org.ecoinformatics.eml.jaxb.eml.ConnectionDefinitionType.SchemeName;

public class ConnectionDefinitionTypeTest {

	/**
	 * Can we build with a schema name?
	 */
	@Test
	public void testSchemaName01() {
		String schemaName = "some schema";
		ConnectionDefinitionType result = new ConnectionDefinitionType()
			.withSchemeName(new SchemeName().withValue(schemaName));
		assertThat(result.getSchemeName().getValue(), is(schemaName));
	}
}
