package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.DistributionType;
import au.org.ecoinformatics.eml.jaxb.eml.OnlineType;
import au.org.ecoinformatics.eml.jaxb.eml.UrlType;
import au.org.ecoinformatics.eml.jaxb.eml.ViewType.References;

public class DistributionTypeTest {

	/**
	 * Can we build with an 'online' element supplied?
	 */
	@Test
	public void testOnline01() {
		DistributionType result = new DistributionType()
			.withOnline(new OnlineType().withUrl(new UrlType().withValue("http://some.url")));
		assertThat(result.getOnline().getUrl().getValue(), is("http://some.url"));
	}

	/**
	 * Can we build with a 'references' element supplied?
	 */
	@Test
	public void testReferences01() {
		String referencesText = "some ref";
		DistributionType result = new DistributionType()
			.withReferences(new References().withValue(referencesText));
		assertThat(result.getReferences().getValue(), is(referencesText));
	}
}
