package au.org.ecoinformatics.eml;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.DatasetType;
import au.org.ecoinformatics.eml.jaxb.eml.Eml;
import au.org.ecoinformatics.eml.jaxb.eml.I18NNonEmptyStringType;

public class JaxbPrettyPrinterTest {

	private static final String testPrettyPrint01_EXPECTATION = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
			"<eml:eml xmlns:sw=\"eml://ecoinformatics.org/software-2.1.1\" "
			+ "xmlns:eml=\"eml://ecoinformatics.org/eml-2.1.1\" "
			+ "xmlns:doc=\"eml://ecoinformatics.org/documentation-2.1.1\" "
			+ "xmlns:spref=\"eml://ecoinformatics.org/spatialReference-2.1.1\" "
			+ "xmlns:dat=\"eml://ecoinformatics.org/dataTable-2.1.1\" "
			+ "xmlns:ent=\"eml://ecoinformatics.org/entity-2.1.1\" "
			+ "xmlns:sv=\"eml://ecoinformatics.org/spatialVector-2.1.1\" "
			+ "xmlns:sr=\"eml://ecoinformatics.org/spatialRaster-2.1.1\" "
			+ "xmlns:txt=\"eml://ecoinformatics.org/text-2.1.1\" "
			+ "xmlns:md=\"eml://ecoinformatics.org/methods-2.1.1\" "
			+ "xmlns:rp=\"eml://ecoinformatics.org/party-2.1.1\" "
			+ "xmlns:att=\"eml://ecoinformatics.org/attribute-2.1.1\" "
			+ "xmlns:prot=\"eml://ecoinformatics.org/protocol-2.1.1\" "
			+ "xmlns:cit=\"eml://ecoinformatics.org/literature-2.1.1\" "
			+ "xmlns:acc=\"eml://ecoinformatics.org/access-2.1.1\" "
			+ "xmlns:v=\"eml://ecoinformatics.org/view-2.1.1\" "
			+ "xmlns:ds=\"eml://ecoinformatics.org/dataset-2.1.1\" "
			+ "xmlns:proj=\"eml://ecoinformatics.org/project-2.1.1\" "
			+ "xmlns:sp=\"eml://ecoinformatics.org/storedProcedure-2.1.1\" "
			+ "xmlns:phys=\"eml://ecoinformatics.org/physical-2.1.1\">\n" +
			"    <dataset id=\"package1\">\n" +
			"        <title>fantastic dataset</title>\n" +
			"    </dataset>\n" +
			"</eml:eml>\n";
	
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
		assertEquals(testPrettyPrint01_EXPECTATION, out.toString());
	}
}
