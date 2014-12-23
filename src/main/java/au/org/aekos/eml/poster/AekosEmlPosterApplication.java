package au.org.aekos.eml.poster;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import au.org.aekos.eml.poster.service.EmlPosterService;

/**
 * Application to perform the POSTing of EML and System Metadata documents to a dataONE member node
 */
public class AekosEmlPosterApplication implements CommandLineRunner {

	private EmlPosterService service;

	@Override
	public void run(String... args) {
		service.doPost();
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run("classpath:/au/org/aekos/eml/poster/application-context.xml", args);
	}

	public void setService(EmlPosterService service) {
		this.service = service;
	}
}
