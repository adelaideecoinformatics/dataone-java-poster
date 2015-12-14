package au.org.aekos.sysmetagen.service;

import java.nio.file.Path;

public interface SysMetaService {

	/**
	 * Creates sysmeta metadata for the single supplied file.
	 * 
	 * @param file	path to file to process
	 */
	void doGenerateForFile(Path file);

	/**
	 * Creates system metadata for all the supported files in the supplied directories.
	 * 
	 * @param directory	path to directory to process
	 */
	void doGenerateForDirectory(Path directory);
}
