package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.Address;
import au.org.ecoinformatics.eml.jaxb.I18NNonEmptyStringType;

public class AddressBuilder {

	private String deliveryPoint;
	private String city;
	private String administrativeArea;
	private String postalCode;
	private String country;

	public AddressBuilder deliveryPoint(String deliveryPoint) {
		// TODO support multiple
		this.deliveryPoint = deliveryPoint;
		return this;
	}

	public AddressBuilder city(String city) {
		this.city = city;
		return this;
	}

	public AddressBuilder administrativeArea(String administrativeArea) {
		this.administrativeArea = administrativeArea;
		return this;
	}

	public AddressBuilder postalCode(String postalCode) {
		this.postalCode = postalCode;
		return this;
	}

	public AddressBuilder country(String country) {
		this.country = country;
		return this;
	}

	public Address build() {
		Address result = new Address();
		if (isSupplied(deliveryPoint)) {
			I18NNonEmptyStringType jaxbDeliveryPoint = new I18NNonEmptyStringTypeBuilder(deliveryPoint).build();
			result.getDeliveryPoint().add(jaxbDeliveryPoint);
		}
		if (isSupplied(city)) {
			I18NNonEmptyStringType jaxbCity = new I18NNonEmptyStringTypeBuilder(city).build();
			result.setCity(jaxbCity);
		}
		if (isSupplied(administrativeArea)) {
			I18NNonEmptyStringType jaxbAdministrativeArea = new I18NNonEmptyStringTypeBuilder(administrativeArea).build();
			result.setAdministrativeArea(jaxbAdministrativeArea);
		}
		if (isSupplied(postalCode)) {
			I18NNonEmptyStringType jaxbPostalCode = new I18NNonEmptyStringTypeBuilder(postalCode).build();
			result.setPostalCode(jaxbPostalCode);
		}
		if (isSupplied(country)) {
			I18NNonEmptyStringType jaxbCountry = new I18NNonEmptyStringTypeBuilder(country).build();
			result.setCountry(jaxbCountry);
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
