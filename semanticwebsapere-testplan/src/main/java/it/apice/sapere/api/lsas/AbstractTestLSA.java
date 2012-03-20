package it.apice.sapere.api.lsas;

import it.apice.sapere.api.AbstractModelTest;
import it.apice.sapere.api.nodes.SAPEREAgent;
import it.apice.sapere.api.nodes.SAPERENode;

import java.net.URI;

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
public abstract class AbstractTestLSA extends AbstractModelTest {

	/**
	 * <p>
	 * Tests LSA invariants.
	 * </p>
	 */
	@Test
	public final void testLSA() {
		final SAPEREAgent agent = createAgent();
		final LSA lsa = createFactory().createLSA();

		Assert.assertTrue(lsa.isSynthetic());
		Assert.assertFalse(lsa.isOwnedBy(agent));
		Assert.assertNotNull(lsa.getLSAId());
		Assert.assertNotNull(lsa.getSemanticDescription());

		final LSA lsa2 = createFactory().createLSA(agent);

		Assert.assertFalse(lsa2.isSynthetic());
		Assert.assertTrue(lsa2.isOwnedBy(agent));
		Assert.assertNotNull(lsa2.getLSAId());
		Assert.assertNotNull(lsa2.getSemanticDescription());

		try {
			lsa.isOwnedBy(null);
			Assert.fail("Cannot check if NULL owns an LSA, "
					+ "please raise a RuntimeException");
		} catch (Exception ex) {
			Assert.assertTrue(true);
		}

		Assert.assertFalse(lsa.equals(lsa2));
		Assert.assertFalse(lsa.hashCode() == lsa2.hashCode());

		Assert.assertTrue(lsa.equals(lsa));
		Assert.assertTrue(lsa2.equals(lsa2));
		
		final LSA clonedLSA = lsa2.clone();
		Assert.assertEquals(lsa2, clonedLSA);
		Assert.assertTrue(lsa2.hashCode() == clonedLSA.hashCode());
	}

	/**
	 * <p>
	 * Creates a new SAPEREAgent.
	 * </p>
	 * 
	 * @return A new mock SAPEREAgent
	 */
	private SAPEREAgent createAgent() {
		return createAgent(null);
	}

	/**
	 * <p>
	 * Creates a new SAPEREAgent with a specific id.
	 * </p>
	 * 
	 * @param id
	 *            The id of the new agent
	 * @return A new mock SAPEREAgent
	 */
	private SAPEREAgent createAgent(final URI id) {
		return new SAPEREAgent() {

			@Override
			public void spawn() {

			}

			@Override
			public boolean isRunning() {
				return true;
			}

			@Override
			public SAPERENode getNode() {
				return null;
			}

			@Override
			public URI getId() {
				return id;
			}
		};
	}

}
