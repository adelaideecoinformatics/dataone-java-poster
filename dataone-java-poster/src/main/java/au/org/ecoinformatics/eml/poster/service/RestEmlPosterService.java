package au.org.ecoinformatics.eml.poster.service;

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

public class RestEmlPosterService implements EmlPosterService {

	private static final Logger logger = LoggerFactory.getLogger(RestEmlPosterService.class);
	private static final String HARDCODED_SYSMETA_SUFFIX = "-sysmeta";
	private String nodeEndpoint;
	private MNode nodeClient;
	private String operation;
	private Map<String, EmlPosterStrategy> operationStrategies;
	private int filesProcessed = 0;
	
	@Override
	public void doPostForWholeDirectory(String directoryPath) throws EcoinformaticsEmlPosterException {
		FilenameFilter filter = new SuffixFileFilter(".xml");
		Path directory = Paths.get(directoryPath);
		connectToNode();
		for (File currFile : directory.toFile().listFiles(filter)) {
			String emlFilePath = currFile.getAbsolutePath();
			String smdFilePath = calculateSmdFilePath(emlFilePath);
			// TODO add switch to push past errors and handle exceptions here
			DataoneRecord record = readRecord(emlFilePath, smdFilePath);
			postRecord(record);
			filesProcessed++;
		}
		writeStats();
	}

	@Override
	public void doPostForSingleRecord(String emlFilePath, String smdFilePath) throws EcoinformaticsEmlPosterException {
		DataoneRecord record = readRecord(emlFilePath, smdFilePath);
		connectToNode();
		postRecord(record);
		filesProcessed++;
		writeStats();
	}
	
	private DataoneRecord readRecord(String emlFilePath, String smdFilePath) throws EcoinformaticsEmlPosterException {
		logger.info("EML Poster: reading EML metadata file " + emlFilePath);
		try {
			FileInputStream emlData = new FileInputStream(emlFilePath);
			SystemMetadata sysmetaData = unMarshalSystemMetadata(smdFilePath);
			return new DataoneRecord(emlData, emlFilePath, sysmetaData);
		} catch (FileNotFoundException e) {
			throw new EcoinformaticsEmlPosterException("Failed to load the EML file: " + emlFilePath, e);
		}
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

	private void postRecord(DataoneRecord record) throws EcoinformaticsEmlPosterException {
		EmlPosterStrategy selectedStrategy = operationStrategies.get(operation);
		if (selectedStrategy == null) {
			logger.warn(String.format("Warning: could NOT find a strategy for operation %s, available operations are: %s", operation, getAvailableOperations()));
		}
		selectedStrategy.execute(record.sysmetaData, record.emlData, nodeClient);
		try {
			record.emlData.close();
		} catch (IOException e) {
			throw new EcoinformaticsEmlPosterException("Runtime error: failed to close EML file: " + record.emlFilePath, e);
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
		logger.info(String.format("Processed %s files", filesProcessed));
		logger.info("Finished POSTing metadata");
	}

	private String calculateSmdFilePath(String emlFilePath) {
		// FIXME I'm sure other people would appreciate more flexibility here.
		// Some sort of pattern language to describe how the files are paired based on name
		return emlFilePath + HARDCODED_SYSMETA_SUFFIX;
	}
	
	public void setOperation(String operation) {
		this.operation = operation;
	}

	public void setNodeEndpoint(String nodeEndpoint) {
		this.nodeEndpoint = nodeEndpoint;
	}

	public void setOperationStrategies(Map<String, EmlPosterStrategy> operationStrategies) {
		this.operationStrategies = operationStrategies;
	}
	
	private static class DataoneRecord {
		private final InputStream emlData;
		private final String emlFilePath;
		private final SystemMetadata sysmetaData;
		
		public DataoneRecord(InputStream emlData, String emlFilePath, SystemMetadata sysmetaData) {
			this.emlData = emlData;
			this.emlFilePath = emlFilePath;
			this.sysmetaData = sysmetaData;
		}
	}
}
