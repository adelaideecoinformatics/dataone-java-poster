package au.org.ecoinformatics.d1.poster.service;

public interface DataonePosterService {

	/**
	 * Read the found records in a directory and POST them to the member node
	 * @param directoryPath 	path to the directory to read records from
	 * 
	 * @throws EcoinformaticsDataonePosterException when something goes wrong
	 */
	void doPostForWholeDirectory(String directoryPath) throws EcoinformaticsDataonePosterException;;

	/**
	 * Read single record (pair of files) and POST them to the member node
	 * @param objectFilePath 	path to the object file
	 * @param sysmetaFilePath 	path to the SysMeta file
	 * 
	 * @throws EcoinformaticsDataonePosterException when something goes wrong
	 */
	void doPostForSingleRecord(String objectFilePath, String sysmetaFilePath) throws EcoinformaticsDataonePosterException;;
}
