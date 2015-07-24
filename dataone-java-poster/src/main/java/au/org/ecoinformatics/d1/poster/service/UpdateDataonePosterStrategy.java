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
	private static final String NOTHING = "";
	private static final int START_FROM_FIRST_RESULT = 0;
	private static final Boolean INCLUDE_REPLICATED = Boolean.TRUE;
	private static final Date START_OF_TIME = new Date(0l);
	private static final int LARGER_THAN_THE_NUMBER_OF_RESULTS_WE_WILL_EVER_HAVE = 999999;
	private static final Integer GET_ALL_RESULTS = LARGER_THAN_THE_NUMBER_OF_RESULTS_WE_WILL_EVER_HAVE;
	private static final ObjectFormatIdentifier ALL_FORMATS = null;
	private static final String PID_VERSION_SUFFIX_REGEX = "\\.20\\d{6}$";

	private final Set<String> seenPids = new HashSet<String>();
	private boolean isKnownIdentifiersPopulated = false;
	private Map<String, Identifier> knownIdentifiersOnServer;
	
	@Override
	public void execute(SystemMetadata sysmetaData, InputStream objectData, MNode nodeClient) throws EcoinformaticsDataonePosterException {
		logger.info("DataONE Poster: performing an 'update' operation");
		populateKnownPidsIfRequired(nodeClient);
		try {
			Identifier newPid = sysmetaData.getIdentifier();
			verifyPidHasntBeenSeenYet(newPid);
			Identifier existingPid = findNewestExistingPid(newPid);
			nodeClient.update(existingPid, objectData, newPid, sysmetaData);
			logger.info(String.format("Updated %s from version %s to %s", 
					trimVersionFromPid(existingPid), extractVersionFromPid(existingPid), extractVersionFromPid(newPid)));
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
	 */
	Identifier findNewestExistingPid(Identifier pid) throws EcoinformaticsDataonePosterException {
		String newPidWithoutVersion = trimVersionFromPid(pid);
		if (serverHasVersionOf(newPidWithoutVersion)) {
			return knownIdentifiersOnServer.get(newPidWithoutVersion);
		}
		throw new IllegalStateException("Runtime error: couldn't find existing record to update (when searching for it)");
		// TODO flip to create here to behave as an upsert
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
		logger.info("Starting population of known idenitifers from server");
		knownIdentifiersOnServer = new HashMap<String, Identifier>();
		Date now = new Date();
		try {
			ObjectList allObjs = nodeClient.listObjects(START_OF_TIME, now, ALL_FORMATS, 
					INCLUDE_REPLICATED, START_FROM_FIRST_RESULT, GET_ALL_RESULTS);
			logger.debug(String.format("Received %s known objects from the server", allObjs.sizeObjectInfoList()));
			for (ObjectInfo currObject : allObjs.getObjectInfoList()) {
				Identifier currExistingPid = currObject.getIdentifier();
				String currPidWithoutVersion = trimVersionFromPid(currExistingPid);
				if (serverHasVersionOf(currPidWithoutVersion) && currentKnownVersionOfPidIsNewerThan(currExistingPid)) {
					// don't need to update our mapping
					continue;
				}
				int pidVersion = extractVersionFromPid(currExistingPid);
				if (knownIdentifiersOnServer.containsKey(currPidWithoutVersion)) {
					logger.debug(String.format("Updating known PID mapping for %s to version %s", currPidWithoutVersion, pidVersion));
				} else {
					logger.debug(String.format("Adding known PID mapping for %s with version %s", currPidWithoutVersion, pidVersion));
				}
				knownIdentifiersOnServer.put(currPidWithoutVersion, currExistingPid);
			}
			logger.info(String.format("Processed %s known identifiers", knownIdentifiersOnServer.size()));
		} catch (ServiceFailure | InvalidToken | InvalidRequest | NotAuthorized | NotImplemented e) {
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
	 */
	boolean currentKnownVersionOfPidIsNewerThan(Identifier pid) {
		String pidWithoutVersion = trimVersionFromPid(pid);
		Identifier currentKnownPid = knownIdentifiersOnServer.get(pidWithoutVersion);
		int versionOfCurrentNewestPid = extractVersionFromPid(currentKnownPid);
		int versionOfSuppliedPid = extractVersionFromPid(pid);
		return versionOfCurrentNewestPid > versionOfSuppliedPid;
	}

	String trimVersionFromPid(Identifier pid) {
		return pid.getValue().replaceAll(PID_VERSION_SUFFIX_REGEX, NOTHING);
	}

	int extractVersionFromPid(Identifier pid) {
		String pidString = pid.getValue();
		return Integer.parseInt(pidString.substring(pidString.length() -8));
	}
}
