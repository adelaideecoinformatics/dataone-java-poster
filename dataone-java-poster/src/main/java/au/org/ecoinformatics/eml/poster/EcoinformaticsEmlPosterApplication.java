package au.org.ecoinformatics.eml.poster;

import java.util.HashSet;
import java.util.Set;

import org.apache.jcs.access.exception.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import au.org.ecoinformatics.eml.poster.argval.ArgValidator;
import au.org.ecoinformatics.eml.poster.service.EcoinformaticsEmlPosterException;
import au.org.ecoinformatics.eml.poster.service.EmlPosterService;

/**
 * Application to perform the POSTing of EML and System Metadata (sysmeta) documents to a dataONE member node
 */
public class EcoinformaticsEmlPosterApplication {

	private static final Logger logger = LoggerFactory.getLogger(EcoinformaticsEmlPosterApplication.class);
	private EmlPosterService service;
	private Set<ArgValidator> validators = new HashSet<>();
	
	@Value("${eml-poster.operation}")
	private String operation;
	
	@Value("${eml-poster.directory}")
	private String directoryPath;
	
	@Value("${eml-poster.file.eml}")
	private String emlFilePath;
	
	@Value("${eml-poster.file.sysmeta}")
	private String smdFilePath;
	
	@Value("${arg.unset.placeholder}")
	private String argUnsetPlaceholder;

	public void run() {
		logger.info(String.format("Starting EML-POSTer to perform the %s operation", operation));
		validateArgs();
		try {
			if (runningForWholeDirectory()) {
				service.doPostForWholeDirectory(directoryPath);
			} else {
				service.doPostForSingleRecord(emlFilePath, smdFilePath);
			}
		} catch (EcoinformaticsEmlPosterException e) {
			logger.error("ERROR: Bugga, something exploded", e);
		}
		logger.info("Finished EML-POSTer");
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
	 * Entry point for the EML-POSTer application.
	 * <br /><br />
	 * Available command line arguments (used as <code>--&lt;arg&gt;=&lt;value&gt;</code>):
	 * <ul>
	 * <li>eml-poster.endpoint=&lt;url&gt;:			(MANDATORY) full URL of the DataONE node endpoint</li>
	 * <li>eml-poster.operation=&lt;op&gt;: 		(Optional) operation to perform</li>
	 * then either both of:
	 * <li>eml-poster.file.eml=&lt;file&gt;:		(MANDATORY) filename of the EML file to read</li>
	 * <li>eml-poster.file.sysmeta=&lt;file&gt;:	(MANDATORY) filename of the SysMeta file to read</li>
	 * or only:
	 * <li>eml-poster.directory=&lt;dir&gt;:		(MANDATORY) directory to read EML and Sysmeta file pairs from</li>
	 * </ul>
	 * 
	 * @param args			See Javadoc above for accepted values
	 */
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {"classpath:/au/org/ecoinformatics/eml/poster/application-context.xml"}, false);
		MutablePropertySources propertySources = context.getEnvironment().getPropertySources();
		propertySources.addFirst(new SimpleCommandLinePropertySource(args));
		context.refresh();
		EcoinformaticsEmlPosterApplication app = context.getBean(EcoinformaticsEmlPosterApplication.class);
		app.run();
		context.close();
	}

	public void setService(EmlPosterService service) {
		this.service = service;
	}

	public void setValidators(Set<ArgValidator> validators) {
		this.validators.addAll(validators);
	}
}
