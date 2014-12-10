package au.org.ecoinformatics.eml.builder;

import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasFirstI18NContent;
import static au.org.ecoinformatics.eml.matchers.EcoinformaticsEmlMatchers.hasI18NContent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.DatasetType;
import au.org.ecoinformatics.eml.jaxb.Person;
import au.org.ecoinformatics.eml.jaxb.ResponsibleParty;
import au.org.ecoinformatics.eml.jaxb.TextType;

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
		ResponsibleParty creator = new ResponsiblePartyBuilder().person(
				new PersonBuilder()
					.salutation("Mr")
					.givenName("Neo")
					.surName("Anderson")
					.build()
			).build();
		DatasetType result = objectUnderTest.creator(creator).build();
		Person actualPerson = (Person) result.getCreator().get(0).getIndividualNameOrOrganizationNameOrPositionName().get(0).getValue();
		assertThat(actualPerson.getSurName(), hasI18NContent("Anderson"));
	}

	/**
	 * Can we build a dataset object with an abstract?
	 */
	@Test
	public void testAbstract01() {
		DatasetTypeBuilder objectUnderTest = new DatasetTypeBuilder();
		TextType abstractPara = new AbstractParaBuilder("some awesome abstract text").build();
		DatasetType result = objectUnderTest.abstractPara(abstractPara).build();
		String firstAbstractText = (String) result.getAbstract().getContent().get(0);
		assertThat(firstAbstractText, is("some awesome abstract text"));
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
	}
}
