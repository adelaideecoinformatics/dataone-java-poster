package au.org.aekos.sysmetagen.service;



public class GmdNamespaceUnawareXmlParser extends AbstractXmlParser implements XmlParser {

	private static final String IDENTIFIER_XPATH = "/MD_Metadata/fileIdentifier/CharacterString";
	private static final String CONTACT_XPATH = "/MD_Metadata/contact/CI_ResponsibleParty/organisationName/CharacterString";
	
	public GmdNamespaceUnawareXmlParser() {
		super(IDENTIFIER_XPATH, CONTACT_XPATH);
	}
	
	@Override
	String getPrettyFormatName() {
		return "GMD";
	}
}
