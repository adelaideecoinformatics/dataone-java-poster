package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.ViewType.References;

public class ReferencesBuilder {

	private String references;
	private String system;

	public ReferencesBuilder(String references) {
		this.references = references;
	}
	
	public ReferencesBuilder system(String system) {
		this.system = system;
		return this;
	}

	public References build() {
		References result = new References();
		if (isSupplied(references)) {
			result.setValue(references);
		}
		if (isSupplied(system)) {
			result.getSystem().add(system);
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
