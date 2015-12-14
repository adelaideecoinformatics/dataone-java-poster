package au.org.aekos.sysmetagen.service;

public class SysMetaFragments {

	private final String identifier;
	private final String contact;
	private final String formatId;
	
	public SysMetaFragments(String identifier, String contact, String formatId) {
		this.identifier = identifier;
		this.contact = contact;
		this.formatId = formatId;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getContact() {
		return contact;
	}

	public String getFormatId() {
		return formatId;
	}
}
