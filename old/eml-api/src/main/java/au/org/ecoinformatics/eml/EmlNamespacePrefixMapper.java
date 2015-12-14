package au.org.ecoinformatics.eml;

import java.util.HashMap;
import java.util.Map;

class EmlNamespacePrefixMapper extends AbstractEcoinformaticsNamespacePrefixMapper {
	private static final Map<String, String> nsMappings = new HashMap<String, String>();
	static {
		nsMappings.put("eml://ecoinformatics.org/eml", "eml");
		nsMappings.put("eml://ecoinformatics.org/access", "acc");
		nsMappings.put("eml://ecoinformatics.org/attribute", "att");
		nsMappings.put("eml://ecoinformatics.org/coverage", "cov");
		nsMappings.put("eml://ecoinformatics.org/dataset", "ds");
		nsMappings.put("eml://ecoinformatics.org/dataTable", "dat");
		nsMappings.put("eml://ecoinformatics.org/documentation", "doc");
		nsMappings.put("eml://ecoinformatics.org/entity", "ent");
		nsMappings.put("eml://ecoinformatics.org/literature", "cit");
		nsMappings.put("eml://ecoinformatics.org/methods", "md");
		nsMappings.put("eml://ecoinformatics.org/party", "rp");
		nsMappings.put("eml://ecoinformatics.org/physical", "phys");
		nsMappings.put("eml://ecoinformatics.org/project", "proj");
		nsMappings.put("eml://ecoinformatics.org/protocol", "prot");
		nsMappings.put("eml://ecoinformatics.org/resource", "res");
		nsMappings.put("eml://ecoinformatics.org/software", "sw");
		nsMappings.put("eml://ecoinformatics.org/spatialReference", "spref");
		nsMappings.put("eml://ecoinformatics.org/spatialVector", "sv");
		nsMappings.put("eml://ecoinformatics.org/spatialRaster", "sr");
		nsMappings.put("eml://ecoinformatics.org/storedProcedure", "sp");
		nsMappings.put("eml://ecoinformatics.org/text", "txt");
		nsMappings.put("eml://ecoinformatics.org/view", "v");
		nsMappings.put("http://www.w3.org/XML/1998/namespace", "xml");
	}

	@Override
	Map<String, String> getNamespaceMappings() {
		return nsMappings;
	}
}