package au.org.aekos.sysmetagen.service;




public class EmlNamespaceUnawareXmlParser extends AbstractXmlParser implements XmlParser {

	private static final String IDENTIFIER_XPATH = "/eml/@packageId";
	private static final String CONTACT_XPATH = "/eml/dataset/creator/organizationName";
	
	public EmlNamespaceUnawareXmlParser() {
		super(IDENTIFIER_XPATH, CONTACT_XPATH);
	}
	
	@Override
	String getPrettyFormatName() {
		return "EML";
	}
}
