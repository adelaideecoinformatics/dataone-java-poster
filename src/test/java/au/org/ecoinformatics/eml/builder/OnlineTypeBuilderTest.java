package au.org.ecoinformatics.eml.builder;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.OnlineType;

public class OnlineTypeBuilderTest {

	/**
	 * Can we construct with a URL?
	 */
	@Test
	public void testUrl01() {
		String url = "http://something.com/test";
		UrlTypeBuilder urlBuilder = new UrlTypeBuilder(url);
		OnlineType result = new OnlineTypeBuilder(urlBuilder).build();
		assertThat(result.getUrl().getValue(), is(url));
	}

	/**
	 * Can we construct with a connection?
	 */
	@Test
	public void testConnection01() {
		String referencesText = "some reference";
		ReferencesBuilder referencesBuilder = new ReferencesBuilder(referencesText);
		ConnectionTypeBuilder connectionBuilder = new ConnectionTypeBuilder(referencesBuilder);
		OnlineType result = new OnlineTypeBuilder(connectionBuilder).build();
		assertThat(result.getConnection().getReferences().getValue(), is(referencesText));
	}
	
	/**
	 * Can we construct with a connection definition?
	 */
	@Test
	public void testConnectionDefinition01() {
		String schemaName = "some schema";
		ConnectionDefinitionTypeBuilder connectionDefinitionTypeBuilder = new ConnectionDefinitionTypeBuilder(schemaName);
		OnlineType result = new OnlineTypeBuilder(connectionDefinitionTypeBuilder).build();
		assertThat(result.getConnectionDefinition().getSchemeName().getValue(), is(schemaName));
	}
	
	/**
	 * Can we build with an online description?
	 */
	@Test
	public void testOnlineDescription01() {
		OnlineTypeBuilder objectUnderTest = new OnlineTypeBuilder(new UrlTypeBuilder("http://something.com/test"));
		OnlineType result = objectUnderTest.onlineDescription("some desc").build();
		String firstContent = (String) result.getOnlineDescription().getContent().get(0);
		assertThat(firstContent, is("some desc"));
	}
	
	/**
	 * Are all the optional items empty when they aren't supplied?
	 */
	@Test
	public void testBuild01() {
		OnlineType result = new OnlineTypeBuilder(new UrlTypeBuilder("http://something.com/test")).build();
		assertNull(result.getOnlineDescription());
	}
}
