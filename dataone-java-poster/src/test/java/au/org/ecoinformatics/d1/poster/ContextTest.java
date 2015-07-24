package au.org.ecoinformatics.d1.poster;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ContextTest {

	/**
	 * Can we successfully load the "real" Spring application context?
	 */
	@Test
	public void testContext01() throws Throwable {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {"classpath:/au/org/ecoinformatics/d1/poster/application-context.xml"});
		context.close();
	}
}
