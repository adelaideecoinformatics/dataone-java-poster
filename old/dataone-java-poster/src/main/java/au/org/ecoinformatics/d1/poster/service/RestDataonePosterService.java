package au.org.ecoinformatics.d1.poster.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.dataone.client.D1Client;
import org.dataone.client.MNode;
import org.dataone.service.exceptions.InsufficientResources;
import org.dataone.service.exceptions.NotImplemented;
import org.dataone.service.exceptions.ServiceFailure;
import org.dataone.service.types.v1.SystemMetadata;
import org.dataone.service.util.TypeMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestDataonePosterService implements DataonePosterService {

	private static final Logger logger = LoggerFactory.getLogger(RestDataonePosterService.class);
	private static final String HARDCODED_SYSMETA_SUFFIX = "-sysmeta";
	private static final String TARGET_FILE_TYPE_SUFFIX = ".xml";
	private String nodeEndpoint;
	private MNode nodeClient;
	private String operation;
	private Map<String, DataonePosterStrategy> operationStrategies;
	private int filesProcessed = 0;
	private int errorsEncountered = 0;
	
	@Override
	public void doPostForWholeDirectory(String directoryPath) throws EcoinformaticsDataonePosterException {
		FilenameFilter filter = new SuffixFileFilter(TARGET_FILE_TYPE_SUFFIX);
		Path directory = Paths.get(directoryPath);
		connectToNode();
		for (File currFile : directory.toFile().listFiles(filter)) {
			String objectFilePath = currFile.getAbsolutePath();
			String sysmetaFilePath = calculateSmdFilePath(objectFilePath);
			// TODO add switch to push past errors and handle exceptions here
			DataoneRecord record = readRecord(objectFilePath, sysmetaFilePath);
			boolean success = postRecord(record);
			if (success) {
				filesProcessed++;
				continue;
			}
			errorsEncountered++;
		}
		writeStats();
	}

	@Override
	public void doPostForSingleRecord(String objectFilePath, String sysmetaFilePath) throws EcoinformaticsDataonePosterException {
		DataoneRecord record = readRecord(objectFilePath, sysmetaFilePath);
		connectToNode();
		boolean success = postRecord(record);
		if (success) {
			filesProcessed++;
		} else {
			errorsEncountered++;
		}
		writeStats();
	}
	
	private DataoneRecord readRecord(String objectFilePath, String sysmetaFilePath) throws EcoinformaticsDataonePosterException {
		logger.info("DataONE Poster: reading object file " + objectFilePath);
		try {
			FileInputStream objectData = new FileInputStream(objectFilePath);
			SystemMetadata sysmetaData = unMarshalSystemMetadata(sysmetaFilePath);
			return new DataoneRecord(objectData, objectFilePath, sysmetaData);
		} catch (FileNotFoundException e) {
			throw new EcoinformaticsDataonePosterException("Failed to load the Object file: " + objectFilePath, e);
		}
	}

	private void connectToNode() throws EcoinformaticsDataonePosterException {
		logger.info("DataONE Poster: connecting to node endpoint [" + nodeEndpoint + "]");
		try {
			nodeClient = D1Client.getMN(nodeEndpoint);
			nodeClient.ping();
		} catch (ServiceFailure e) {
			throw new EcoinformaticsDataonePosterException("Runtime error: Failed to build a dataONE client.", e);
		} catch (NotImplemented e) {
			throw new EcoinformaticsDataonePosterException("Runtime error: Failed when testing the connection to the dataONE node.", e);
		} catch (InsufficientResources e) {
			throw new EcoinformaticsDataonePosterException("Runtime error: Failed to ping dataONE node.", e);
		}
	}

	private boolean postRecord(DataoneRecord record) throws EcoinformaticsDataonePosterException {
		DataonePosterStrategy selectedStrategy = operationStrategies.get(operation);
		if (selectedStrategy == null) {
			logger.warn(String.format("Warning: could NOT find a strategy for operation %s, available operations are: %s", operation, getAvailableOperations()));
			return false;
		}
		try {
			return selectedStrategy.execute(record.sysmetaData, record.objectData, nodeClient);
		} catch (EcoinformaticsDataonePosterException e) {
			logger.error("Failed to post record with ID: " + record.getId(), e);
			return false;
		} finally {
			try {
				record.objectData.close();
			} catch (IOException e) {
				logger.warn("Runtime error: failed to close Object file: " + record.objectFilePath, e);
			}
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

	private SystemMetadata unMarshalSystemMetadata(String filePath) throws EcoinformaticsDataonePosterException {
		// See: https://mule1.dataone.org/ArchitectureDocs-current/design/SystemMetadata.html for what is required in sysmeta
        try {
            logger.info("DataONE Poster: reading sysmeta file " + filePath);
        	InputStream is = new FileInputStream(filePath);
            return TypeMarshaller.unmarshalTypeFromStream(SystemMetadata.class, is);
        } catch (Exception e) {
        	throw new EcoinformaticsDataonePosterException("Failed to read sysmeta from '" + filePath + "'.", e);
        }
    }
	
	private void writeStats() {
		logger.info(String.format("Processed %s files", filesProcessed));
		boolean errorsWereEncountered = errorsEncountered > 0;
		if (errorsWereEncountered) {
			logger.warn(String.format("WARNING: %s errors were countered, check the log for more details.", errorsEncountered));
		}
		logger.info("Finished POSTing metadata");
	}

	private String calculateSmdFilePath(String objectFilePath) {
		// FIXME I'm sure other people would appreciate more flexibility here.
		// Some sort of pattern language to describe how the files are paired based on name
		return objectFilePath + HARDCODED_SYSMETA_SUFFIX;
	}
	
	public void setOperation(String operation) {
		this.operation = operation;
	}

	public void setNodeEndpoint(String nodeEndpoint) {
		this.nodeEndpoint = nodeEndpoint;
	}

	public void setOperationStrategies(Map<String, DataonePosterStrategy> operationStrategies) {
		this.operationStrategies = operationStrategies;
	}
	
	private static class DataoneRecord {
		private final InputStream objectData;
		private final String objectFilePath;
		private final SystemMetadata sysmetaData;
		
		public DataoneRecord(InputStream objectData, String objectFilePath, SystemMetadata sysmetaData) {
			this.objectData = objectData;
			this.objectFilePath = objectFilePath;
			this.sysmetaData = sysmetaData;
		}
		
		public String getId() {
			return sysmetaData.getIdentifier().getValue();
		}
	}
}
