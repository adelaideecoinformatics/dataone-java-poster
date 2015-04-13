package au.org.ecoinformatics.eml.poster.rigs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FindIdsRigTest {

	/**
	 * Can we match LTERN records?
	 */
	@Test
	public void testDoesntMatchAnyFilters01() {
		boolean result = FindIdsRig.matchesAnyFilter("ltern2.105.17");
		assertTrue("Should match", result);
	}
	
	/**
	 * Can we match a datalibrarian record?
	 */
	@Test
	public void testDoesntMatchAnyFilters02() {
		boolean result = FindIdsRig.matchesAnyFilter("datalibrarian.73.26");
		assertTrue("Should match", result);
	}
	
	/**
	 * Are AEKOS records not matched?
	 */
	@Test
	public void testDoesntMatchAnyFilters03() {
		boolean result = FindIdsRig.matchesAnyFilter("aekos.org.au/collection/gov.au/abares/gcrs_SA");
		assertFalse("Should NOT match", result);
	}
}
