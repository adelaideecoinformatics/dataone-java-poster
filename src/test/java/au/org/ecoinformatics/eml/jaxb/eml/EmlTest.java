package au.org.ecoinformatics.eml.jaxb.eml;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasFirstI18NContent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.DatasetType;
import au.org.ecoinformatics.eml.jaxb.eml.Eml;
import au.org.ecoinformatics.eml.jaxb.eml.I18NNonEmptyStringType;

public class EmlTest {

	/**
	 * Can we build the expected EML document?
	 */
	@Test
	public void testBuild01() {
		String packageId = "someId";
		Eml result = new Eml()
			.withPackageId(packageId)
			.withDataset(new DatasetType().withTitle(new I18NNonEmptyStringType().withContent("some dataset")));
		assertThat(result.getPackageId(), is("someId"));
		assertThat(result.getDataset().getTitle(), hasFirstI18NContent("some dataset"));
	}
}
