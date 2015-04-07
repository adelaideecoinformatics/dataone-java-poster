package au.org.ecoinformatics.eml.poster.service;

public interface EmlPosterService {

	/**
	 * Read the required files and POST them to the member node
	 * @throws EcoinformaticsEmlPosterException when something goes wrong
	 */
	void doPost() throws EcoinformaticsEmlPosterException;
}
