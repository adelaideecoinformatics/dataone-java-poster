package au.org.ecoinformatics.eml.builder;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasFirstI18NContent;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.DatasetType;

public class DatasetTypeBuilderTest {

	/**
	 * Can we build a dataset object with a title?
	 */
	@Test
	public void testDatasetTitle01() {
		DatasetTypeBuilder objectUnderTest = new DatasetTypeBuilder();
		String datasetTitle = "some dataset title";
		DatasetType result = objectUnderTest.datasetTitle(datasetTitle).build();
		assertThat(result.getTitle(), hasFirstI18NContent("some dataset title"));
	}
}
