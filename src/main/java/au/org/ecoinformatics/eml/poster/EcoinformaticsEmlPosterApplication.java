package au.org.ecoinformatics.eml.poster;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import au.org.ecoinformatics.eml.poster.service.EmlPosterService;

/**
 * Application to perform the POSTing of EML and System Metadata (sysmeta) documents to a dataONE member node
 */
public class EcoinformaticsEmlPosterApplication {

	private EmlPosterService service;

	public void run() {
		service.doPost();
	}

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
