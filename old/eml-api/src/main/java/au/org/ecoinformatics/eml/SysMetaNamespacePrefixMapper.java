package au.org.ecoinformatics.eml;

import java.util.HashMap;
import java.util.Map;

class SysMetaNamespacePrefixMapper extends AbstractEcoinformaticsNamespacePrefixMapper {
	private static final Map<String, String> nsMappings = new HashMap<String, String>();
	static {
		nsMappings.put("http://ns.dataone.org/service/types/v1", "d1");
		nsMappings.put("http://www.w3.org/XML/1998/namespace", "xml");
	}

	@Override
	Map<String, String> getNamespaceMappings() {
		return nsMappings;
	}
}