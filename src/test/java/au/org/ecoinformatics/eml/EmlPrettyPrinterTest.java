package au.org.ecoinformatics.eml;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.DatasetType;
import au.org.ecoinformatics.eml.jaxb.eml.Eml;
import au.org.ecoinformatics.eml.jaxb.eml.I18NNonEmptyStringType;

public class EmlPrettyPrinterTest {

	/**
	 * Can we pretty print a basic EML document?
	 */
	@Test
	public void testPrettyPrint01() {
		Eml eml = new Eml()
			.withDataset(new DatasetType()
				.withId("package1")
				.withTitle(new I18NNonEmptyStringType().withContent("fantastic dataset")));
		EmlPrettyPrinter objectUnderTest = new EmlPrettyPrinter();
		objectUnderTest.prettyPrint(eml, System.out);
	}
}
