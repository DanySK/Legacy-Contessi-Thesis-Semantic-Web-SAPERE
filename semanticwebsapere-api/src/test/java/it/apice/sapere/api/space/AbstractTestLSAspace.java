package it.apice.sapere.api.space;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.apice.sapere.api.AbstractModelTest;
import it.apice.sapere.api.SAPEREFactory;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.nodes.SAPEREAgent;
import it.apice.sapere.api.nodes.SAPEREException;
import it.apice.sapere.api.space.observation.LSAEvent;
import it.apice.sapere.api.space.observation.LSAObserver;
import it.apice.sapere.api.space.observation.SpaceOperationType;

import java.net.URI;

import org.junit.Test;

/**
 * <p>
 * Test case for Extended LSA-space entity.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestLSAspace extends AbstractModelTest {

	/** Reference to SAPERE Factory. */
	private final transient SAPEREFactory factory = createFactory();

	/** Reference to tested space. */
	private transient LSAspace space = createSpace();

	/** Reference to an agent. */
	private transient SAPEREAgent agent = createAgent();

	/** Reference to an LSA observer. */
	private transient LSAObserver obs = createLSAObserver();

	/**
	 * Tests INJECT exceptions.
	 */
	@Test
	public final void testInjectExceptions() {
		try {
			space.inject(null);
			fail("Uncatched exception on INJECT!");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			final LSA lsa = factory.createLSA();
			space.inject(lsa);
			assertTrue(true);
			space.inject(lsa);
			fail("Double insertion should be avoided and notified");
		} catch (Exception ex) {
			assertTrue(true);
		}
	}

	/**
	 * Tests READ exceptions.
	 */
	@Test
	public final void testReadExceptions() {
		try {
			space.read(null);
			fail("Uncatched exception on READ!");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			final LSAid id = factory.createLSAid();
			space.read(id);
			fail("When trying to read something that is "
					+ "not in the LSA-space an exception should be raised");
		} catch (Exception ex) {
			assertTrue(true);
		}
	}

	/**
	 * Tests REMOVE exceptions.
	 */
	@Test
	public final void testRemoveExceptions() {
		try {
			space.remove(null, agent);
			fail("Uncatched exception on REMOVE!");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			space.remove(null, null);
			fail("A requestor should be provided");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			final LSA lsa = factory.createLSA(agent);
			space.remove(lsa, agent);
			fail("Cannot remove something that has not been injected");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			final LSA sysLsa = factory.createLSA();
			space.remove(sysLsa, agent);
			fail("Cannot remove something that requestor does not own");
		} catch (Exception ex) {
			assertTrue(true);
		}
	}

	/**
	 * Tests UPDATE exceptions.
	 */
	@Test
	public final void testUpdateExceptions() {
		try {
			space.update(null, agent);
			fail("Uncatched exception on UPDATE!");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			final LSA lsa = factory.createLSA();
			space.update(lsa, null);
			fail("Cannot avoid requestor specification");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			final LSA lsa = factory.createLSA();
			space.update(lsa, agent);
			fail("Cannot update something that is not in the LSA-space");
		} catch (Exception ex) {
			assertTrue(true);
		}
	}

	/**
	 * Tests OBSERVE exceptions.
	 */
	@Test
	public final void testObserveExceptions() {
		try {
			space.observe(null, agent, obs);
			fail("Should observe something");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			final LSAid id = factory.createLSAid();
			space.observe(id, null, obs);
			fail("Cannot hide requestor identity");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			final LSAid id = factory.createLSAid();
			space.observe(id, agent, null);
			fail("Cannot avoid observer specification");
		} catch (Exception ex) {
			assertTrue(true);
		}
	}

	/**
	 * Tests IGNORE exceptions.
	 */
	@Test
	public final void testIgnoreExceptions() {
		try {
			space.ignore(null, agent, obs);
			fail("Should observe something");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			final LSAid id = factory.createLSAid();
			space.ignore(id, null, obs);
			fail("Cannot hide requestor identity");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			final LSAid id = factory.createLSAid();
			space.ignore(id, agent, null);
			fail("Cannot avoid observer specification");
		} catch (Exception ex) {
			assertTrue(true);
		}
	}

	/**
	 * <p>
	 * Tests a normal scenario.
	 * </p>
	 * 
	 * @throws Exception
	 *             URI problem
	 */
	@Test
	public final void testScenario() throws Exception {
		try {
			final LSA lsa = factory.createLSA(agent);
			final LSAObserver sObs = createScenarioObserver(lsa);

			// 1. Register observer
			space.observe(lsa.getLSAId(), agent, sObs);

			// 2. Inject and read the same LSA
			assertEquals(space.inject(lsa).read(lsa.getLSAId()), lsa);

			// 3. Update the LSA
			lsa.getSemanticDescription().addProperty(
					factory.createProperty(new URI(
							"http://localhost:8080/sapere#prop"), factory
							.createPropertyValue(true)));
			space.update(lsa, agent);

			// 4. Remove the LSA
			space.remove(lsa, agent);

			// 5. Ignore the LSA
			space.ignore(lsa.getLSAId(), agent, sObs);
		} catch (SAPEREException e) {
			e.printStackTrace();
			fail("Something went wrong during normal scenario "
					+ "(see stdout for details)");
		}
	}

	/**
	 * <p>
	 * Creates an observer for the normal scenario.
	 * </p>
	 * 
	 * @param observedLSA
	 *            The LSA to be observed
	 * @return An LSAObserver
	 */
	private LSAObserver createScenarioObserver(final LSA observedLSA) {
		return new LSAObserver() {

			/** Counts occurred events. */
			private transient int evCounter = 0;

			/** First INJECT. */
			private static final transient int INJECT = 1;

			/** First UPDATE. */
			private static final transient int UPDATE = 2;

			/** Finally REMOVE. */
			private static final transient int REMOVE = 3;

			@Override
			public void eventOccurred(final LSAEvent ev) {
				evCounter++;

				assertEquals(ev.getLSA(), observedLSA);

				switch (evCounter) {
				case INJECT:
					assertEquals(ev.getOperationType(),
							SpaceOperationType.AGENT_INJECT);
					break;
				case UPDATE:
					assertEquals(ev.getOperationType(),
							SpaceOperationType.AGENT_UPDATE);
					break;
				case REMOVE:
					assertEquals(ev.getOperationType(),
							SpaceOperationType.AGENT_REMOVE);
					break;
				default:
					fail("Unexpected LSAEvent: " + ev);
					break;
				}
			}
		};
	}

	/**
	 * <p>
	 * Should return a valid LSAspace.
	 * </p>
	 * 
	 * @return An LSAspace
	 */
	protected abstract LSAspace createSpace();

	/**
	 * <p>
	 * Should return a valid SAPEREAgent.
	 * </p>
	 * 
	 * @return A SAPEREAgent
	 */
	protected abstract SAPEREAgent createAgent();

	/**
	 * <p>
	 * Creates a mock LSA observer that does nothing.
	 * </p>
	 * 
	 * @return A reference to an LSAObserver
	 */
	private LSAObserver createLSAObserver() {
		return new LSAObserver() {

			@Override
			public void eventOccurred(final LSAEvent ev) {

			}
		};
	}
}
