package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.ResponsibleParty.UserId;

public class UserIdBuilder {

	private String id;

	public UserIdBuilder(String id) {
		this.id = id;
	}

	public UserId build() {
		UserId result = new UserId();
		if (isSupplied(id)) {
			result.setValue(id);
		}
		return result;
	}

	private boolean isSupplied(String obj) {
		return obj != null;
	}
}
