package au.org.aekos.eml.poster.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.dataone.client.v1.MNode;
import org.dataone.client.v1.itk.D1Client;
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
import org.dataone.service.util.TypeMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestEmlPosterService implements EmlPosterService {

	private static final Logger logger = LoggerFactory.getLogger(RestEmlPosterService.class);
	private String metadataDirectoryPath;
	private String nodeEndpoint;
	private MNode nodeClient;
	private String systemMetadataFilename;
	private String emlFilename;
	
	@Override
	public void doPost() {
		readMetadataFiles();
		connectToNode();
		postMetadata();
		writeStats();
	}
	
	private void readMetadataFiles() {
		logger.info("EML Poster: reading metadata files");
		// FIXME dynamically load all file pairs from the directory
		systemMetadataFilename = "smd.xml";
		emlFilename = "eml.xml";
	}

	private void connectToNode() {
		logger.info("EML Poster: connecting to node endpoint [" + nodeEndpoint + "]");
		try {
			nodeClient = D1Client.getMN(nodeEndpoint);
		} catch (ServiceFailure e) {
			logger.error("Runtime error: Failed to build a dataONE client.", e);
		}
	}
	
	private void postMetadata() {
		logger.info("EML Poster: POSTing data");
		Identifier pid = new Identifier(); // TODO generate this ID dynamically
		pid.setValue("blah123");
		try {
			InputStream object = new FileInputStream(metadataDirectoryPath + File.separator + emlFilename);
			SystemMetadata sysmeta = unMarshalSystemMetadata(metadataDirectoryPath + File.separator + systemMetadataFilename);
			nodeClient.create(pid, object, sysmeta);
			object.close();
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
		} catch (FileNotFoundException e) {
			logger.error("Runtime error: failed to load the EML file from: " + emlFilename, e);
		} catch (IOException e) {
			logger.error("Runtime error: failed to close EML file: " + emlFilename, e);
		}
	}
	
	private SystemMetadata unMarshalSystemMetadata(String filePath) {
        SystemMetadata smd = null;
        try {
            InputStream is = new FileInputStream(filePath);
            smd = TypeMarshaller.unmarshalTypeFromStream(SystemMetadata.class, is);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return smd;
    }
	
	private void writeStats() {
		logger.info("Finished POSTing metadata");
	}

	public void setMetadataDirectoryPath(String metadataDirectoryPath) {
		this.metadataDirectoryPath = metadataDirectoryPath;
	}

	public void setNodeEndpoint(String nodeEndpoint) {
		this.nodeEndpoint = nodeEndpoint;
	}
}
