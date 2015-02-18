package au.org.ecoinformatics.eml;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.Coverage;
import au.org.ecoinformatics.eml.jaxb.eml.DatasetType;
import au.org.ecoinformatics.eml.jaxb.eml.DistributionType;
import au.org.ecoinformatics.eml.jaxb.eml.Eml;
import au.org.ecoinformatics.eml.jaxb.eml.I18NNonEmptyStringType;
import au.org.ecoinformatics.eml.jaxb.eml.MaintenanceType;
import au.org.ecoinformatics.eml.jaxb.eml.OnlineType;
import au.org.ecoinformatics.eml.jaxb.eml.ProtocolType.KeywordSet;
import au.org.ecoinformatics.eml.jaxb.eml.ProtocolType.KeywordSet.Keyword;
import au.org.ecoinformatics.eml.jaxb.eml.ResponsibleParty;
import au.org.ecoinformatics.eml.jaxb.eml.TextType;
import au.org.ecoinformatics.eml.jaxb.eml.UrlType;
import au.org.ecoinformatics.eml.jaxb.eml.ViewType.References;

public class EmlValidatorTest {

	/**
	 * Is an empty EML document invalid?
	 */
	@Test(expected=EmlValidationException.class)
	public void testValidate01() throws EmlValidationException {
		EmlValidator objectUnderTest = new EmlValidator();
		Eml eml = new Eml();
		objectUnderTest.validate(eml);
	}

	/**
	 * Can we validate the bare minimum conformant EML document?
	 */
	@Test
	public void testValidate02() throws EmlValidationException {
		EmlValidator objectUnderTest = new EmlValidator();
		Eml eml = new Eml()
			.withPackageId("some.package.123")
			.withSystem("aekos")
			.withDataset(new DatasetType()
				.withTitle(i18n("some dataset"))
				.withCreator(new ResponsibleParty()
					.withPositionName(i18n("senior creator")))
				.withContact(new ResponsibleParty()
					.withPositionName(i18n("contact officer")))
		);
		objectUnderTest.validate(eml);
	}
	
	/**
	 * Can we validate and EML document with more than the bare minimum?
	 */
	@Test
	public void testValidate03() throws EmlValidationException {
		EmlValidator objectUnderTest = new EmlValidator();
		Eml eml = new Eml()
			.withPackageId("some.package.123")
			.withSystem("aekos")
			.withDataset(new DatasetType()
				.withTitle(i18n("some dataset"))
				.withCreator(new ResponsibleParty()
					.withPositionName(i18n("senior creator")))
				.withMetadataProvider(new ResponsibleParty()
					.withPositionName(i18n("junior metadata provider")))
				.withLanguage(i18n("english"))
				.withSeries("1")
				.withAbstract(tt("more information about this metadata")) // FIXME might need Section or Para to be supplied here
				.withKeywordSet(new KeywordSet()
					.withKeyword(new Keyword()
						.withContent("keyword1")))
				.withAdditionalInfo(tt("but wait, there's more!"))
				.withIntellectualRights(tt("all your IP are belong to us"))
				.withDistribution(new DistributionType()
					.withOnline(new OnlineType()
						.withUrl(new UrlType()
							.withValue("http://aekos.org.au/"))))
				.withCoverage(new Coverage()
					.withReferences(new References()
						.withValue("lot's of stuff is covered")))
				.withPurpose(tt("the boss said to do it"))
				.withMaintenance(new MaintenanceType()
					.withDescription(tt("check the oil every 1000kms")))
				.withContact(new ResponsibleParty() //
					.withPositionName(i18n("contact officer")))
		);
		objectUnderTest.validate(eml);
	}
	
	private TextType tt(String content) {
		return new TextType().withContent(content);
	}

	private I18NNonEmptyStringType i18n(String content) {
		return new I18NNonEmptyStringType().withContent(content);
	}
}
