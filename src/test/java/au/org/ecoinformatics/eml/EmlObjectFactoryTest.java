package au.org.ecoinformatics.eml;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasI18NContent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.DatasetType;
import au.org.ecoinformatics.eml.jaxb.I18NNonEmptyStringType;

public class EmlObjectFactoryTest {

	/**
	 * Can we generate a dataset object?
	 */
	@Test
	public void testDataset01() {
		EmlObjectFactory objectUnderTest = new EmlObjectFactory();
		String datasetTitle = "some dataset title";
		DatasetType result = objectUnderTest.dataset(datasetTitle);
		assertThat(result.getTitle().get(0).getContent().get(0).toString(), is("some dataset title"));
		assertThat(result.getTitle(), hasI18NContent("some dataset title"));
	}

	/**
	 * Can we create an I18N string with valid content?
	 */
	@Test
	public void testI18nString01() {
		EmlObjectFactory objectUnderTest = new EmlObjectFactory();
		String someText = "wombat";
		I18NNonEmptyStringType result = objectUnderTest.i18nString(someText);
		assertThat(result.getContent().get(0).toString(), is("wombat"));
	}
}
