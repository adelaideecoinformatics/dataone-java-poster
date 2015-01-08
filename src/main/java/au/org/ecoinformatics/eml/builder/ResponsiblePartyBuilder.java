package au.org.ecoinformatics.eml.builder;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import au.org.ecoinformatics.eml.jaxb.Address;
import au.org.ecoinformatics.eml.jaxb.I18NNonEmptyStringType;
import au.org.ecoinformatics.eml.jaxb.Person;
import au.org.ecoinformatics.eml.jaxb.ResponsibleParty;
import au.org.ecoinformatics.eml.jaxb.ResponsibleParty.Phone;
import au.org.ecoinformatics.eml.jaxb.ResponsibleParty.UserId;

public class ResponsiblePartyBuilder {

	private Person individualName;
	private String orgName;
	private String positionName;
	private Address address;
	private Phone phone;
	private String emailAddress;
	private List<String> onlineUrls = new LinkedList<String>();
	private List<UserId> userIds = new LinkedList<UserId>();

	public ResponsiblePartyBuilder individualName(PersonBuilder personBuilder) {
		//FIXME support maxOccurs="unbounded"
		this.individualName = personBuilder.build();
		return this;
	}

	public ResponsiblePartyBuilder organizationName(String organizationName) {
		//FIXME support maxOccurs="unbounded"
		this.orgName = organizationName;
		return this;
	}

	public ResponsiblePartyBuilder positionName(String positionName) {
		//FIXME support maxOccurs="unbounded"
		this.positionName = positionName;
		return this;
	}

	public ResponsiblePartyBuilder address(AddressBuilder addressBuilder) {
		//FIXME support maxOccurs="unbounded"
		this.address = addressBuilder.build();
		return this;
	}

	public ResponsiblePartyBuilder phone(PhoneBuilder phoneBuilder) {
		//FIXME support maxOccurs="unbounded"
		this.phone = phoneBuilder.build();
		return this;
	}

	public ResponsiblePartyBuilder electronicMailAddress(String emailAddress) {
		//FIXME support maxOccurs="unbounded"
		this.emailAddress = emailAddress;
		return this;
	}

	public ResponsiblePartyBuilder addOnlineUrl(String onlineUrl) {
		this.onlineUrls.add(onlineUrl);
		return this;
	}

	public ResponsiblePartyBuilder addUserId(UserIdBuilder userIdBuilder) {
		userIds.add(userIdBuilder.build());
		return this;
	}

	public ResponsibleParty build() {
		ResponsibleParty result = new ResponsibleParty();
		if (isSupplied(individualName)) {
			JAXBElement<?> e = new JAXBElement<Person>(new QName("person"), Person.class, null, individualName);
			result.getIndividualNameOrOrganizationNameOrPositionName().add(e);
		}
		if (isSupplied(orgName)) {
			I18NNonEmptyStringType jaxbOrgName = new I18NNonEmptyStringTypeBuilder(orgName).build();
			JAXBElement<?> e = new JAXBElement<I18NNonEmptyStringType>(new QName("organizationName"), I18NNonEmptyStringType.class, null, jaxbOrgName);
			result.getIndividualNameOrOrganizationNameOrPositionName().add(e);
		}
		if (isSupplied(positionName)) {
			I18NNonEmptyStringType jaxbPositionName = new I18NNonEmptyStringTypeBuilder(positionName).build();
			JAXBElement<?> e = new JAXBElement<I18NNonEmptyStringType>(new QName("positionName"), I18NNonEmptyStringType.class, null, jaxbPositionName);
			result.getIndividualNameOrOrganizationNameOrPositionName().add(e);
		}
		if (isSupplied(address)) {
			result.getAddress().add(address);
		}
		if (isSupplied(phone)) {
			result.getPhone().add(phone);
		}
		if (isSupplied(emailAddress)) {
			I18NNonEmptyStringType e = new I18NNonEmptyStringTypeBuilder(emailAddress).build();
			result.getElectronicMailAddress().add(e);
		}
		if (!onlineUrls.isEmpty()) {
			for (String currOnlineUrl : onlineUrls) {
				result.getOnlineUrl().add(currOnlineUrl);
			}
		}
		if (!userIds.isEmpty()) {
			for (UserId currUserId : userIds) {
				result.getUserId().add(currUserId);
			}
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}
