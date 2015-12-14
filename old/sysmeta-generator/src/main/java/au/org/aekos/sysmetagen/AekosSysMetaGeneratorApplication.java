package au.org.aekos.sysmetagen;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import au.org.aekos.sysmetagen.service.SysMetaService;

public class AekosSysMetaGeneratorApplication {

	private static final Logger logger = LoggerFactory.getLogger(AekosSysMetaGeneratorApplication.class);
	private SysMetaService service;

	@Value("${sysmeta-generator.input.path}")
	private String inputPath;
	
	private void run() {
		logger.info("Started SysMeta generator");
		// TODO validate args
		Path inputPathObj = Paths.get(inputPath);
		if (Files.isDirectory(inputPathObj)) {
			service.doGenerateForDirectory(inputPathObj);
		} else {
			service.doGenerateForFile(inputPathObj);
		}
		logger.info("Finished SysMeta generator");
	}

	/**
	 * 
	 * sysmeta-generator.input.path
	 * sysmeta-generator.file.output.suffix
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		boolean dontRefreshYet = false;
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {"classpath:/au/org/aekos/sysmetagen/application-context.xml"}, dontRefreshYet);
		MutablePropertySources propertySources = context.getEnvironment().getPropertySources();
		propertySources.addFirst(new SimpleCommandLinePropertySource(args));
		context.refresh();
		AekosSysMetaGeneratorApplication app = context.getBean(AekosSysMetaGeneratorApplication.class);
		app.run();
		context.close();
	}

	public void setService(SysMetaService service) {
		this.service = service;
	}
}
