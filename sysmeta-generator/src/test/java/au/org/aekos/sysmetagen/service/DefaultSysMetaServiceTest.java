package au.org.aekos.sysmetagen.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;

import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.sysmeta.SystemMetadata;

public class DefaultSysMetaServiceTest {

	/**
	 * Is the sysmeta document created using the supplied details (for EML)?
	 */
	@Test
	public void testCreateSysMetaFile01() throws Throwable {
		DefaultSysMetaService objectUnderTest = new DefaultSysMetaService();
		SysMetaFragments details = new SysMetaFragments("some-id", "a contact", "eml://ecoinformatics.org/eml-2.1.1");
		SystemMetadata result = objectUnderTest.createSysMetaFile(details, new BigInteger("123"), "checksumA1B2");
		assertThat(result.getIdentifier().getValue(), is("some-id"));
		assertThat(result.getSize(), is(new BigInteger("123")));
		assertThat(result.getFormatId(), is("eml://ecoinformatics.org/eml-2.1.1"));
		assertThat(result.getChecksum().getValue(), is("checksumA1B2"));
		assertThat(result.getRightsHolder().getValue(), is(DefaultSysMetaService.AUTHENTICATED_USER_SUBJECT_STRING));
		assertNull(result.getSubmitter());
	}
	
	/**
	 * Is the sysmeta document created using the supplied details (for GMD)?
	 */
	@Test
	public void testCreateSysMetaFile02() throws Throwable {
		DefaultSysMetaService objectUnderTest = new DefaultSysMetaService();
		SysMetaFragments details = new SysMetaFragments("some-id", "a contact", "http://www.isotc211.org/2005/gmd");
		SystemMetadata result = objectUnderTest.createSysMetaFile(details, new BigInteger("123"), "checksumA1B2");
		assertThat(result.getIdentifier().getValue(), is("some-id"));
		assertThat(result.getSize(), is(new BigInteger("123")));
		assertThat(result.getFormatId(), is("http://www.isotc211.org/2005/gmd"));
		assertThat(result.getChecksum().getValue(), is("checksumA1B2"));
		assertThat(result.getRightsHolder().getValue(), is(DefaultSysMetaService.AUTHENTICATED_USER_SUBJECT_STRING));
		assertNull(result.getSubmitter());
	}
	
	/**
	 * Can we read in and calculate the checksum for a given file?
	 */
	@Test
	public void testGetChecksumOfInputFile01() throws Throwable {
		DefaultSysMetaService objectUnderTest = new DefaultSysMetaService();
		Path targetFile = Files.createTempFile("testGetChecksumOfInputFile01", ".testfile");
		PrintWriter printWriter = new PrintWriter(targetFile.toFile());
		printWriter.write("You're a wizard, Harry");
		printWriter.close();
		String result = objectUnderTest.getChecksumOfInputFile(targetFile);
		assertThat(result, is("9208f545abf366c0de3550f701666b58"));
	}
	
	/**
	 * Can we read in and calculate the size of a given file?
	 */
	@Test
	public void testGetInputFileSize01() throws Throwable {
		DefaultSysMetaService objectUnderTest = new DefaultSysMetaService();
		Path targetFile = Files.createTempFile("testGetInputFileSize01", ".testfile");
		PrintWriter printWriter = new PrintWriter(targetFile.toFile());
		printWriter.write("A computer would deserve to be called intelligent if it could deceive a human into believing that it was human.");
		printWriter.close();
		BigInteger result = objectUnderTest.getInputFileSize(targetFile);
		assertThat(result.toString(), is("111"));
	}
	
	/**
	 * Can we identify and parse an EML v2.1.1 document?
	 */
	@Test
	public void testFindAndExecuteParser01() throws Throwable {
		DefaultSysMetaService objectUnderTest = new DefaultSysMetaService();
		Set<XmlParser> parsers = new HashSet<XmlParser>();
		parsers.add(new EmlNamespaceUnawareXmlParser());
		parsers.add(new GmdNamespaceUnawareXmlParser());
		objectUnderTest.setParsers(parsers);
		InputStream emlStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("au/org/aekos/sysmetagen/example-eml-2.1.1.xml");
		SysMetaFragments result = objectUnderTest.findAndExecuteParser(emlStream);
		assertThat(result.getIdentifier(), is("aekos.org.au/collection/wa.gov.au/ravensthorpe"));
		assertThat(result.getContact(), is("Department of Parks and Wildlife (Biogeography Program)"));
		assertThat(result.getFormatId(), is("eml://ecoinformatics.org/eml-2.1.1"));
	}
	
	/**
	 * Can we identify and parse an EML v2.1.0 document?
	 */
	@Test
	public void testFindAndExecuteParser02() throws Throwable {
		DefaultSysMetaService objectUnderTest = new DefaultSysMetaService();
		Set<XmlParser> parsers = new HashSet<XmlParser>();
		parsers.add(new EmlNamespaceUnawareXmlParser());
		parsers.add(new GmdNamespaceUnawareXmlParser());
		objectUnderTest.setParsers(parsers);
		InputStream emlStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("au/org/aekos/sysmetagen/example-eml-2.1.0.xml");
		SysMetaFragments result = objectUnderTest.findAndExecuteParser(emlStream);
		assertThat(result.getIdentifier(), is("aekos.org.au/collection/wa.gov.au/ravensthorpe"));
		assertThat(result.getContact(), is("Department of Parks and Wildlife (Biogeography Program)"));
		assertThat(result.getFormatId(), is("eml://ecoinformatics.org/eml-2.1.0"));
	}
	
	/**
	 * Can we identify and parse a GMD document?
	 */
	@Test
	public void testFindAndExecuteParser03() throws Throwable {
		DefaultSysMetaService objectUnderTest = new DefaultSysMetaService();
		Set<XmlParser> parsers = new HashSet<XmlParser>();
		parsers.add(new EmlNamespaceUnawareXmlParser());
		parsers.add(new GmdNamespaceUnawareXmlParser());
		objectUnderTest.setParsers(parsers);
		InputStream gmdStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("au/org/aekos/sysmetagen/example-gmd.xml");
		SysMetaFragments result = objectUnderTest.findAndExecuteParser(gmdStream);
		assertThat(result.getIdentifier(), is("3478cacc-65c1-4ef0-ae11-0ef923a91857"));
		assertThat(result.getContact(), is("Australian Government Department of Sustainability, Environment, Water, Population and Communities"));
		assertThat(result.getFormatId(), is("http://www.isotc211.org/2005/gmd"));
	}
}
