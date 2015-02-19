package au.org.ecoinformatics.eml;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.DatasetType;
import au.org.ecoinformatics.eml.jaxb.eml.Eml;
import au.org.ecoinformatics.eml.jaxb.eml.I18NNonEmptyStringType;

public class JaxbPrettyPrinterTest {
	/**
	 * Can we pretty print a basic EML document?
	 */
	@Test
	public void testPrettyPrint01() {
		Eml eml = new Eml()
			.withDataset(new DatasetType()
				.withId("package1")
				.withTitle(new I18NNonEmptyStringType().withContent("fantastic dataset")));
		JaxbPrettyPrinter<Eml> objectUnderTest = JaxbPrettyPrinter.getEmlJaxbPrettyPrinterInstance();
		OutputStream out = new ByteArrayOutputStream();
		objectUnderTest.prettyPrint(eml, out);
		assertTrue(out.toString().startsWith(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
				"<eml:eml"));
		assertTrue(out.toString().endsWith(
				"    <dataset id=\"package1\">\n" +
				"        <title>fantastic dataset</title>\n" +
				"    </dataset>\n" +
				"</eml:eml>\n"));
	}
}
