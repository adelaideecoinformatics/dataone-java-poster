package au.org.ecoinformatics.d1.poster.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SupersitesLternPidProcessingStrategyTest {

	/**
	 * Can we trim the version from a Supersites identifier?
	 */
	@Test
	public void testTrimVersionFromPid01() {
		SupersitesLternPidProcessingStrategy objectUnderTest = new SupersitesLternPidProcessingStrategy();
		String pid = "www.supersites.net.au/knb/metacat/wahyuni.9.46/html";
		String result = objectUnderTest.trimVersionFromPid(pid);
		assertThat(result, is("www.supersites.net.au/knb/metacat/wahyuni.9/html"));
	}
	
	/**
	 * Can we trim the version from an LTERN identifier?
	 */
	@Test
	public void testTrimVersionFromPid02() {
		SupersitesLternPidProcessingStrategy objectUnderTest = new SupersitesLternPidProcessingStrategy();
		String pid = "www.ltern.org.au/knb/metacat/ltern.99.22/html";
		String result = objectUnderTest.trimVersionFromPid(pid);
		assertThat(result, is("www.ltern.org.au/knb/metacat/ltern.99/html"));
	}
	
	/**
	 * Can we tell when a PID is NOT a Supersites one?
	 */
	@Test
	public void testCanHandle01() throws Throwable {
		SupersitesLternPidProcessingStrategy objectUnderTest = new SupersitesLternPidProcessingStrategy();
		String pid = "aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20151212";
		boolean result = objectUnderTest.canHandle(pid);
		assertFalse("PID should NOT be considered a Supersites one", result);
	}
	
	/**
	 * Can we tell when a PID is a Supersites one?
	 */
	@Test
	public void testCanHandle02() throws Throwable {
		SupersitesLternPidProcessingStrategy objectUnderTest = new SupersitesLternPidProcessingStrategy();
		String pid = "www.supersites.net.au/knb/metacat/lloyd.673.3/html";
		boolean result = objectUnderTest.canHandle(pid);
		assertTrue("PID should be considered a Supersites one", result);
	}
	
	/**
	 * Can we tell when a really short PID is NOT a Supersites one?
	 */
	@Test
	public void testCanHandle03() throws Throwable {
		SupersitesLternPidProcessingStrategy objectUnderTest = new SupersitesLternPidProcessingStrategy();
		String pid = "d.1.2";
		boolean result = objectUnderTest.canHandle(pid);
		assertFalse("PID should NOT be considered a Supersites one", result);
	}
	
	/**
	 * Can we tell when a PID is an LTERN one?
	 */
	@Test
	public void testCanHandle04() throws Throwable {
		SupersitesLternPidProcessingStrategy objectUnderTest = new SupersitesLternPidProcessingStrategy();
		String pid = "www.ltern.org.au/knb/metacat/ltern.99.22/html";
		boolean result = objectUnderTest.canHandle(pid);
		assertTrue("PID should be considered an LTERN one", result);
	}
	
	/**
	 * Can we extract the version from a Supersites PID?
	 */
	@Test
	public void testExtractVersionFromPid01() {
		SupersitesLternPidProcessingStrategy objectUnderTest = new SupersitesLternPidProcessingStrategy();
		String pid = "www.supersites.net.au/knb/metacat/lloyd.673.3/html";
		int result = objectUnderTest.extractVersionFromPid(pid);
		assertThat(result, is(3));
	}
	
	/**
	 * Can we extract the version from an LTERN PID?
	 */
	@Test
	public void testExtractVersionFromPid02() {
		SupersitesLternPidProcessingStrategy objectUnderTest = new SupersitesLternPidProcessingStrategy();
		String pid = "www.ltern.org.au/knb/metacat/ltern.99.22/html";
		int result = objectUnderTest.extractVersionFromPid(pid);
		assertThat(result, is(22));
	}
}
