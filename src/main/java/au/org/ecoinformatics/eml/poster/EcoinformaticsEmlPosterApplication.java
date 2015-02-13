package au.org.ecoinformatics.eml.poster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import au.org.ecoinformatics.eml.poster.service.EmlPosterService;

/**
 * Application to perform the POSTing of EML and System Metadata (sysmeta) documents to a dataONE member node
 */
public class EcoinformaticsEmlPosterApplication {

	private static final Logger logger = LoggerFactory.getLogger(EcoinformaticsEmlPosterApplication.class);
	private EmlPosterService service;
	
	@Value("${eml-poster.operation}")
	private String operation;

	public void run() {
		logger.info(String.format("Starting EML-POSTer to perform the %s operation", operation));
		// TODO validate args
		service.doPost();
		logger.info("Finished EML-POSTer");
	}

	/**
	 * Entry point for the EML-POSTer application.
	 * <br /><br />
	 * Available command line arguments (used as <code>--&lt;arg&gt;=&lt;value&gt;</code>):
	 * <ul>
	 * <li>eml-poster.endpoint=&lt;url&gt;: (MANDATORY) full URL of the DataONE node endpoint</li>
	 * <li>eml-poster.file.eml=&lt;filename&gt;: (MANDATORY) filename of the EML file to read</li>
	 * <li>eml-poster.file.sysmeta=&lt;filename&gt;: (MANDATORY) filename of the SysMeta file to read</li>
	 * <li>eml-poster.operation=&lt;op&gt;: (Optional) operation to perform</li>
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
}
