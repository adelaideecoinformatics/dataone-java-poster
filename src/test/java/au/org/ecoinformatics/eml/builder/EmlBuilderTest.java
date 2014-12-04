package au.org.ecoinformatics.eml.builder;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasFirstI18NContent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.Eml;

public class EmlBuilderTest {

	/**
	 * Can we build the expected EML document?
	 */
	@Test
	public void testBuild01() {
		String packageTitle = "someId";
		EmlBuilder objectUnderTest = new EmlBuilder(packageTitle);
		objectUnderTest.dataset(new DatasetTypeBuilder().datasetTitle("some dataset").build());
		Eml result = objectUnderTest.build();
		assertThat(result.getPackageId(), is("someId"));
		assertThat(result.getDataset().getTitle(), hasFirstI18NContent("some dataset"));
	}
}
