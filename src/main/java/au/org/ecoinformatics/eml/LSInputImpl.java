package au.org.ecoinformatics.eml;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;

class LSInputImpl implements LSInput {

    private static final Logger logger = LoggerFactory.getLogger(LSInputImpl.class);
	private String publicId;
    private String systemId;
    private final BufferedInputStream inputStream;

    public LSInputImpl(String publicId, String sysId, InputStream input) {
        this.publicId = publicId;
        this.systemId = sysId;
        this.inputStream = new BufferedInputStream(input);
    }
    
    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }
    
    public String getStringData() {
    	synchronized (inputStream) {
    		try {
    			byte[] input = new byte[inputStream.available()];
    			inputStream.read(input);
    			String contents = new String(input);
    			return contents;
    		} catch (IOException e) {
    			logger.error("Problem reading the input stream", e);
    			return null;
    		}
    	}
    }
    
    public String getBaseURI() { return null; }
    public InputStream getByteStream() { return null; }
    public boolean getCertifiedText() { return false; }
    public Reader getCharacterStream() { return null; }
    public String getEncoding() { return null; }

    public void setBaseURI(String baseURI) { }
    public void setByteStream(InputStream byteStream) { }
    public void setCertifiedText(boolean certifiedText) { }
    public void setCharacterStream(Reader characterStream) { }
    public void setEncoding(String encoding) { }
    public void setStringData(String stringData) { }
}