package au.org.ecoinformatics.eml.poster.service;

import java.io.InputStream;

import org.dataone.client.MNode;
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
	public void execute(SystemMetadata sysmetaData, InputStream emlData, MNode nodeClient) throws EcoinformaticsEmlPosterException {
		try {
			Identifier pid = sysmetaData.getIdentifier();
			logger.info("EML Poster: performing a 'delete' operation for pid: " + pid.getValue());
			nodeClient.delete(pid);
		} catch (InvalidToken | ServiceFailure e) {
			throw new EcoinformaticsEmlPosterException("Runtime error: failed to POST to the dataONE node", e);
		} catch (NotAuthorized e) {
			throw new EcoinformaticsEmlPosterException("Runtime error: authorisation failure", e);
		} catch (NotImplemented e) {
			throw new EcoinformaticsEmlPosterException("Runtime error: requested operation isn't available on the server", e);
		} catch (NotFound e) {
			throw new EcoinformaticsEmlPosterException("Runtime error: failed to find existing object to update", e);
		}
	}
}
