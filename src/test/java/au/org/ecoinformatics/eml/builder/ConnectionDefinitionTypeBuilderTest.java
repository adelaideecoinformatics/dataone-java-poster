package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.ConnectionDefinitionType;

public class ConnectionDefinitionTypeBuilderTest {

	/**
	 * Can we build with a schema name?
	 */
	@Test
	public void testSchemaName01() {
		String schemaName = "some schema";
		ConnectionDefinitionTypeBuilder objectUnderTest = new ConnectionDefinitionTypeBuilder(schemaName);
		ConnectionDefinitionType result = objectUnderTest.build();
		assertThat(result.getSchemeName().getValue(), is(schemaName));
	}
}
