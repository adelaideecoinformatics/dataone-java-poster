package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.DistributionType;

public class DistributionTypeBuilderTest {

	/**
	 * Can we build with an 'online' element supplied?
	 */
	@Test
	public void testOnline01() {
		OnlineTypeBuilder onlineTypeBuilder = new OnlineTypeBuilder(new UrlTypeBuilder("http://some.url"));
		DistributionTypeBuilder objectUnderTest = new DistributionTypeBuilder(onlineTypeBuilder);
		DistributionType result = objectUnderTest.build();
		assertThat(result.getOnline().getUrl().getValue(), is("http://some.url"));
	}

	/**
	 * Can we build with a 'references' element supplied?
	 */
	@Test
	public void testReferences01() {
		String referencesText = "some ref";
		ReferencesBuilder referencesBuilder = new ReferencesBuilder(referencesText);
		DistributionTypeBuilder objectUnderTest = new DistributionTypeBuilder(referencesBuilder);
		DistributionType result = objectUnderTest.build();
		assertThat(result.getReferences().getValue(), is(referencesText));
	}
}
