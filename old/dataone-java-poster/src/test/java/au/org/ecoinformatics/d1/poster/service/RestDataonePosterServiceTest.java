package au.org.ecoinformatics.d1.poster.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import au.org.ecoinformatics.d1.poster.service.DataonePosterStrategy;
import au.org.ecoinformatics.d1.poster.service.RestDataonePosterService;

public class RestDataonePosterServiceTest {

	/**
	 * Can we create a list of available operations?
	 */
	@Test
	public void testGetAvailableOperations01() {
		RestDataonePosterService objectUnderTest = new RestDataonePosterService();
		Map<String, DataonePosterStrategy> operationStrategies = new TreeMap<String, DataonePosterStrategy>();
		operationStrategies.put("create", null);
		operationStrategies.put("delete", null);
		operationStrategies.put("update", null);
		objectUnderTest.setOperationStrategies(operationStrategies);
		String result = objectUnderTest.getAvailableOperations();
		assertThat(result, is("create, delete, update"));
	}

}
