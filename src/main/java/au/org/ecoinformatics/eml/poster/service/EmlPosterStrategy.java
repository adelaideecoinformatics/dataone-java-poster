package au.org.ecoinformatics.eml.poster.service;

import java.io.InputStream;

import org.dataone.client.v1.MNode;
import org.dataone.service.types.v1.SystemMetadata;

public interface EmlPosterStrategy {

	void execute(SystemMetadata sysmetaData, InputStream emlData, MNode nodeClient);

}
