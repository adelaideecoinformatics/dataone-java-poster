package au.org.ecoinformatics.d1.poster.service;

import java.io.InputStream;

import org.dataone.client.MNode;
import org.dataone.service.types.v1.SystemMetadata;

public interface DataonePosterStrategy {

	/**
	 * Performs the operation of this object using the supplied data
	 * 
	 * @param sysmetaData	sysmeta record
	 * @param objectData	stream of data for the object
	 * @param nodeClient	client to use for communication with the DataONE server
	 * @throws EcoinformaticsDataonePosterException	when things go wrong
	 */
	void execute(SystemMetadata sysmetaData, InputStream objectData, MNode nodeClient) throws EcoinformaticsDataonePosterException;
}
