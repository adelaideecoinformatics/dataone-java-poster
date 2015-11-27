package au.org.ecoinformatics.d1.poster.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dataone.client.MNode;
import org.dataone.service.types.v1.Identifier;
import org.dataone.service.types.v1.SystemMetadata;
import org.junit.Test;

import au.org.ecoinformatics.d1.poster.service.UpdateDataonePosterStrategy.NoStrategyFoundException;

public class UpdateDataonePosterStrategyTest {

	/**
	 * Can we tell when we haven't and then have seen a PID before?
	 */
	@Test
	public void testVerifyPidHasntBeenSeenYet01() {
		UpdateDataonePosterStrategy objectUnderTest = new UpdateDataonePosterStrategy();
		Identifier pid = identifier("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20150515");
		objectUnderTest.verifyPidHasntBeenSeenYet(pid);
		try {
			objectUnderTest.verifyPidHasntBeenSeenYet(pid);
			fail();
		} catch (IllegalStateException e) {
			// success!
		}
	}
	
	/**
	 * Do we call the update operation on the node with the expected information when an existing version of the PID exists?
	 */
	@Test
	public void testExecute01() throws Throwable {
		String identifier = "aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG";
		String version = ".20150101";
		Identifier existingPid = identifier(identifier + version);
		UpdateDataonePosterStrategy objectUnderTest = getObjectUnderTestWithKnownPid(identifier, version);
		Identifier newPid = identifier("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20160606");
		SystemMetadata sysmetaData = new SystemMetadata();
		sysmetaData.setIdentifier(newPid);
		InputStream objectData = null;
		MNode nodeClient = mock(MNode.class);
		objectUnderTest.execute(sysmetaData, objectData, nodeClient);
		verify(nodeClient).update(existingPid, objectData, newPid, sysmetaData);
	}
	
	/**
	 * Do we call the fall back strategy when there is no existing version of the PID?
	 */
	@Test
	public void testExecute02() throws Throwable {
		UpdateDataonePosterStrategy objectUnderTest = getObjectUnderTest();
		DataonePosterStrategy fallbackStrategy = mock(DataonePosterStrategy.class);
		objectUnderTest.setFallbackStrategy(fallbackStrategy);
		Identifier newPid = identifier("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20160606");
		SystemMetadata sysmetaData = new SystemMetadata();
		sysmetaData.setIdentifier(newPid);
		InputStream objectData = null;
		MNode nodeClient = mock(MNode.class);
		objectUnderTest.execute(sysmetaData, objectData, nodeClient);
		verify(fallbackStrategy).execute(sysmetaData, objectData, nodeClient);
	}
	
	/**
	 * Can we tell when a PID is older than the one that we currently know about from the server?
	 */
	@Test
	public void testCurrentKnownVersionOfPidIsNewerThan01() throws Throwable {
		UpdateDataonePosterStrategy objectUnderTest = getObjectUnderTestWithKnownPid(
				"aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG", ".20151212");
		Identifier olderPid = identifier("aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/JTH_GG.20150101");
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
		Identifier olderPid = identifier(identifier+".19990101");
		Identifier result = objectUnderTest.findNewestExistingPid(olderPid);
		Identifier newestPid = identifier(identifier+version);
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
	@Test(expected=NoStrategyFoundException.class)
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
		UpdateDataonePosterStrategy result = getObjectUnderTest();
		
		Identifier knownPid = identifier(identifier+version);
		
		Field f = UpdateDataonePosterStrategy.class.getDeclaredField("knownIdentifiersOnServer");
		f.setAccessible(true);
		Map<String, Identifier> knownIdentifiers = new HashMap<String, Identifier>();
		knownIdentifiers.put(identifier, knownPid);
		f.set(result, knownIdentifiers);
		return result;
	}

	private UpdateDataonePosterStrategy getObjectUnderTest() throws NoSuchFieldException, IllegalAccessException {
		UpdateDataonePosterStrategy result = new UpdateDataonePosterStrategy();
		
		Field f = UpdateDataonePosterStrategy.class.getDeclaredField("knownIdentifiersOnServer");
		f.setAccessible(true);
		Map<String, Identifier> knownIdentifiers = new HashMap<String, Identifier>();
		f.set(result, knownIdentifiers);
		
		Field f2 = UpdateDataonePosterStrategy.class.getDeclaredField("isKnownIdentifiersPopulated");
		f2.setAccessible(true);
		f2.set(result, true);
		
		Set<PidProcessingStrategy> strategies = new HashSet<PidProcessingStrategy>();
		strategies.add(new AekosPidProcessingStrategy());
		result.setPidProcessingStrategies(strategies);
		return result;
	}
	
	private Identifier identifier(String pidValue) {
		Identifier result = new Identifier();
		result.setValue(pidValue);
		return result;
	}
}
