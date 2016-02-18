package au.org.ecoinformatics.d1.poster.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AekosPidProcessingStrategyTest {

	/**
	 * Can we trim the version from an identifier?
	 */
	@Test
	public void testTrimVersionFromPid01() {
		AekosPidProcessingStrategy objectUnderTest = new AekosPidProcessingStrategy();
		String pid = "aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20150515";
		String result = objectUnderTest.trimVersionFromPid(pid);
		assertThat(result, is("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG"));
	}
	
	/**
	 * Can we tell when a PID is NOT an AEKOS one?
	 */
	@Test
	public void testCanHandle01() throws Throwable {
		AekosPidProcessingStrategy objectUnderTest = new AekosPidProcessingStrategy();
		String pid = "lloyd.238.26";
		boolean result = objectUnderTest.canHandle(pid);
		assertFalse("PID should NOT be considered an AEKOS one", result);
	}
	
	/**
	 * Can we tell when a PID is an AEKOS one?
	 */
	@Test
	public void testCanHandle02() throws Throwable {
		AekosPidProcessingStrategy objectUnderTest = new AekosPidProcessingStrategy();
		String pid = "aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20151212";
		boolean result = objectUnderTest.canHandle(pid);
		assertTrue("PID should be considered an AEKOS one", result);
	}
	
	/**
	 * Can we tell when a really short PID is NOT an AEKOS one?
	 */
	@Test
	public void testCanHandle03() throws Throwable {
		AekosPidProcessingStrategy objectUnderTest = new AekosPidProcessingStrategy();
		String pid = "d.1.2";
		boolean result = objectUnderTest.canHandle(pid);
		assertFalse("PID should NOT be considered an AEKOS one", result);
	}
	
	/**
	 * Can we extract the version from a AEKOS PID?
	 */
	@Test
	public void testExtractVersionFromPid01() {
		AekosPidProcessingStrategy objectUnderTest = new AekosPidProcessingStrategy();
		String pid = "aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20151212";
		int result = objectUnderTest.extractVersionFromPid(pid);
		assertThat(result, is(20151212));
	}
	
	/**
	 * Do this method explode when we give it a non-AEKOS PID?
	 */
	@Test(expected=NumberFormatException.class)
	public void testExtractVersionFromPid02() {
		AekosPidProcessingStrategy objectUnderTest = new AekosPidProcessingStrategy();
		String pid = "lloyd.238.26";
		objectUnderTest.extractVersionFromPid(pid);
	}
}
