package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.ConnectionDefinitionType;
import au.org.ecoinformatics.eml.jaxb.eml.ConnectionDefinitionType.SchemeName;
import au.org.ecoinformatics.eml.jaxb.eml.ConnectionType;
import au.org.ecoinformatics.eml.jaxb.eml.I18NNonEmptyStringType;
import au.org.ecoinformatics.eml.jaxb.eml.OnlineType;
import au.org.ecoinformatics.eml.jaxb.eml.UrlType;
import au.org.ecoinformatics.eml.jaxb.eml.ViewType.References;

public class OnlineTypeTest {

	/**
	 * Can we construct with a URL?
	 */
	@Test
	public void testUrl01() {
		String url = "http://something.com/test";
		OnlineType result = new OnlineType()
			.withUrl(new UrlType().withValue(url));
		assertThat(result.getUrl().getValue(), is(url));
	}

	/**
	 * Can we construct with a connection?
	 */
	@Test
	public void testConnection01() {
		String referencesText = "some reference";
		OnlineType result = new OnlineType()
			.withConnection(
					new ConnectionType().withReferences(
							new References().withValue(referencesText)));
		assertThat(result.getConnection().getReferences().getValue(), is(referencesText));
	}
	
	/**
	 * Can we construct with a connection definition?
	 */
	@Test
	public void testConnectionDefinition01() {
		String schemaName = "some schema";
		OnlineType result = new OnlineType()
			.withConnectionDefinition(
					new ConnectionDefinitionType().withSchemeName(
							new SchemeName().withValue(schemaName)));
		assertThat(result.getConnectionDefinition().getSchemeName().getValue(), is(schemaName));
	}
	
	/**
	 * Can we build with an online description?
	 */
	@Test
	public void testOnlineDescription01() {
		OnlineType result = new OnlineType()
			.withOnlineDescription(new I18NNonEmptyStringType().withContent("some desc"));
		String firstContent = (String) result.getOnlineDescription().getContent().get(0);
		assertThat(firstContent, is("some desc"));
	}
	
	/**
	 * Are all the optional items empty when they aren't supplied?
	 */
	@Test
	public void testBuild01() {
		OnlineType result = new OnlineType();
		assertNull(result.getOnlineDescription());
		assertNull(result.getUrl());
	}
}
