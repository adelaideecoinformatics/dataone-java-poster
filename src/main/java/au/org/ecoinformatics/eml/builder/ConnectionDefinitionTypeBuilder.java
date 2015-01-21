package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.ConnectionDefinitionType;
import au.org.ecoinformatics.eml.jaxb.ConnectionDefinitionType.SchemeName;

public class ConnectionDefinitionTypeBuilder {

	private String schemaName;

	public ConnectionDefinitionTypeBuilder(String schemaName) {
		this.schemaName = schemaName;
	}

	// TODO support 'system' attribute on schemaName
	
	// TODO support a 'description' sub-element
	
	/* TODO support a sub-element:
	 *  parameterDefinition: {
	 *    name: ?
	 *    defintiion: ? 
	 *    defaultValue: ?
	 *  }
	 */
	
	public ConnectionDefinitionType build() {
		ConnectionDefinitionType result = new ConnectionDefinitionType();
		if (isSupplied(schemaName)) {
			SchemeName value = new SchemeName();
			value.setValue(schemaName);
			result.setSchemeName(value);
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
