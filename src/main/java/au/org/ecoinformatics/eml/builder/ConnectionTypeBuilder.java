package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.ConnectionDefinitionType;
import au.org.ecoinformatics.eml.jaxb.ConnectionType;
import au.org.ecoinformatics.eml.jaxb.ViewType.References;

public class ConnectionTypeBuilder {

	private ConnectionDefinitionType connectionDefinitionType;
	private References references;

	public ConnectionTypeBuilder(ConnectionDefinitionTypeBuilder connectionDefinitionTypeBuilder) {
		this.connectionDefinitionType = connectionDefinitionTypeBuilder.build();
	}

	public ConnectionTypeBuilder(ReferencesBuilder referencesBuilder) {
		this.references = referencesBuilder.build();
	}

	public ConnectionType build() {
		ConnectionType result = new ConnectionType();
		if (isSupplied(connectionDefinitionType)) {
			result.setConnectionDefinition(connectionDefinitionType);
		}
		if (isSupplied(references)) {
			result.setReferences(references);
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
