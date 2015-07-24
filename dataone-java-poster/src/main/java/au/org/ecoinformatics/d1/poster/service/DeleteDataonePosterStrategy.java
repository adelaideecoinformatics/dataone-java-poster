package au.org.ecoinformatics.d1.poster.service;

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

public class DeleteDataonePosterStrategy implements DataonePosterStrategy {

	private static final Logger logger = LoggerFactory.getLogger(DeleteDataonePosterStrategy.class);
	
	@Override
	public void execute(SystemMetadata sysmetaData, InputStream objectData, MNode nodeClient) throws EcoinformaticsDataonePosterException {
		try {
			Identifier pid = sysmetaData.getIdentifier();
			logger.info("DataONE Poster: performing a 'delete' operation for pid: " + pid.getValue());
			nodeClient.delete(pid);
		} catch (InvalidToken | ServiceFailure e) {
			throw new EcoinformaticsDataonePosterException("Runtime error: failed to POST to the dataONE node", e);
		} catch (NotAuthorized e) {
			throw new EcoinformaticsDataonePosterException("Runtime error: authorisation failure", e);
		} catch (NotImplemented e) {
			throw new EcoinformaticsDataonePosterException("Runtime error: requested operation isn't available on the server", e);
		} catch (NotFound e) {
			throw new EcoinformaticsDataonePosterException("Runtime error: failed to find existing object to update", e);
		}
	}
}
