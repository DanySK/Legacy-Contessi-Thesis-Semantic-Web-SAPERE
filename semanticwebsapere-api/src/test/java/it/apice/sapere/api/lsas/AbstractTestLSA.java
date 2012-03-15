package it.apice.sapere.api.lsas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;

import it.apice.sapere.api.AbstractModelTest;
import it.apice.sapere.api.SAPEREFactory;
import it.apice.sapere.api.nodes.SAPEREAgent;
import it.apice.sapere.api.nodes.SAPERENode;

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

	/** Reference to SAPEREFactory. */
	private final transient SAPEREFactory factory = createFactory();

	/**
	 * <p>
	 * Tests LSA invariants.
	 * </p>
	 */
	@Test
	public final void testLSA() {
		final SAPEREAgent agent = createAgent();
		final LSA lsa = factory.createLSA();

		assertTrue(lsa.isSynthetic());
		assertFalse(lsa.isOwnedBy(agent));
		assertNotNull(lsa.getLSAId());
		assertNotNull(lsa.getSemanticDescription());

		final LSA lsa2 = factory.createLSA(agent);

		assertFalse(lsa2.isSynthetic());
		assertTrue(lsa2.isOwnedBy(agent));
		assertNotNull(lsa2.getLSAId());
		assertNotNull(lsa2.getSemanticDescription());

		try {
			lsa.isOwnedBy(null);
			fail("Cannot check if NULL owns an LSA, "
					+ "please raise a RuntimeException");
		} catch (Exception ex) {
			assertTrue(true);
		}

		assertFalse(lsa.equals(lsa2));
		assertFalse(lsa.hashCode() == lsa2.hashCode());

		assertTrue(lsa.equals(lsa));
		assertTrue(lsa2.equals(lsa2));
		
		final LSA clonedLSA = lsa2.clone();
		assertEquals(lsa2, clonedLSA);
		assertTrue(lsa2.hashCode() == clonedLSA.hashCode());
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
