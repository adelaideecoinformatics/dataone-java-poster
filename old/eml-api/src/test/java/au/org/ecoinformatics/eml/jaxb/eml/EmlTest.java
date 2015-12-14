package au.org.ecoinformatics.eml.jaxb.eml;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasFirstI18NContent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.EmlValidationException;

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
	
	/**
	 * Can we validate the bare minimum conformant EML document?
	 */
	@Test
	public void testValidate01() throws EmlValidationException {
		Eml objectUnderTest = new Eml()
			.withPackageId("some.package.123")
			.withSystem("aekos")
			.withDataset(new DatasetType()
				.withTitle(i18n("some dataset"))
				.withCreator(new ResponsibleParty()
					.withPositionName(i18n("senior creator")))
				.withContact(new ResponsibleParty()
					.withPositionName(i18n("contact officer")))
		);
		objectUnderTest.validate();
	}

	private I18NNonEmptyStringType i18n(String content) {
		return new I18NNonEmptyStringType().withContent(content);
	}
}
