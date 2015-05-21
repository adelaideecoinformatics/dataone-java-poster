package au.org.ecoinformatics.eml.poster.service;

public interface EmlPosterService {

	/**
	 * Read the found records in a directory and POST them to the member node
	 * @param directoryPath 	path to the directory to read records from
	 * 
	 * @throws EcoinformaticsEmlPosterException when something goes wrong
	 */
	void doPostForWholeDirectory(String directoryPath) throws EcoinformaticsEmlPosterException;;

	/**
	 * Read single record (pair of files) and POST them to the member node
	 * @param emlFilePath 	path to the EML file
	 * @param smdFilePath 	path to the SysMeta file
	 * 
	 * @throws EcoinformaticsEmlPosterException when something goes wrong
	 */
	void doPostForSingleRecord(String emlFilePath, String smdFilePath) throws EcoinformaticsEmlPosterException;;
}
