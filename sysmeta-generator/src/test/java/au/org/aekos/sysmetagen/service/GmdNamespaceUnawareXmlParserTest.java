package au.org.aekos.sysmetagen.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;

public class GmdNamespaceUnawareXmlParserTest {
	
	/**
	 * Can we parse a GMD document?
	 */
	@Test
	public void testParse01() throws Throwable {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("au/org/aekos/sysmetagen/example-gmd.xml");
		Document gmdDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
		GmdNamespaceUnawareXmlParser objectUnderTest = new  GmdNamespaceUnawareXmlParser();
		SysMetaFragments result = objectUnderTest.parse(gmdDoc);
		assertThat(result.getIdentifier(), is("3478cacc-65c1-4ef0-ae11-0ef923a91857"));
		assertThat(result.getContact(), is("Australian Government Department of Sustainability, Environment, Water, Population and Communities"));
		assertThat(result.getFormatId(), is("http://www.isotc211.org/2005/gmd"));
	}
	
	/**
	 * Can we tell when we can parse a file?
	 */
	@Test
	public void testCanParse01() throws Throwable {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("au/org/aekos/sysmetagen/example-gmd.xml");
		Document gmdDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
		GmdNamespaceUnawareXmlParser objectUnderTest = new  GmdNamespaceUnawareXmlParser();
		boolean result = objectUnderTest.canParse(gmdDoc);
		assertTrue("GMD should be parseable by this parser", result);
	}
	
	/**
	 * Can we tell when we cannot parse a file?
	 */
	@Test
	public void testCanParse02() throws Throwable {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("au/org/aekos/sysmetagen/example-eml-2.1.1.xml");
		Document emlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
		GmdNamespaceUnawareXmlParser objectUnderTest = new  GmdNamespaceUnawareXmlParser();
		boolean result = objectUnderTest.canParse(emlDoc);
		assertFalse("EML should not be parseable by this parser", result);
	}
}
