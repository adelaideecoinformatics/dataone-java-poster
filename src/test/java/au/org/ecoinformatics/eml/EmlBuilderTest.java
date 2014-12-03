package au.org.ecoinformatics.eml;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasI18NContent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.Eml;

public class EmlBuilderTest {

	private EmlObjectFactory f = new EmlObjectFactory();

	/**
	 * Can we build the expected EML document?
	 */
	@Test
	public void testBuild01() {
		String packageTitle = "someId";
		EmlBuilder objectUnderTest = new EmlBuilder(packageTitle);
		objectUnderTest.dataset(f.dataset("some dataset"));
		Eml result = objectUnderTest.build();
		assertThat(result.getPackageId(), is("someId"));
		assertThat(result.getDataset().getTitle(), hasI18NContent("some dataset"));
	}
}
