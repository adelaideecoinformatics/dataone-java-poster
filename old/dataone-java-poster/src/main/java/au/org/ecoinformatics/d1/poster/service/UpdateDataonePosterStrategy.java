package au.org.ecoinformatics.d1.poster.service;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dataone.client.MNode;
import org.dataone.service.exceptions.IdentifierNotUnique;
import org.dataone.service.exceptions.InsufficientResources;
import org.dataone.service.exceptions.InvalidRequest;
import org.dataone.service.exceptions.InvalidSystemMetadata;
import org.dataone.service.exceptions.InvalidToken;
import org.dataone.service.exceptions.NotAuthorized;
import org.dataone.service.exceptions.NotFound;
import org.dataone.service.exceptions.NotImplemented;
import org.dataone.service.exceptions.ServiceFailure;
import org.dataone.service.exceptions.UnsupportedType;
import org.dataone.service.types.v1.Identifier;
import org.dataone.service.types.v1.ObjectFormatIdentifier;
import org.dataone.service.types.v1.ObjectInfo;
import org.dataone.service.types.v1.ObjectList;
import org.dataone.service.types.v1.SystemMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Performs an 'update' operation whereby an existing version of an object is updated with a new object.
 * The new version is marked as obsoleting the old one.
 */
public class UpdateDataonePosterStrategy implements DataonePosterStrategy {

	private static final Logger logger = LoggerFactory.getLogger(UpdateDataonePosterStrategy.class);
	static final int START_FROM_FIRST_RESULT = 0;
	static final Boolean INCLUDE_REPLICATED = Boolean.TRUE;
	private static final Date START_OF_TIME = new Date(0l);
	private static final int LARGER_THAN_THE_NUMBER_OF_RESULTS_WE_WILL_EVER_HAVE = 999999;
	static final Integer GET_ALL_RESULTS = LARGER_THAN_THE_NUMBER_OF_RESULTS_WE_WILL_EVER_HAVE;
	private static final ObjectFormatIdentifier ALL_FORMATS = null;

	private final Set<String> seenPids = new HashSet<String>();
	private boolean isKnownIdentifiersPopulated = false;
	private Map<String, Identifier> knownIdentifiersOnServer;
	private DataonePosterStrategy fallbackStrategy;
	private Set<PidProcessingStrategy> pidProcessingStrategies = new HashSet<PidProcessingStrategy>();
	
	@Override
	public boolean execute(SystemMetadata sysmetaData, InputStream objectData, MNode nodeClient) throws EcoinformaticsDataonePosterException {
		logger.info("DataONE Poster: performing an 'update' operation");
		populateKnownPidsIfRequired(nodeClient);
		try {
			Identifier newPid = sysmetaData.getIdentifier();
			verifyPidHasntBeenSeenYet(newPid);
			Identifier existingPid = findNewestExistingPid(newPid);
			nodeClient.update(existingPid, objectData, newPid, sysmetaData);
			PidProcessingStrategy pidStrategy = getStrategyFor(newPid.getValue()); // assume that the old and new PIDs are the same format
			logger.info(String.format("Updated %s from version %s to %s", 
					pidStrategy.trimVersionFromPid(existingPid.getValue()), 
					pidStrategy.extractVersionFromPid(existingPid.getValue()), 
					pidStrategy.extractVersionFromPid(newPid.getValue())));
			return true;
		} catch (IdentifierNotUnique | InsufficientResources | ServiceFailure | UnsupportedType | InvalidToken e) {
			throw new EcoinformaticsDataonePosterException("Runtime error: failed to POST to the dataONE node", e);
		} catch (InvalidRequest e) {
			throw new EcoinformaticsDataonePosterException("Runtime error: request is invalid", e);
		} catch (InvalidSystemMetadata e) {
			throw new EcoinformaticsDataonePosterException("Runtime error: sysmeta is invalid", e);
		} catch (NotAuthorized e) {
			throw new EcoinformaticsDataonePosterException("Runtime error: authorisation failure", e);
		} catch (NotImplemented e) {
			throw new EcoinformaticsDataonePosterException("Runtime error: requested operation is not available on the server", e);
		} catch (NotFound e) {
			throw new EcoinformaticsDataonePosterException("Runtime error: couldn't find existing record to update (when performing the operation)", e);
		} catch (NoExistingRecordFoundException e) {
			logger.warn("Encountered problem during 'update'. " + e.getMessage() + ". Going to fallback strategy");
			return fallbackStrategy.execute(sysmetaData, objectData, nodeClient);
		} catch (NoStrategyFoundException e) {
			throw new EcoinformaticsDataonePosterException("Programmer error: couldn't find a PID strategy.", e);
		}
	}

