package au.org.ecoinformatics.eml.builder;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasFirstI18NContent;
import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasI18NContent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.DatasetType;
import au.org.ecoinformatics.eml.jaxb.Person;
import au.org.ecoinformatics.eml.jaxb.ProtocolType.KeywordSet;

public class DatasetTypeBuilderTest {

	/**
	 * Can we build a dataset object with a title?
	 */
	@Test
	public void testDatasetTitle01() {
		DatasetTypeBuilder objectUnderTest = new DatasetTypeBuilder();
		String datasetTitle = "some dataset title";
		DatasetType result = objectUnderTest.datasetTitle(datasetTitle).build();
		assertThat(result.getTitle(), hasFirstI18NContent("some dataset title"));
	}

	/**
	 * Can we build a dataset object with a person as a creator?
	 */
	@Test
	public void testCreator01() {
		DatasetTypeBuilder objectUnderTest = new DatasetTypeBuilder();
		ResponsiblePartyBuilder creatorBuilder = new ResponsiblePartyBuilder().individualName(
				new PersonBuilder()
					.salutation("Mr")
					.givenName("Neo")
					.surName("Anderson")
					.build()
			);
		DatasetType result = objectUnderTest.creator(creatorBuilder).build();
		Person actualPerson = (Person) result.getCreator().get(0).getIndividualNameOrOrganizationNameOrPositionName().get(0).getValue();
		assertThat(actualPerson.getSurName(), hasI18NContent("Anderson"));
	}

	/**
	 * Can we build a dataset object with an abstract?
	 */
	@Test
	public void testAbstractPara01() {
		DatasetTypeBuilder objectUnderTest = new DatasetTypeBuilder();
		AbstractParaBuilder abstractParaBuilder = new AbstractParaBuilder("some awesome abstract text");
		DatasetType result = objectUnderTest.abstractPara(abstractParaBuilder).build();
		String abstractParagraphFirstContent = (String) result.getAbstract().getContent().get(0);
		assertThat(abstractParagraphFirstContent, is("some awesome abstract text"));
	}

	/**
	 * Can we build a dataset object with some keywordSets?
	 */
	@Test
	public void testKeywordSet01() {
		DatasetTypeBuilder objectUnderTest = new DatasetTypeBuilder();
		KeywordSetBuilder keywordSetBuilder1 = new KeywordSetBuilder()
			.addKeyword("keywordOne-1")
			.addKeyword("keywordOne-2");
		KeywordSetBuilder keywordSetBuilder2 = new KeywordSetBuilder()
			.addKeyword("keywordTwo-1")
			.addKeyword("keywordTwo-2");
		DatasetType result = objectUnderTest
			.addKeywordSet(keywordSetBuilder1)
			.addKeywordSet(keywordSetBuilder2)
			.build();
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
		DatasetTypeBuilder objectUnderTest = new DatasetTypeBuilder();
		IntellectualRightsBuilder intellectualRightsBuilder = new IntellectualRightsBuilder("some intellectual text");
		DatasetType result = objectUnderTest.intellectualRights(intellectualRightsBuilder).build();
		String intellectualRightsFirstContent = (String) result.getIntellectualRights().getContent().get(0);
		assertThat(intellectualRightsFirstContent, is("some intellectual text"));
	}
	
	/**
	 * Can we build an empty dataset without adding any empty elements to it?
	 */
	@Test
	public void testBuild01() {
		DatasetTypeBuilder objectUnderTest = new DatasetTypeBuilder();
		DatasetType result = objectUnderTest.build();
		assertThat(result.getTitle().size(), is(0));
		assertThat(result.getCreator().size(), is(0));
		assertNull(result.getAbstract());
		assertThat(result.getKeywordSet().size(), is(0));
		assertNull(result.getIntellectualRights());
	}
}
