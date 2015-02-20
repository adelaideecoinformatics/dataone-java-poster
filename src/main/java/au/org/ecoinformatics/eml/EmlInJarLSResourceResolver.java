package au.org.ecoinformatics.eml;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * A resource resolver to handle loading referenced schema files from a JAR on the classpath.
 * 
 * Modified from http://code.google.com/p/xmlsanity/source/browse/src/com/arc90/xmlsanity/validation/ResourceResolver.java?r=03a92d97f15904b3892922e45724bb086d54fa4e
 * Original credit to:
 * From http://pbin.oogly.co.uk/listings/viewlistingdetail/2a70d763929ce3053085bfaa1d78e2
 * From http://stackoverflow.com/questions/1094893/validate-an-xml-file-against-multiple-schema-definitions/1105871#1105871
 * @author Jon http://stackoverflow.com/users/82865/jon
 */
class EmlInJarLSResourceResolver implements LSResourceResolver {
	
	private static final Logger logger = LoggerFactory.getLogger(EmlInJarLSResourceResolver.class);
	private static final Map<ResolutionStrategyKey, ResolutionStrategy> resolutionStrategies = getStrategies();
	private final String localBasePath;

	/**
	 * @param localBasePath		path to look in for requested relative XSD file names
	 */
	public EmlInJarLSResourceResolver(String localBasePath) {
		this.localBasePath = localBasePath;
	}
	
	@Override
	public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
		boolean isUrl = isAValidUrl(systemId);
		ResolutionStrategy strategy = resolutionStrategies.get(new ResolutionStrategyKey(isUrl));
		return strategy.handle(publicId, systemId, localBasePath);
	}
	
	boolean isAValidUrl(String systemId) {
		try {
			new URL(systemId);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}

	private interface ResolutionStrategy {

		LSInput handle(String publicId, String systemId, String localBasePath);
		
	}
	
	private static class ClasspathFileResolutionStrategy implements ResolutionStrategy {

		@Override
		public LSInput handle(String publicId, String systemId, String localBasePath) {
			String fullXsdPath = localBasePath + systemId;
			logger.debug(String.format("Attempting to load requested XSD '%s' from %s", systemId, fullXsdPath));
			InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fullXsdPath);
		    return new LSInputImpl(publicId, systemId, resourceAsStream);
		}
		
	}

	private static class URLResolutionStrategy implements ResolutionStrategy {

		@Override
		public LSInput handle(String publicId, String systemId, String localBasePath) {
			logger.debug(String.format("Attempting to load requested XSD '%s' from %s", systemId, systemId));
			try {
				InputStream urlStream = new URL("http://www.w3.org/2009/01/xml.xsd").openStream();
				return new LSInputImpl(publicId, systemId, urlStream);
			} catch (MalformedURLException e) {
				throw new RuntimeException("Failed to read the remote XSD", e);
			} catch (IOException e) {
				throw new RuntimeException("Failed to read the remote XSD", e);
			}
		}
	}
	
	
	private static Map<ResolutionStrategyKey, ResolutionStrategy> getStrategies() {
		Map<ResolutionStrategyKey, ResolutionStrategy> result = new HashMap<ResolutionStrategyKey, ResolutionStrategy>();
		result.put(new ResolutionStrategyKey(false), new ClasspathFileResolutionStrategy());
		result.put(new ResolutionStrategyKey(true), new URLResolutionStrategy());
		return result;
	}
	
	private static class ResolutionStrategyKey {
		private final boolean isUrl;

		public ResolutionStrategyKey(boolean isUrl) {
			this.isUrl = isUrl;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (isUrl ? 1231 : 1237);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ResolutionStrategyKey other = (ResolutionStrategyKey) obj;
			if (isUrl != other.isUrl)
				return false;
			return true;
		}
	}
}