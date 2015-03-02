package au.org.ecoinformatics.eml;

import java.io.InputStream;
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
	private final String localXsdBasePath;
	private String emlSubdirName;

	/**
	 * @param localXsdBasePath		path to look in for requested relative XSD file names
	 * @param emlSubdirName			name of the subdirectory that has the EML XSDs
	 */
	public EmlInJarLSResourceResolver(String localXsdBasePath, String emlSubdirName) {
		this.localXsdBasePath = localXsdBasePath;
		this.emlSubdirName = emlSubdirName;
	}
	
	@Override
	public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
		ResolutionStrategy strategy = resolutionStrategies.get(ResolutionStrategyKey.newKey(systemId));
		String xsdPath = strategy.getPath(localXsdBasePath, emlSubdirName, systemId);
		logger.debug(String.format("Attempting to load requested XSD '%s' from %s", systemId, xsdPath));
		InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(xsdPath);
		return new LSInputImpl(publicId, systemId, resourceAsStream);
	}

	private interface ResolutionStrategy {
		String getPath(String localXsdBasePath, String emlSubdirName, String systemId);
	}
	
	private static class EmlXsdResolutionStrategy implements ResolutionStrategy {
		@Override
		public String getPath(String localXsdBasePath, String emlSubdirName, String systemId) {
			return localXsdBasePath + emlSubdirName + systemId;
		}
	}

	private static class ThirdPartyXsdResolutionStrategy implements ResolutionStrategy {

		private static final String DONT_USE_SUBDIR = "";
		private static final String XML_XSD_FILENAME = "2001-03-xml.xsd";
		private static final String XML_DTD_FILENAME = "2009-XMLSchema.dtd";
		private static final String DATATYPES_DTD_FILENAME = "2001-datatypes.dtd";
		private static final Map<String, String> XSD_NAME_MAPPINGS = new HashMap<String, String>();
		static {
			XSD_NAME_MAPPINGS.put("http://www.w3.org/2009/01/xml.xsd", XML_XSD_FILENAME);
			XSD_NAME_MAPPINGS.put("XMLSchema.dtd", XML_DTD_FILENAME);
			XSD_NAME_MAPPINGS.put("datatypes.dtd", DATATYPES_DTD_FILENAME);
		}
		
		@Override
		public String getPath(String localXsdBasePath, String emlSubdirName, String systemId) {
			String localXsdFilename = XSD_NAME_MAPPINGS.get(systemId);
			logger.info(String.format("Mapped the requested '%s' to '%s'", systemId, localXsdFilename));
			explodeIfNull(localXsdFilename);
			return localXsdBasePath + DONT_USE_SUBDIR + localXsdFilename;
		}

		private void explodeIfNull(String s) {
			s.toString();
		}
	}
	
	private static Map<ResolutionStrategyKey, ResolutionStrategy> getStrategies() {
		Map<ResolutionStrategyKey, ResolutionStrategy> result = new HashMap<ResolutionStrategyKey, ResolutionStrategy>();
		result.put(new ResolutionStrategyKey(true), new EmlXsdResolutionStrategy());
		result.put(new ResolutionStrategyKey(false), new ThirdPartyXsdResolutionStrategy());
		return result;
	}
	
	private static class ResolutionStrategyKey {
		private static final String EML_PREFIX = "eml";
		private final boolean isEmlXsd;

		public ResolutionStrategyKey(boolean isEmlXsd) {
			this.isEmlXsd = isEmlXsd;
		}

		public static ResolutionStrategyKey newKey(String systemId) {
			return new ResolutionStrategyKey(systemId.startsWith(EML_PREFIX));
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (isEmlXsd ? 1231 : 1237);
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
			if (isEmlXsd != other.isEmlXsd)
				return false;
			return true;
		}
	}
}