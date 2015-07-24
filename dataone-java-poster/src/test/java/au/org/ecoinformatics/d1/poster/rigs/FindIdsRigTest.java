package au.org.ecoinformatics.d1.poster.rigs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FindIdsRigTest {

	/**
	 * Can we match LTERN records?
	 */
	@Test
	public void testDoesntMatchAnyFilters01() {
		FindIdsRig objectUnderTest = getObjectUnderTestWithPreconfiguredIncludeFilters();
		boolean result = objectUnderTest.matchesAnyFilter("ltern2.105.17");
		assertTrue("Should match", result);
	}

	/**
	 * Can we match a datalibrarian record?
	 */
	@Test
	public void testDoesntMatchAnyFilters02() {
		FindIdsRig objectUnderTest = getObjectUnderTestWithPreconfiguredIncludeFilters();
		boolean result = objectUnderTest.matchesAnyFilter("datalibrarian.73.26");
		assertTrue("Should match", result);
	}
	
	/**
	 * Are AEKOS records not matched?
	 */
	@Test
	public void testDoesntMatchAnyFilters03() {
		FindIdsRig objectUnderTest = getObjectUnderTestWithPreconfiguredIncludeFilters();
		boolean result = objectUnderTest.matchesAnyFilter("aekos.org.au/collection/gov.au/abares/gcrs_SA");
		assertFalse("Should NOT match", result);
	}
	
	private FindIdsRig getObjectUnderTestWithPreconfiguredIncludeFilters() {
		List<String> filters = new ArrayList<String>();
		filters.addAll(Arrays.asList(
			new String[] {
				"^datalibrarian\\..+",
				"^ltern2\\..+"
			}
		));
		return new FindIdsRig(filters);
	}
}
