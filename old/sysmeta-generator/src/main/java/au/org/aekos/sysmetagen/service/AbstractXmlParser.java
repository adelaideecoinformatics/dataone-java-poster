package au.org.aekos.sysmetagen.service;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * Base class for extracting the details from an XML file that are needed
 * for writing a sysmeta record. There is a requirement that the processes XML
 * file uses a prefix for the root element and that prefix is defined with an 
 * <code>xmlns</code> attribute on the root element.
 * 
 * @author Tom Saleeba
 */
public abstract class AbstractXmlParser implements XmlParser {

	private static final String XML_PREFIX_AND_NODE_NAME_SEPARATOR = ":";
	private static final Logger logger = LoggerFactory.getLogger(AbstractXmlParser.class);
	private static final String WHITESPACE_REGEX = "\\s+";
	private static final String DOESNT_EXIST = "";
	private final String idenitiferXpath;
	private final String contactXpath;
	private final XPath xpath = getXpath();

	/**
	 * @param idenitiferXpath	Xpath to extract the identifier of the record
	 * @param contactXpath		Xpath to extract the contact of the record
	 */
	public AbstractXmlParser(String idenitiferXpath, String contactXpath) {
		this.idenitiferXpath = idenitiferXpath;
		this.contactXpath = contactXpath;
	}

	@Override
	public boolean canParse(Document doc) {
		try {
			String content = extractIdentifier(doc);
			return !content.equalsIgnoreCase(DOESNT_EXIST);
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Programmer error: XPath could not be evaluated", e);
		}
	}

	@Override
	public SysMetaFragments parse(Document doc) throws XmlParserException {
		logger.info("Treating input file as " + getPrettyFormatName());
		try {
			String identifier = extractIdentifier(doc);
			String contact = extractContact(doc);
			String formatId = extractFormatId(doc);
			SysMetaFragments result = new SysMetaFragments(identifier, contact, formatId);
			return result;
		} catch (XPathException e) {
			throw new XmlParserException("Programmer error: Failed execute the XPath", e);
		}
	}

	abstract String getPrettyFormatName();
	
	private XPath getXpath() {
		XPathFactory xPathfactory = XPathFactory.newInstance();
		return xPathfactory.newXPath();
	}
	
	/**
	 * Finds the namespace of the root element. This would be simple if the document was parsed
	 * with a namespace aware {@link DocumentBuilderFactory} but that makes everything else hard
	 * so this is the compromise.
	 * 
	 * @param doc	document to parse
	 * @return		the namespace of the prefix assigned to the root element as defined in the document
	 * @throws XPathException	when something goes wrong
	 */
	private String extractFormatId(Document doc) throws XPathException {
		Node rootElement = doc.getFirstChild();
		String rootElementNodeName = rootElement.getNodeName();
		String rootElementPrefix = rootElementNodeName.substring(0, rootElementNodeName.indexOf(XML_PREFIX_AND_NODE_NAME_SEPARATOR));
		Node xmlnsAttribute = rootElement.getAttributes().getNamedItem(
				XMLConstants.XMLNS_ATTRIBUTE + XML_PREFIX_AND_NODE_NAME_SEPARATOR + rootElementPrefix);
		return xmlnsAttribute.getNodeValue();
	}

	private String extractContact(Document doc) throws XPathExpressionException {
		XPathExpression expr = xpath.compile(contactXpath);
		String contactContent = expr.evaluate(doc);
		return contactContent.replaceAll(WHITESPACE_REGEX, " ");
	}

	private String extractIdentifier(Document doc) throws XPathExpressionException {
		XPathExpression expr = xpath.compile(idenitiferXpath);
		return expr.evaluate(doc);
	}
}
