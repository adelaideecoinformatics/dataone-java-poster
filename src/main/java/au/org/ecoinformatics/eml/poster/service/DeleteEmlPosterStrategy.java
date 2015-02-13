package au.org.ecoinformatics.eml.poster.service;

import java.io.InputStream;

import org.dataone.client.v1.MNode;
import org.dataone.service.exceptions.InvalidToken;
import org.dataone.service.exceptions.NotAuthorized;
import org.dataone.service.exceptions.NotFound;
import org.dataone.service.exceptions.NotImplemented;
import org.dataone.service.exceptions.ServiceFailure;
import org.dataone.service.types.v1.Identifier;
import org.dataone.service.types.v1.SystemMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteEmlPosterStrategy implements EmlPosterStrategy {

	private static final Logger logger = LoggerFactory.getLogger(DeleteEmlPosterStrategy.class);
	
	@Override
	public void execute(SystemMetadata sysmetaData, InputStream emlData, MNode nodeClient) {
		try {
			Identifier pid = sysmetaData.getIdentifier();
			logger.info("EML Poster: performing a 'delete' operation for pid: " + pid);
			nodeClient.delete(pid);
		} catch (InvalidToken e) {
			logger.error("Runtime error: failed to POST to the dataONE node", e);
		} catch (NotAuthorized e) {
			logger.error("Runtime error: failed to POST to the dataONE node", e);
		} catch (NotImplemented e) {
			logger.error("Runtime error: failed to POST to the dataONE node", e);
		} catch (ServiceFailure e) {
			logger.error("Runtime error: failed to POST to the dataONE node", e);
		} catch (NotFound e) {
			logger.error("Runtime error: failed to find existing object to update", e);
		}
	}
}
