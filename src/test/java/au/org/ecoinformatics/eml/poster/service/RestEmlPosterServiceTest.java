package au.org.ecoinformatics.eml.poster.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class RestEmlPosterServiceTest {

	/**
	 * Can we create a list of available operations?
	 */
	@Test
	public void testGetAvailableOperations01() {
		RestEmlPosterService objectUnderTest = new RestEmlPosterService();
		Map<String, EmlPosterStrategy> operationStrategies = new TreeMap<String, EmlPosterStrategy>();
		operationStrategies.put("create", null);
		operationStrategies.put("delete", null);
		operationStrategies.put("update", null);
		objectUnderTest.setOperationStrategies(operationStrategies);
		String result = objectUnderTest.getAvailableOperations();
		assertThat(result, is("create, delete, update"));
	}

}
