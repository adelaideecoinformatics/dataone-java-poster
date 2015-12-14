package au.org.ecoinformatics.eml;

import org.junit.Test;
import org.w3c.dom.ls.LSInput;

public class EmlInJarLSResourceResolverTest {

	/**
	 * Can we resolve a URL style non-EML based resource?
	 */
	@Test
	public void testResolveResource01() {
		EmlInJarLSResourceResolver objectUnderTest = new EmlInJarLSResourceResolver("some/base/", "subdir");
		String type = "http://www.w3.org/2001/XMLSchema";
		String namespaceURI = "http://www.w3.org/XML/1998/namespace";
		String systemId = "http://www.w3.org/2009/01/xml.xsd";
		String baseURI = "file:///some/path/eml-text.xsd";
		LSInput result = objectUnderTest.resolveResource(type, namespaceURI, null, systemId, baseURI);
		result.getStringData();
	}
	
	/**
	 * Can we resolve a non-URL style non-EML based resource?
	 */
	@Test
	public void testResolveResource02() {
		EmlInJarLSResourceResolver objectUnderTest = new EmlInJarLSResourceResolver("au/org/ecoinformatics/", "eml/");
		String type = "http://www.w3.org/2001/XMLSchema";
		String namespaceURI = "eml://ecoinformatics.org/documentation-2.1.1";
		String systemId = "XMLSchema.dtd";
		String baseURI = null;
		LSInput result = objectUnderTest.resolveResource(type, namespaceURI, null, systemId, baseURI);
		result.getStringData();
	}
}
