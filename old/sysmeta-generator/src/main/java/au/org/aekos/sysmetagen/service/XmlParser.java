package au.org.aekos.sysmetagen.service;

import org.w3c.dom.Document;

public interface XmlParser {

	public class XmlParserException extends Exception {

		private static final long serialVersionUID = 1L;

		public XmlParserException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	boolean canParse(Document doc);

	/**
	 * @param doc	XML document to parse
	 * @return		extracted data
	 * @throws XmlParserException	when something goes wrong at any point
	 */
	SysMetaFragments parse(Document doc) throws XmlParserException;
}
