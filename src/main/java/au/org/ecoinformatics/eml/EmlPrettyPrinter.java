package au.org.ecoinformatics.eml;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.ecoinformatics.eml.jaxb.eml.Eml;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class EmlPrettyPrinter {

	/**
	 * Pretty prints the supplied EML document to the supplied stream.
	 *
	 * @param eml	document to pretty print
	 * @param out	stream to print document to
	 */
	public void prettyPrint(Eml eml, OutputStream out) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Eml.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			NamespacePrefixMapper mapper = new EmlNamespacePrefixMapper();
			jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", mapper);
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
			jaxbMarshaller.marshal(eml, out);
		} catch (JAXBException e) {
			throw new RuntimeException("Failed to pretty print the EML document", e);
		}
	}

	private static class EmlNamespacePrefixMapper extends NamespacePrefixMapper {
		private static final Logger logger = LoggerFactory.getLogger(EmlNamespacePrefixMapper.class);
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
}
