package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.FunctionType;
import au.org.ecoinformatics.eml.jaxb.UrlType;

public class UrlTypeBuilder {

	private String anyUri;
	private FunctionType function;

	public UrlTypeBuilder(String anyUri) {
		this.anyUri = anyUri;
		// TODO verify that the argument conforms to xs:anyURI
	}
	
	public UrlTypeBuilder function(FunctionType functionType) {
		this.function = functionType;
		return this;
	}

	public UrlType build() {
		UrlType result = new UrlType();
		if (isSupplied(anyUri)) {
			result.setValue(anyUri);
		}
		if (isSupplied(function)) {
			result.setFunction(function);
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
