package au.org.ecoinformatics.eml;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

abstract class AbstractEcoinformaticsNamespacePrefixMapper extends NamespacePrefixMapper {
	private static final Logger logger = LoggerFactory.getLogger(AbstractEcoinformaticsNamespacePrefixMapper.class);
	private final Map<String, String> nsMappings;
	
	public AbstractEcoinformaticsNamespacePrefixMapper() {
		this.nsMappings = getNamespaceMappings();
	}

	abstract Map<String, String> getNamespaceMappings();
	
	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
		logger.debug("handling '{}' with suggestion '{}'", namespaceUri, suggestion);
		for (String currMapping : nsMappings.keySet()) {
			if (namespaceUri.startsWith(currMapping)) {
				return nsMappings.get(currMapping);
			}
		}
		return null;
	}
}