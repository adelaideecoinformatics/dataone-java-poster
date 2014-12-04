package au.org.ecoinformatics.eml;

import org.junit.Test;

import au.org.ecoinformatics.eml.builder.DatasetTypeBuilder;
import au.org.ecoinformatics.eml.builder.EmlBuilder;
import au.org.ecoinformatics.eml.jaxb.Eml;

public class EmlPrettyPrinterTest {

	/**
	 * Can we pretty print a basic EML document?
	 */
	@Test
	public void testPrettyPrint01() {
		EmlBuilder emlBuilder = new EmlBuilder("package1");
		emlBuilder.dataset(new DatasetTypeBuilder().datasetTitle("fantastic dataset").build());
		Eml eml = emlBuilder.build();
		EmlPrettyPrinter objectUnderTest = new EmlPrettyPrinter();
		objectUnderTest.prettyPrint(eml, System.out);
	}
}
