package au.org.ecoinformatics.d1.poster.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dataone.service.types.v1.Identifier;
import org.junit.Test;

public class UpdateDataonePosterStrategyTest {

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
		UpdateDataonePosterStrategy objectUnderTest = getObjectUnderTestWithKnownPid(
				"aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG", ".20151212");
		Identifier olderPid = new Identifier();
		olderPid.setValue("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20150101");
		boolean result = objectUnderTest.currentKnownVersionOfPidIsNewerThan(olderPid);
		assertTrue("server version should be considered newer", result);
	}
	
	/**
	 * Can we tell when a PID is newer than the one that we currently know about from the server?
	 */
	@Test
	public void testCurrentKnownVersionOfPidIsNewerThan02() throws Throwable {
		UpdateDataonePosterStrategy objectUnderTest = getObjectUnderTestWithKnownPid(
				"aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG", ".20150101");
		Identifier newerPid = new Identifier();
		newerPid.setValue("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20151212");
		boolean result = objectUnderTest.currentKnownVersionOfPidIsNewerThan(newerPid);
		assertFalse("server version should NOT be considered newer", result);
	}
	
	/**
	 * Is the newest version of a PID returned?
	 */
	@Test
	public void testFindNewestExistingPid01() throws Throwable {
		String identifier = "aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG";
		String version = ".20150101";
		UpdateDataonePosterStrategy objectUnderTest = getObjectUnderTestWithKnownPid(identifier, version);
		Identifier olderPid = new Identifier();
		olderPid.setValue(identifier+".19990101");
		Identifier result = objectUnderTest.findNewestExistingPid(olderPid);
		Identifier newestPid = new Identifier();
		newestPid.setValue(identifier+version);
		assertThat(result, is(newestPid));
	}
	
	/**
	 * Can we find the first strategy that can handle the PID?
	 */
	@Test
	public void testGetStrategyFor01() throws Throwable {
		UpdateDataonePosterStrategy objectUnderTest = new UpdateDataonePosterStrategy();
		Set<PidProcessingStrategy> pidProcessingStrategies = new HashSet<PidProcessingStrategy>();
		pidProcessingStrategies.add(new PickMePidProcessingStrategy());
		pidProcessingStrategies.add(new CantDoAnythingPidProcessingStrategy());
		objectUnderTest.setPidProcessingStrategies(pidProcessingStrategies);
		PidProcessingStrategy result = objectUnderTest.getStrategyFor("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20150101");
		assertTrue(result instanceof PickMePidProcessingStrategy);
	}

	/**
	 * Is the expected exception thrown when we can't find a strategy?
	 */
	@Test(expected=IllegalStateException.class)
	public void testGetStrategyFor02() throws Throwable {
		UpdateDataonePosterStrategy objectUnderTest = new UpdateDataonePosterStrategy();
		Set<PidProcessingStrategy> pidProcessingStrategies = new HashSet<PidProcessingStrategy>();
		pidProcessingStrategies.add(new CantDoAnythingPidProcessingStrategy());
		objectUnderTest.setPidProcessingStrategies(pidProcessingStrategies);
		objectUnderTest.getStrategyFor("nothing.will.process.me");
	}
	
	private class CantDoAnythingPidProcessingStrategy implements PidProcessingStrategy {
		@Override public String trimVersionFromPid(String pid) { return null; }
		@Override public int extractVersionFromPid(String pid) { return 0; }
		@Override public boolean canHandle(String pid) {
			return false;
		}
	}
	
	private class PickMePidProcessingStrategy implements PidProcessingStrategy {
		@Override public String trimVersionFromPid(String pid) { return null; }
		@Override public int extractVersionFromPid(String pid) { return 0; }
		@Override public boolean canHandle(String pid) {
			return true;
		}
	}
	
	private UpdateDataonePosterStrategy getObjectUnderTestWithKnownPid(
			String identifier, String version) throws NoSuchFieldException, IllegalAccessException {
		UpdateDataonePosterStrategy result = new UpdateDataonePosterStrategy();
		Identifier knownPid = new Identifier();
		knownPid.setValue(identifier+version);
		Field f = UpdateDataonePosterStrategy.class.getDeclaredField("knownIdentifiersOnServer");
		f.setAccessible(true);
		Map<String, Identifier> knownIdentifiers = new HashMap<String, Identifier>();
		knownIdentifiers.put(identifier, knownPid);
		f.set(result, knownIdentifiers);
		Set<PidProcessingStrategy> strategies = new HashSet<PidProcessingStrategy>();
		strategies.add(new AekosPidProcessingStrategy());
		result.setPidProcessingStrategies(strategies);
		return result;
	}
}
