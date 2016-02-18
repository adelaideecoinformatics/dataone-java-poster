package au.org.ecoinformatics.d1.poster;

import java.util.HashSet;
import java.util.Set;

import org.apache.jcs.access.exception.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import au.org.ecoinformatics.d1.poster.argval.ArgValidator;
import au.org.ecoinformatics.d1.poster.service.EcoinformaticsDataonePosterException;
import au.org.ecoinformatics.d1.poster.service.DataonePosterService;

/**
 * Application to perform the POSTing of objects (e.g. EML) and System Metadata (sysmeta) documents to a DataONE member node
 */
public class EcoinformaticsDataOnePosterApplication {

	private static final Logger logger = LoggerFactory.getLogger(EcoinformaticsDataOnePosterApplication.class);
	private DataonePosterService service;
	private Set<ArgValidator> validators = new HashSet<>();
	
	@Value("${dataone-poster.operation}")
	private String operation;
	
	@Value("${dataone-poster.directory}")
	private String directoryPath;
	
	@Value("${dataone-poster.file.object}")
	private String objectFilePath;
	
	@Value("${dataone-poster.file.sysmeta}")
	private String sysmetaFilePath;
	
	@Value("${arg.unset.placeholder}")
	private String argUnsetPlaceholder;

	public void run() {
		logger.info(String.format("Starting DataONE-POSTer to perform the %s operation", operation));
		validateArgs();
		try {
			if (runningForWholeDirectory()) {
				service.doPostForWholeDirectory(directoryPath);
			} else {
				service.doPostForSingleRecord(objectFilePath, sysmetaFilePath);
			}
		} catch (EcoinformaticsDataonePosterException e) {
			logger.error("ERROR: Bugga, something exploded", e);
		}
		logger.info("Finished DataONE-POSTer");
	}

	private boolean runningForWholeDirectory() {
		return directoryPath != argUnsetPlaceholder;
	}

	private void validateArgs() {
		// TODO validate endpoint
		// TODO validate operation
		// TODO check if files/directory actually exists and is what it's meant to be
		for (ArgValidator currValidator : validators) {
			try {
				currValidator.validate();
			} catch (InvalidArgumentException e) {
				throw new IllegalStateException("User error: invalid command line arguments supplied", e);
			}
		}
	}

	/**
	 * Entry point for the DataONE-POSTer application.
	 * <br /><br />
	 * Available command line arguments (used as <code>--&lt;arg&gt;=&lt;value&gt;</code>):
	 * <ul>
	 * <li>dataone-poster.endpoint=&lt;url&gt;:			(MANDATORY) full URL of the DataONE node endpoint</li>
	 * <li>dataone-poster.operation=&lt;op&gt;: 		(Optional) operation to perform</li>
	 * then either both of:
	 * <li>dataone-poster.file.object=&lt;file&gt;:		(MANDATORY) filename of the Object file to read</li>
	 * <li>dataone-poster.file.sysmeta=&lt;file&gt;:	(MANDATORY) filename of the SysMeta file to read</li>
	 * or only:
	 * <li>dataone-poster.directory=&lt;dir&gt;:		(MANDATORY) directory to read Object and Sysmeta file pairs from</li>
	 * </ul>
	 * 
	 * @param args			See Javadoc above for accepted values
	 */
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {"classpath:/au/org/ecoinformatics/d1/poster/application-context.xml"}, false);
		MutablePropertySources propertySources = context.getEnvironment().getPropertySources();
		propertySources.addFirst(new SimpleCommandLinePropertySource(args));
		context.refresh();
		EcoinformaticsDataOnePosterApplication app = context.getBean(EcoinformaticsDataOnePosterApplication.class);
		app.run();
		context.close();
	}

	public void setService(DataonePosterService service) {
		this.service = service;
	}

	public void setValidators(Set<ArgValidator> validators) {
		this.validators.addAll(validators);
	}
}
