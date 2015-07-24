package au.org.ecoinformatics.d1.poster.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.dataone.service.types.v1.Identifier;
import org.junit.Test;

public class UpdateDataonePosterStrategyTest {

	/**
	 * Can we trim the version from an identifier?
	 */
	@Test
	public void testTrimVersionFromPid01() {
		UpdateDataonePosterStrategy objectUnderTest = new UpdateDataonePosterStrategy();
		Identifier pid = new Identifier();
		pid.setValue("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20150515");
		String result = objectUnderTest.trimVersionFromPid(pid);
		assertThat(result, is("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG"));
	}
	
	/**
	 * Can we tell when we haven't and then have seen a PID before?
	 */
	@Test
	public void testVerifyPidHasntBeenSeenYet01() {
		UpdateDataonePosterStrategy objectUnderTest = new UpdateDataonePosterStrategy();
		Identifier pid = new Identifier();
		pid.setValue("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20150515");
		objectUnderTest.verifyPidHasntBeenSeenYet(pid);
		try {
			objectUnderTest.verifyPidHasntBeenSeenYet(pid);
			fail();
		} catch (IllegalStateException e) {
			// success!
		}
	}
	
	/**
	 * Can we tell when a PID is older than the one that we currently know about from the server?
	 */
	@Test
	public void testCurrentKnownVersionOfPidIsNewerThan01() throws Throwable {
		UpdateDataonePosterStrategy objectUnderTest = new UpdateDataonePosterStrategy();
		Identifier newPid = new Identifier();
		newPid.setValue("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20151212");
		Field f = UpdateDataonePosterStrategy.class.getDeclaredField("knownIdentifiersOnServer");
		f.setAccessible(true);
		Map<String, Identifier> knownIdentifiers = new HashMap<String, Identifier>();
		knownIdentifiers.put("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG", newPid);
		f.set(objectUnderTest, knownIdentifiers);
		Identifier oldPid = new Identifier();
		oldPid.setValue("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20150101");
		boolean result = objectUnderTest.currentKnownVersionOfPidIsNewerThan(oldPid);
		assertTrue("server version should be considered newer", result);
	}
	
	/**
	 * Can we tell when a PID is newer than the one that we currently know about from the server?
	 */
	@Test
	public void testCurrentKnownVersionOfPidIsNewerThan02() throws Throwable {
		UpdateDataonePosterStrategy objectUnderTest = new UpdateDataonePosterStrategy();
		Identifier oldPid = new Identifier();
		oldPid.setValue("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20150101");
		Field f = UpdateDataonePosterStrategy.class.getDeclaredField("knownIdentifiersOnServer");
		f.setAccessible(true);
		Map<String, Identifier> knownIdentifiers = new HashMap<String, Identifier>();
		knownIdentifiers.put("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG", oldPid);
		f.set(objectUnderTest, knownIdentifiers);
		Identifier newPid = new Identifier();
		newPid.setValue("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20151212");
		boolean result = objectUnderTest.currentKnownVersionOfPidIsNewerThan(oldPid);
		assertFalse("server version should NOT be considered newer", result);
	}
	
	/**
	 * Can we extract the version from a PID?
	 */
	@Test
	public void testExtractVersionFromPid01() throws Throwable {
		UpdateDataonePosterStrategy objectUnderTest = new UpdateDataonePosterStrategy();
		Identifier pid = new Identifier();
		pid.setValue("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20151212");
		int result = objectUnderTest.extractVersionFromPid(pid);
		assertThat(result, is(20151212));
	}
	
	/**
	 * Is the newest version of a PID returned?
	 */
	@Test
	public void testFindNewestExistingPid01() throws Throwable {
		UpdateDataonePosterStrategy objectUnderTest = new UpdateDataonePosterStrategy();
		Identifier pid = new Identifier();
		pid.setValue("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20150101");
		Field f = UpdateDataonePosterStrategy.class.getDeclaredField("knownIdentifiersOnServer");
		f.setAccessible(true);
		Map<String, Identifier> knownIdentifiers = new HashMap<String, Identifier>();
		knownIdentifiers.put("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG", pid);
		f.set(objectUnderTest, knownIdentifiers);
		Identifier result = objectUnderTest.findNewestExistingPid(pid);
		assertThat(result, is(pid));
	}
}
