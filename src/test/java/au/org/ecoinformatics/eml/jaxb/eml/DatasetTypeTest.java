package au.org.ecoinformatics.eml.jaxb.eml;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasFirstI18NContent;
import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasI18NContent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.Coverage;
import au.org.ecoinformatics.eml.jaxb.eml.DatasetType;
import au.org.ecoinformatics.eml.jaxb.eml.DistributionType;
import au.org.ecoinformatics.eml.jaxb.eml.GeographicCoverage;
import au.org.ecoinformatics.eml.jaxb.eml.I18NNonEmptyStringType;
import au.org.ecoinformatics.eml.jaxb.eml.ObjectFactory;
import au.org.ecoinformatics.eml.jaxb.eml.OnlineType;
import au.org.ecoinformatics.eml.jaxb.eml.Person;
import au.org.ecoinformatics.eml.jaxb.eml.ProtocolType.KeywordSet;
import au.org.ecoinformatics.eml.jaxb.eml.ProtocolType.KeywordSet.Keyword;
import au.org.ecoinformatics.eml.jaxb.eml.ResponsibleParty;
import au.org.ecoinformatics.eml.jaxb.eml.TextType;
import au.org.ecoinformatics.eml.jaxb.eml.UrlType;
import au.org.ecoinformatics.eml.jaxb.eml.ViewType.References;

public class DatasetTypeTest {

	/**
	 * Can we build a dataset object with a title?
	 */
	@Test
	public void testDatasetTitle01() {
		String datasetTitle = "some dataset title";
		DatasetType result = new DatasetType()
			.withTitle(new I18NNonEmptyStringType().withContent(datasetTitle));
		assertThat(result.getTitle(), hasFirstI18NContent("some dataset title"));
	}

	/**
	 * Can we build a dataset object with a person as a creator?
	 */
	@Test
	public void testCreator01() {
		DatasetType result = new DatasetType()		
			.withCreator(new ResponsibleParty().withIndividualNameOrOrganizationNameOrPositionName(
					new ObjectFactory().createResponsiblePartyIndividualName(
							new Person()
								.withSalutation(new I18NNonEmptyStringType().withContent("Mr"))
								.withGivenName(new I18NNonEmptyStringType().withContent("Neo"))
								.withSurName(new I18NNonEmptyStringType().withContent("Anderson")))));
		Person actualPerson = (Person) result.getCreator().get(0).getIndividualNameOrOrganizationNameOrPositionName().get(0).getValue();
		assertThat(actualPerson.getSurName(), hasI18NContent("Anderson"));
	}

	/**
	 * Can we build a dataset object with an abstract?
	 */
	@Test
	public void testAbstractPara01() {
		DatasetType result = new DatasetType()
			.withAbstract(new TextType().withContent("some awesome abstract text"));
		String abstractParagraphFirstContent = (String) result.getAbstract().getContent().get(0);
		assertThat(abstractParagraphFirstContent, is("some awesome abstract text"));
	}

	/**
	 * Can we build a dataset object with some keywordSets?
	 */
	@Test
	public void testKeywordSet01() {
		DatasetType result = new DatasetType()
			.withKeywordSet(new KeywordSet().withKeyword(
					new Keyword().withContent("keywordOne-1"),
					new Keyword().withContent("keywordOne-2")))
			.withKeywordSet(new KeywordSet()
				.withKeyword(new Keyword().withContent("keywordTwo-1"))
				.withKeyword(new Keyword().withContent("keywordTwo-2")));
		KeywordSet firstKeywordSet = result.getKeywordSet().get(0);
		assertThat(((String)firstKeywordSet.getKeyword().get(0).getContent().get(0)), is("keywordOne-1"));
		assertThat(((String)firstKeywordSet.getKeyword().get(1).getContent().get(0)), is("keywordOne-2"));
		KeywordSet secondKeywordSet = result.getKeywordSet().get(1);
		assertThat(((String)secondKeywordSet.getKeyword().get(0).getContent().get(0)), is("keywordTwo-1"));
		assertThat(((String)secondKeywordSet.getKeyword().get(1).getContent().get(0)), is("keywordTwo-2"));
	}

	/**
	 * Can we build a dataset object with an intellectual rights element?
	 */
	@Test
	public void testIntellectualRights01() {
		DatasetType result = new DatasetType()
			.withIntellectualRights(new TextType().withContent("some intellectual text"));
		String intellectualRightsFirstContent = (String) result.getIntellectualRights().getContent().get(0);
		assertThat(intellectualRightsFirstContent, is("some intellectual text"));
	}
	
	/**
	 * Can we build a dataset object with two distribution elements?
	 */
	@Test
	public void testDistribution01() {
		String url1 = "http://something.com/one";
		String url2 = "http://another.com/two";
		DatasetType result = new DatasetType()
			.withDistribution(
				new DistributionType().withOnline(new OnlineType().withUrl(new UrlType().withValue(url1))))
			.withDistribution(
				new DistributionType().withOnline(new OnlineType().withUrl(new UrlType().withValue(url2))));
		String firstOnlineUrl = result.getDistribution().get(0).getOnline().getUrl().getValue();
		assertThat(firstOnlineUrl, is(url1));
		String secondOnlineUrl = result.getDistribution().get(1).getOnline().getUrl().getValue();
		assertThat(secondOnlineUrl, is(url2));
	}
	
	/**
	 * Can we build a dataset object with a 'coverage' element?
	 */
	@Test
	public void testCoverage01() {
		String references = "some ref stuff";
		DatasetType result = new DatasetType()
			.withCoverage(new Coverage().withGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage(
					new ObjectFactory().createGeographicCoverage().withReferences(new References().withValue(references))));
		GeographicCoverage firstCoverage = (GeographicCoverage) result.getCoverage().getGeographicCoverageOrTemporalCoverageOrTaxonomicCoverage().get(0);
		assertThat(firstCoverage.getReferences().getValue(), is(references));
	}
	
	/**
	 * Can we build an empty dataset without adding any empty elements to it?
	 */
	@Test
	public void testBuild01() {
		DatasetType result = new DatasetType();
		assertThat(result.getTitle().size(), is(0));
		assertThat(result.getCreator().size(), is(0));
		assertNull(result.getAbstract());
		assertThat(result.getKeywordSet().size(), is(0));
		assertNull(result.getIntellectualRights());
		assertThat(result.getDistribution().size(), is(0));
		assertNull(result.getCoverage());
	}
}
