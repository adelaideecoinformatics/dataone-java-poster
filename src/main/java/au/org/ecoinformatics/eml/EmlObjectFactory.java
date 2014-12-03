package au.org.ecoinformatics.eml;

import au.org.ecoinformatics.eml.jaxb.DatasetType;
import au.org.ecoinformatics.eml.jaxb.I18NNonEmptyStringType;

public class EmlObjectFactory {

	public DatasetType dataset(String datasetTitle) {
		DatasetType result = new DatasetType();
		result.getTitle().add(i18nString(datasetTitle));
		return result;
	}

	/**
	 * Helper for creating a I18NNonEmptyStringType
	 *
	 * @param text	content for the I18NNonEmptyStringType object
	 * @return		a new instance with the supplied content
	 */
	I18NNonEmptyStringType i18nString(String text) {
		I18NNonEmptyStringType result = new I18NNonEmptyStringType();
		result.getContent().add(text);
		return result;
	}

}
