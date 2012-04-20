package it.apice.sapere.api.lsas;

import it.apice.sapere.api.AbstractLSAModelTest;

import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * Test case for LSA entity.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestLSA extends AbstractLSAModelTest {

	/**
	 * <p>
	 * Tests LSA invariants.
	 * </p>
	 */
	@Test
	public final void testLSA() {
		final LSA lsa = createFactory().createLSA();

		Assert.assertNotNull(lsa.getLSAId());
		Assert.assertNotNull(lsa.getSemanticDescription());
		
		final LSA lsa2 = createFactory().createLSA();
		Assert.assertNotNull(lsa2.getLSAId());
		Assert.assertNotNull(lsa2.getSemanticDescription());

		Assert.assertFalse(lsa.equals(lsa2));
		Assert.assertFalse(lsa.hashCode() == lsa2.hashCode());
		
		final LSA clonedLSA = lsa2.clone();
		Assert.assertEquals(lsa2, clonedLSA);
		Assert.assertTrue(lsa2.hashCode() == clonedLSA.hashCode());
	}

}