	void verifyPidHasntBeenSeenYet(Identifier pid) {
		if (seenPids.contains(pid.getValue())) {
			String msgTemplate = "Record error: refusing to process %s because we've already seen "
					+ "another version of this object. This is NOT supported.";
			throw new IllegalStateException(String.format(msgTemplate, pid));
			// FIXME a more graceful failure here would be nice, something that lets us continue on
		}
		seenPids.add(pid.getValue());
	}

	/**
	 * Finds the newest version of a given PID.
	 * It's pretty easy at the moment because all the hard work is done elsewhere.
	 * 
	 * @param pid	PID to get newest version of
	 * @return		newest version, if one exists, otherwise an explosion
	 * @throws EcoinformaticsDataonePosterException	when we cannot find an existing version
	 * @throws NoExistingRecordFoundException		when no version of this record exists 
	 * @throws NoStrategyFoundException				when we don't have a strategy to handle the PID
	 */
	Identifier findNewestExistingPid(Identifier pid) throws EcoinformaticsDataonePosterException, NoExistingRecordFoundException, NoStrategyFoundException {
		String newPidWithoutVersion = getStrategyFor(pid.getValue()).trimVersionFromPid(pid.getValue());
		if (serverHasVersionOf(newPidWithoutVersion)) {
			return knownIdentifiersOnServer.get(newPidWithoutVersion);
		}
		throw new NoExistingRecordFoundException("Couldn't find existing record for " + newPidWithoutVersion);
	}

	private class NoExistingRecordFoundException extends Exception {

		private static final long serialVersionUID = 1L;

		public NoExistingRecordFoundException(String message) {
			super(message);
		}
	}
	
	static class NoStrategyFoundException extends Exception {

		private static final long serialVersionUID = 1L;

		public NoStrategyFoundException(String message) {
			super(message);
		}
	}
	
	/**
	 * Checks if the server has a (presumably older) version of the supplied PID.
	 * Note: ANY existing version is considered older, we don't check version numbers.
	 * 
	 * @param pidWithoutVersion	PID (without version suffix) to search for
	 * @return					<code>true</code> if another version exists, <code>false</code> otherwise
	 */
	private boolean serverHasVersionOf(String pidWithoutVersion) {
		return knownIdentifiersOnServer.containsKey(pidWithoutVersion);
	}

