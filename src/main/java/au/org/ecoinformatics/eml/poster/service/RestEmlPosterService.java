package au.org.ecoinformatics.eml.poster.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.dataone.client.D1Client;
import org.dataone.client.MNode;
import org.dataone.service.exceptions.InsufficientResources;
import org.dataone.service.exceptions.NotImplemented;
import org.dataone.service.exceptions.ServiceFailure;
import org.dataone.service.types.v1.SystemMetadata;
import org.dataone.service.util.TypeMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestEmlPosterService implements EmlPosterService {

	private static final Logger logger = LoggerFactory.getLogger(RestEmlPosterService.class);
	private String nodeEndpoint;
	private MNode nodeClient;
	private String systemMetadataFilename;
	private String emlFilename;
	private String operation;
	private Map<String, EmlPosterStrategy> operationStrategies;
	
	private InputStream emlData;
	private SystemMetadata sysmetaData;
	
	@Override
	public void doPost() throws EcoinformaticsEmlPosterException {
		readMetadataFiles();
		connectToNode();
		postMetadata();
		writeStats();
	}
	
	private void readMetadataFiles() throws EcoinformaticsEmlPosterException {
		logger.info("EML Poster: reading EML metadata file " + emlFilename);
		try {
			emlData = new FileInputStream(emlFilename);
		} catch (FileNotFoundException e) {
			throw new EcoinformaticsEmlPosterException("Failed to load the EML file: " + emlFilename, e);
		}
		sysmetaData = unMarshalSystemMetadata(systemMetadataFilename);
	}

	private void connectToNode() throws EcoinformaticsEmlPosterException {
		logger.info("EML Poster: connecting to node endpoint [" + nodeEndpoint + "]");
		try {
			nodeClient = D1Client.getMN(nodeEndpoint);
			nodeClient.ping();
		} catch (ServiceFailure e) {
			throw new EcoinformaticsEmlPosterException("Runtime error: Failed to build a dataONE client.", e);
		} catch (NotImplemented e) {
			throw new EcoinformaticsEmlPosterException("Runtime error: Failed when testing the connection to the dataONE node.", e);
		} catch (InsufficientResources e) {
			throw new EcoinformaticsEmlPosterException("Runtime error: Failed to ping dataONE node.", e);
		}
	}

	private void postMetadata() throws EcoinformaticsEmlPosterException {
		EmlPosterStrategy selectedStrategy = operationStrategies.get(operation);
		if (selectedStrategy == null) {
			logger.warn(String.format("Warning: could NOT find a strategy for operation %s, available operations are: %s", operation, getAvailableOperations()));
		}
		selectedStrategy.execute(sysmetaData, emlData, nodeClient);
		try {
			emlData.close();
		} catch (IOException e) {
			throw new EcoinformaticsEmlPosterException("Runtime error: failed to close EML file: " + emlFilename, e);
		}
	}
	
	String getAvailableOperations() {
		StringBuilder result = new StringBuilder();;
		for (String currKey : operationStrategies.keySet()) {
			if (result.length() > 0) {
				result.append(", ");
			}
			result.append(currKey);
		}
		return result.toString();
	}

	private SystemMetadata unMarshalSystemMetadata(String filePath) throws EcoinformaticsEmlPosterException {
		// See: https://mule1.dataone.org/ArchitectureDocs-current/design/SystemMetadata.html for what is required in sysmeta
        try {
            logger.info("EML Poster: reading sysmeta file " + filePath);
        	InputStream is = new FileInputStream(filePath);
            return TypeMarshaller.unmarshalTypeFromStream(SystemMetadata.class, is);
        } catch (Exception e) {
        	throw new EcoinformaticsEmlPosterException("Failed to read sysmeta from '" + filePath + "'.", e);
        }
    }
	
	private void writeStats() {
		logger.info("Finished POSTing metadata");
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public void setNodeEndpoint(String nodeEndpoint) {
		this.nodeEndpoint = nodeEndpoint;
	}

	public void setSystemMetadataFilename(String systemMetadataFilename) {
		this.systemMetadataFilename = systemMetadataFilename;
	}

	public void setEmlFilename(String emlFilename) {
		this.emlFilename = emlFilename;
	}

	public void setOperationStrategies(Map<String, EmlPosterStrategy> operationStrategies) {
		this.operationStrategies = operationStrategies;
	}
}
