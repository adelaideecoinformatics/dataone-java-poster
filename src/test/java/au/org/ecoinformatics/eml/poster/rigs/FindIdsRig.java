package au.org.ecoinformatics.eml.poster.rigs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dataone.client.MNode;
import org.dataone.service.types.v1.ObjectFormatIdentifier;
import org.dataone.service.types.v1.ObjectInfo;
import org.dataone.service.types.v1.ObjectList;

/**
 * This is NOT a test. This is a rig to connect to the live DataONE instance
 * and find the ID of objects using some filter criteria.
 */
public class FindIdsRig {
	
	private static final Logger logger = Logger.getLogger(FindIdsRig.class);
	private static final int LARGER_THAN_THE_NUMBER_OF_RESULTS_WELL_EVER_HAVE = 999999;
	private static final int RESULT_SIZE = LARGER_THAN_THE_NUMBER_OF_RESULTS_WELL_EVER_HAVE;
	
	// Edit these if required \/
	private static final String NODE_ENDPOINT = "https://dataone.ecoinformatics.org.au/mn";
	private static final String TARGET_FORMAT_ID = "eml://ecoinformatics.org/eml-2.1.1";
	private static final List<String> INCLUDE_REGEX_FILTERS = new ArrayList<String>();
	static {
		INCLUDE_REGEX_FILTERS.addAll(Arrays.asList(
			new String[] {
				// FIXME could also just negate what we expect from AEKOS
				"^bradford\\..+",
				"^datalibrarian\\..+",
				"^ellsworth\\..+",
				"^fahmi\\..+",
				"^lloyd\\..+",
				"^lollback\\..+",
				"^ltern2\\..+",
				"^ltern\\..+",
				"^mccafferty\\..+",
				"^McKeown\\..+",
				"^shuker\\..+",
				"^supersite\\..+",
				"^tucker\\..+",
				"^wahyuni\\..+"
			}
		));
	}
	// Edit these if required /\
	
	private MNode nodeClient;
	private int objectsProcessed = 0;
	private int objectsMatched = 0;

	private void run() throws Throwable {
		setup();
		doFind();
	}
	
	private void setup() throws Throwable {
		logger.info("# Connecting to the endpoint: " + NODE_ENDPOINT);
		nodeClient = new MNode(NODE_ENDPOINT);
		nodeClient.ping();
	}

	private void doFind() throws Throwable {
		logger.info("# Commencing search for identifiers");
		ObjectFormatIdentifier formatId = new ObjectFormatIdentifier();
		formatId.setValue(TARGET_FORMAT_ID);
		ObjectList allObjs = nodeClient.listObjects(new Date(0l), new Date(), formatId, Boolean.TRUE, 0, RESULT_SIZE);
		for (ObjectInfo currObject : allObjs.getObjectInfoList()) {
			objectsProcessed++;
			String identifierText = currObject.getIdentifier().getValue();
			if (!matchesAnyFilter(identifierText)) {
				continue;
			}
			objectsMatched++;
			String formattedAsJavaArrayItem = "\"" + identifierText + "\",";
			logger.info(formattedAsJavaArrayItem);
		}
		logger.info(String.format("#Finished search for identifiers. Searched %d objects and matched %d", 
				objectsProcessed, objectsMatched));
	}
	
	static boolean matchesAnyFilter(String identifier) {
		for (String currFilter : INCLUDE_REGEX_FILTERS) {
			if (identifier.matches(currFilter)) {
				logger.debug("# Matched using filter: " + currFilter);
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		try {
			new FindIdsRig().run();
		} catch (Throwable e) {
			logger.fatal("# FAILURE: couldn't delete an object", e);
		}
	}
}