	/**
	 * Gets a list of all identifiers that the server knows about and stores it in a map
	 * to allow easy checking for old versions of objects. We only do this once at the 
	 * start of the load which means that you CANNOT do two updates to an object in one 
	 * run of this program.
	 * 
	 * @param nodeClient	client to use to contact server
	 * @throws EcoinformaticsDataonePosterException	when something goes wrong contacting the server
	 */
	private void populateKnownPidsIfRequired(MNode nodeClient) throws EcoinformaticsDataonePosterException {
		if (isKnownIdentifiersPopulated) {
			return;
		}
		isKnownIdentifiersPopulated = true;
		logger.info("Starting population of known idenitifers from server");
		knownIdentifiersOnServer = new HashMap<String, Identifier>();
		Date now = new Date();
		try {
			ObjectList allObjs = nodeClient.listObjects(START_OF_TIME, now, ALL_FORMATS, 
					INCLUDE_REPLICATED, START_FROM_FIRST_RESULT, GET_ALL_RESULTS);
			logger.debug(String.format("Received %s known objects from the server", allObjs.sizeObjectInfoList()));
			for (ObjectInfo currObject : allObjs.getObjectInfoList()) {
				Identifier currExistingPid = currObject.getIdentifier();
				String currExistingPidValue = currExistingPid.getValue();
				PidProcessingStrategy pidStrategy;
				try {
					pidStrategy = getStrategyFor(currExistingPidValue);
				} catch (NoStrategyFoundException e) {
					logger.warn("Potential programmer error: no strategy to handle PID '" + currExistingPidValue + "'.");
					continue;
				}
				String currPidWithoutVersion = pidStrategy.trimVersionFromPid(currExistingPidValue);
				boolean dontNeedToUpdateMapping = serverHasVersionOf(currPidWithoutVersion) && currentKnownVersionOfPidIsNewerThan(currExistingPid);
				if (dontNeedToUpdateMapping) {
					continue;
				}
				int pidVersion = pidStrategy.extractVersionFromPid(currExistingPidValue);
				if (knownIdentifiersOnServer.containsKey(currPidWithoutVersion)) {
					logger.debug(String.format("Updating known PID mapping for %s to version %s", currPidWithoutVersion, pidVersion));
				} else {
					logger.debug(String.format("Adding known PID mapping for %s with version %s", currPidWithoutVersion, pidVersion));
				}
				knownIdentifiersOnServer.put(currPidWithoutVersion, currExistingPid);
			}
			logger.info(String.format("Processed %s known identifiers", knownIdentifiersOnServer.size()));
		} catch (ServiceFailure | InvalidToken | InvalidRequest | NotAuthorized | NotImplemented | NoStrategyFoundException e) {
			throw new EcoinformaticsDataonePosterException("Runtime error: failed to retrieve the list of all existing objects", e);
		}
	}
	
	/**
	 * Determines if the supplied PID is older than the version currently in the known
	 * identifiers. We're still processing the known identifiers list so it's only _currently_
	 * the newest.
	 * 
	 * @param pid	PID to compare
	 * @return		<code>true</code> if the supplied PID is older, <code>false</code> otherwise
	 * @throws NoStrategyFoundException		when we don't have a strategy to handle the PID
	 */
	boolean currentKnownVersionOfPidIsNewerThan(Identifier pid) throws NoStrategyFoundException {
		PidProcessingStrategy pidStrategy = getStrategyFor(pid.getValue());
		String pidWithoutVersion = pidStrategy.trimVersionFromPid(pid.getValue());
		Identifier currentKnownPid = knownIdentifiersOnServer.get(pidWithoutVersion);
		int versionOfCurrentNewestPid = pidStrategy.extractVersionFromPid(currentKnownPid.getValue());
		int versionOfSuppliedPid = pidStrategy.extractVersionFromPid(pid.getValue());
		return versionOfCurrentNewestPid > versionOfSuppliedPid;
	}

	PidProcessingStrategy getStrategyFor(String pid) throws NoStrategyFoundException {
		for (PidProcessingStrategy currStrategy : pidProcessingStrategies) {
			if (!currStrategy.canHandle(pid)) {
				continue;
			}
			return currStrategy;
		}
		// TODO handle more gracefully so we can continue
		throw new NoStrategyFoundException("Programmer error: couldn't find a strategy to handle the supplied ID '" + pid + "'.");
	}

	public void setFallbackStrategy(DataonePosterStrategy fallbackStrategy) {
		this.fallbackStrategy = fallbackStrategy;
	}

	public void setPidProcessingStrategies(Set<PidProcessingStrategy> pidProcessingStrategies) {
		this.pidProcessingStrategies = pidProcessingStrategies;
	}
}
