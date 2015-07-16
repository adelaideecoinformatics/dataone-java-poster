package au.org.aekos.sysmetagen.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;

public class EmlNamespaceUnawareXmlParserTest {

	/**
	 * Can we parse an EML v2.1.1 document?
	 */
	@Test
	public void testParse01() throws Throwable {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("au/org/aekos/sysmetagen/example-eml-2.1.1.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		assertFalse(dbf.isNamespaceAware());
		Document emlDoc = dbf.newDocumentBuilder().parse(is);
		EmlNamespaceUnawareXmlParser objectUnderTest = new  EmlNamespaceUnawareXmlParser();
		SysMetaFragments result = objectUnderTest.parse(emlDoc);
		assertThat(result.getIdentifier(), is("aekos.org.au/collection/wa.gov.au/ravensthorpe"));
		assertThat(result.getContact(), is("Department of Parks and Wildlife (Biogeography Program)"));
		assertThat(result.getFormatId(), is("eml://ecoinformatics.org/eml-2.1.1"));
	}
	
	/**
	 * Can we parse an EML v2.1.0 document?
	 */
	@Test
	public void testParse02() throws Throwable {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("au/org/aekos/sysmetagen/example-eml-2.1.0.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		assertFalse(dbf.isNamespaceAware());
		Document emlDoc = dbf.newDocumentBuilder().parse(is);
		EmlNamespaceUnawareXmlParser objectUnderTest = new  EmlNamespaceUnawareXmlParser();
		SysMetaFragments result = objectUnderTest.parse(emlDoc);
		assertThat(result.getIdentifier(), is("aekos.org.au/collection/wa.gov.au/ravensthorpe"));
		assertThat(result.getContact(), is("Department of Parks and Wildlife (Biogeography Program)"));
		assertThat(result.getFormatId(), is("eml://ecoinformatics.org/eml-2.1.0"));
	}
	
	/**
	 * Can we tell when we can parse a file?
	 */
	@Test
	public void testCanParse01() throws Throwable {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("au/org/aekos/sysmetagen/example-eml-2.1.1.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		assertFalse(dbf.isNamespaceAware());
		Document emlDoc = dbf.newDocumentBuilder().parse(is);
		EmlNamespaceUnawareXmlParser objectUnderTest = new  EmlNamespaceUnawareXmlParser();
		boolean result = objectUnderTest.canParse(emlDoc);
		assertTrue("EML should be parseable by this parser", result);
	}
	
	/**
	 * Can we tell when we cannot parse a file?
	 */
	@Test
	public void testCanParse02() throws Throwable {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("au/org/aekos/sysmetagen/example-gmd.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		assertFalse(dbf.isNamespaceAware());
		Document gmdDoc = dbf.newDocumentBuilder().parse(is);
		EmlNamespaceUnawareXmlParser objectUnderTest = new  EmlNamespaceUnawareXmlParser();
		boolean result = objectUnderTest.canParse(gmdDoc);
		assertFalse("GMD should not be parseable by this parser", result);
	}
}
