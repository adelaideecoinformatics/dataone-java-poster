package au.org.ecoinformatics.d1.poster.rigs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.dataone.client.MNode;
import org.dataone.service.types.v1.Identifier;

/**
 * This is NOT a test. This is a rig to connect to the live DataONE instance
 * and *delete* objects by a specified ID. THINGS WILL BE DESTROYED, so be careful.
 * 
 * Build a list of identifiers using the {@link FindIdsRig} and copy-paste
 * them into the static initialiser for the IDS_TO_DELETE.
 * 
 * You'll need to have the X509 certificate which you get by running build-certificate.sh.
 */
public class DeleteByIdRig {
	
	private static final Logger logger = Logger.getLogger(DeleteByIdRig.class);
	
	// Edit these if required \/
	private static final String NODE_ENDPOINT = "https://dataone.ecoinformatics.org.au/mn";
	private static final List<String> IDS_TO_DELETE = new ArrayList<String>();
	static {
		IDS_TO_DELETE.addAll(Arrays.asList(
			new String[] {
				"ltern.21.25",
				"bradford.5.20",
				"ellsworth.6.9",
				"fahmi.30.12"
			}
		));
	}
	// Edit these if required /\
	
	private MNode nodeClient;

	private void run() throws Throwable {
		setup();
		for (String currIdentifier : IDS_TO_DELETE) {
			doDelete(currIdentifier);
		}
		logger.info("Finished. And as always, have nice day");
	}
	
	private void setup() throws Throwable {
		logger.info("Connecting to the endpoint: " + NODE_ENDPOINT);
		nodeClient = new MNode(NODE_ENDPOINT);
		nodeClient.ping();
	}

	private void doDelete(String identifierText) throws Throwable {
		Identifier pid = new Identifier();
		pid.setValue(identifierText);
		logger.info("Deleting object with identifier: " + identifierText);
		nodeClient.delete(pid);
	}
	
	public static void main(String[] args) {
		try {
			new DeleteByIdRig().run();
		} catch (Throwable e) {
			logger.fatal("FAILURE: couldn't delete an object. Giving up on entire queue.", e);
		}
	}
}
