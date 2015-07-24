package au.org.aekos.sysmetagen.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import au.org.aekos.sysmetagen.service.XmlParser.XmlParserException;
import au.org.ecoinformatics.eml.EmlValidationException;
import au.org.ecoinformatics.eml.jaxb.sysmeta.AccessPolicy;
import au.org.ecoinformatics.eml.jaxb.sysmeta.AccessRule;
import au.org.ecoinformatics.eml.jaxb.sysmeta.Checksum;
import au.org.ecoinformatics.eml.jaxb.sysmeta.Identifier;
import au.org.ecoinformatics.eml.jaxb.sysmeta.Permission;
import au.org.ecoinformatics.eml.jaxb.sysmeta.ReplicationPolicy;
import au.org.ecoinformatics.eml.jaxb.sysmeta.Subject;
import au.org.ecoinformatics.eml.jaxb.sysmeta.SystemMetadata;

public class DefaultSysMetaService implements SysMetaService {

	private static final String TARGET_FILE_EXTENSION = ".xml";
	private static final int NUMBER_OF_REPLICAS = 1;
	private static final String PUBLIC = "public";
	private static final Logger logger = LoggerFactory.getLogger(DefaultSysMetaService.class);
	private static final String MD5_ALGORITHM = "MD5";
	private static final boolean ALWAYS_REPLICATE = true;
	private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	@Value("${sysmeta-generator.file.output.suffix}")
	private String outputSuffix;
	
	private Set<XmlParser> parsers;
	private int filesProcessed = 0;

	@Override
	public void doGenerateForDirectory(Path directory) {
		logger.info(String.format("Searching directory %s for files with the extension %s", directory.toString(), TARGET_FILE_EXTENSION));
		FilenameFilter filter = new SuffixFileFilter(TARGET_FILE_EXTENSION);
		for (File currFile : directory.toFile().listFiles(filter)) {
			doGenerateForFile(currFile.toPath()); // TODO add switch to push past errors and handle exceptions here
			filesProcessed++;
		}
		logger.info(String.format("Processed %s files", filesProcessed));
	}
	
	@Override
	public void doGenerateForFile(Path file) {
		try {
			SysMetaFragments details = readRequiredDataFromInputFile(file.toUri());
			SystemMetadata output = createSysMetaFile(details, getInputFileSize(file), getChecksumOfInputFile(file));
			OutputStream os = Files.newOutputStream(Paths.get(getOutputPathString(file)),
					StandardOpenOption.CREATE, 
					StandardOpenOption.TRUNCATE_EXISTING, 
					StandardOpenOption.WRITE);
			logger.info("Writing the sysmeta file to " + getOutputPathString(file));
			output.write(os);
			output.validate();
		} catch (FileNotFoundException e) {
			throw new FailedToProcessFileException("Failed to open the input file '" + file + "'", e);
		} catch (XmlParserException e) {
			throw new FailedToProcessFileException("Failed to parse the input file '" + file + "'", e);
		} catch (IOException e) {
			throw new FailedToProcessFileException("Failed to open the output file '" + getOutputPathString(file) + "'", e);
		} catch (EmlValidationException e) {
			throw new FailedToProcessFileException("Built sysmeta file failed validation but it has been written "
					+ "out for debugging to '" + getOutputPathString(file) + "'", e);
		}
	}

	private class FailedToProcessFileException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public FailedToProcessFileException(String message, Throwable cause) {
			super(message, cause);
		}
	}
	
	private SysMetaFragments readRequiredDataFromInputFile(URI filePath) throws FileNotFoundException, XmlParserException {
		InputStream is = new FileInputStream(new File(filePath));
		try {
			SysMetaFragments details = findAndExecuteParser(is);
			return details;
		} catch (NoParserFoundException e) {
			throw new RuntimeException("Couldn't process the file: " + filePath, e);
		}
	}

	SysMetaFragments findAndExecuteParser(InputStream is) throws XmlParserException, NoParserFoundException {
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(is);
			for (XmlParser currParser : parsers) {
				if (!currParser.canParse(doc)) {
					continue;
				}
				return currParser.parse(doc);
			}
		} catch (ParserConfigurationException e) {
			throw new XmlParserException("Programmer error: Failed build a document builder", e);
		} catch (SAXException e) {
			throw new XmlParserException("Data error: Failed parse the XML data", e);
		} catch (IOException e) {
			throw new XmlParserException("Data error: Failed parse the XML data", e);
		}
		throw new NoParserFoundException("Programmer error: no parser found");
	}

	private class NoParserFoundException extends Exception {

		private static final long serialVersionUID = 1L;

		public NoParserFoundException(String message) {
			super(message);
		}
	}
	
	SystemMetadata createSysMetaFile(SysMetaFragments details, BigInteger size, String checksum) {
		Subject submitterSubject = new Subject().withValue(details.getContact());
		SystemMetadata result = new SystemMetadata()
			.withIdentifier(new Identifier().withValue(details.getIdentifier()))
			.withFormatId(details.getFormatId())
		 	.withSize(size)
		 	.withChecksum(new Checksum()
		 		.withValue(checksum)
		 		.withAlgorithm(MD5_ALGORITHM))
	 		.withRightsHolder(submitterSubject)
	 		.withSubmitter(submitterSubject)
			.withAccessPolicy(new AccessPolicy()
				.withAllows(
					new AccessRule()
					.withSubjects(submitterSubject)
					.withPermissions(
						Permission.READ, 
						Permission.WRITE, 
						Permission.CHANGE_PERMISSION), 
					new AccessRule()
					.withSubjects(new Subject().withValue(PUBLIC))
					.withPermissions(Permission.READ)))
			.withReplicationPolicy(new ReplicationPolicy()
				.withReplicationAllowed(ALWAYS_REPLICATE)
				.withNumberReplicas(NUMBER_OF_REPLICAS));
		return result;
	}

	BigInteger getInputFileSize(Path file) {
		try {
			long size = Files.size(file);
			return new BigInteger(String.valueOf(size));
		} catch (IOException e) {
			throw new RuntimeException("Failed to read the input file", e);
		}
	}
	
	String getChecksumOfInputFile(Path file) {
		try {
			MessageDigest md = MessageDigest.getInstance(MD5_ALGORITHM);
			try (InputStream is = Files.newInputStream(file)) {
			  DigestInputStream dis = new DigestInputStream(is, md);
			  byte[] b = new byte[8];
			  while (dis.available() != 0) {
				  dis.read(b);
			  }
			}
			return toMD5ChecksumString(md.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Programmer error: incorrectly configured checksum algorithm", e);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read the input file", e);
		}
	}

	// Thanks to http://www.rgagnon.com/javadetails/java-0416.html
	private String toMD5ChecksumString(byte[] checksum) {
		String result = "";
		for (int i = 0; i < checksum.length; i++) {
			result += Integer.toString((checksum[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
	
	private String getOutputPathString(Path file) {
		return file.toString() + outputSuffix;
	}

	public void setParsers(Set<XmlParser> parsers) {
		this.parsers = parsers;
	}
}
