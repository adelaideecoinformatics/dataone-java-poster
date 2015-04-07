package au.org.ecoinformatics.eml.poster.service;

import java.io.InputStream;

import org.dataone.client.MNode;
import org.dataone.service.exceptions.IdentifierNotUnique;
import org.dataone.service.exceptions.InsufficientResources;
import org.dataone.service.exceptions.InvalidRequest;
import org.dataone.service.exceptions.InvalidSystemMetadata;
import org.dataone.service.exceptions.InvalidToken;
import org.dataone.service.exceptions.NotAuthorized;
import org.dataone.service.exceptions.NotImplemented;
import org.dataone.service.exceptions.ServiceFailure;
import org.dataone.service.exceptions.UnsupportedType;
import org.dataone.service.types.v1.Identifier;
import org.dataone.service.types.v1.SystemMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateEmlPosterStrategy implements EmlPosterStrategy {

	private static final Logger logger = LoggerFactory.getLogger(CreateEmlPosterStrategy.class);
	
	@Override
	public void execute(SystemMetadata sysmetaData, InputStream emlData, MNode nodeClient) {
		logger.info("EML Poster: performing a 'create' operation");
		try {
			Identifier pid = sysmetaData.getIdentifier();
			nodeClient.create(pid, emlData, sysmetaData);
		} catch (IdentifierNotUnique e) {
			logger.error("Runtime error: failed to POST to the dataONE node", e);
		} catch (InsufficientResources e) {
			logger.error("Runtime error: failed to POST to the dataONE node", e);
		} catch (InvalidRequest e) {
			logger.error("Runtime error: failed to POST to the dataONE node", e);
		} catch (InvalidSystemMetadata e) {
			logger.error("Runtime error: failed to POST to the dataONE node", e);
		} catch (InvalidToken e) {
			logger.error("Runtime error: failed to POST to the dataONE node", e);
		} catch (NotAuthorized e) {
			logger.error("Runtime error: failed to POST to the dataONE node", e);
		} catch (NotImplemented e) {
			logger.error("Runtime error: failed to POST to the dataONE node", e);
		} catch (ServiceFailure e) {
			logger.error("Runtime error: failed to POST to the dataONE node", e);
		} catch (UnsupportedType e) {
			logger.error("Runtime error: failed to POST to the dataONE node", e);
		}
	}
}
