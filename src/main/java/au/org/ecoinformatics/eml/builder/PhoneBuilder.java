package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.ResponsibleParty.Phone;

public class PhoneBuilder {

	private String phoneNumber;
	private PhoneType phoneType;

	public PhoneBuilder phoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public PhoneBuilder phoneType(PhoneType phoneType) {
		this.phoneType = phoneType;
		return this;
	}

	public Phone build() {
		Phone result = new Phone();
		if (isSupplied(phoneNumber)) {
			result.setValue(phoneNumber);
		}
		if (isSupplied(phoneType)) {
			result.setPhonetype(phoneType.name());
		}
		return result;
	}

	private boolean isSupplied(Object valueInQuestion) {
		return valueInQuestion != null;
	}
}
