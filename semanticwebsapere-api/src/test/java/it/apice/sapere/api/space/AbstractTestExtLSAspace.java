package it.apice.sapere.api.space;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.apice.sapere.api.AbstractModelTest;
import it.apice.sapere.api.SAPEREFactory;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.nodes.SAPEREException;
import it.apice.sapere.api.space.observation.LSAEvent;
import it.apice.sapere.api.space.observation.LSAObserver;
import it.apice.sapere.api.space.observation.SpaceEvent;
import it.apice.sapere.api.space.observation.SpaceObserver;
import it.apice.sapere.api.space.observation.SpaceOperationType;

import java.net.URI;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Test case for Extended LSA-space entity.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestExtLSAspace extends AbstractModelTest {

	/** Reference to SAPERE Factory. */
	private final transient SAPEREFactory factory = createFactory();

	/** Reference to tested space. */
	private transient ExtLSAspace space;

	/** Reference to Observer. */
	private transient Observer obs;

	/** Reference to an LSAObserver. */
	private transient LSAObserver lsaObs = createLSAObserver();

	/**
	 * Tests INJECT exceptions.
	 */
	@Test
	public final void testInjectExceptions() {
		SpaceEvent ev = null;
		try {
			space.inject(null);
			fail("Uncatched exception on INJECT!");
		} catch (Exception ex) {
			ev = obs.lookup(SpaceOperationType.DROPPED_REQUEST);
			assertNotNull(ev);
			obs.checked(ev);
		}

		try {
			final LSA lsa = factory.createLSA();
			space.inject(lsa);
			assertTrue(true);
			space.inject(lsa);
			ev = obs.lookup(SpaceOperationType.DROPPED_REQUEST);
			assertNotNull(ev);
			obs.checked(ev);
			fail("Double insertion should be avoided and notified");
		} catch (Exception ex) {
			ev = obs.lookup(SpaceOperationType.DROPPED_REQUEST);
			assertNotNull(ev);
			obs.checked(ev);
		}
	}

	/**
	 * Tests READ exceptions.
	 */
	@Test
	public final void testReadExceptions() {
		SpaceEvent ev = null;
		try {
			space.read(null);
			fail("Uncatched exception on READ!");
		} catch (Exception ex) {
			ev = obs.lookup(SpaceOperationType.DROPPED_REQUEST);
			assertNotNull(ev);
			obs.checked(ev);
		}

		try {
			final LSAid id = factory.createLSAid();
			space.read(id);
			fail("When trying to read something that is "
					+ "not in the LSA-space an exception should be raised");
		} catch (Exception ex) {
			ev = obs.lookup(SpaceOperationType.DROPPED_REQUEST);
			assertNotNull(ev);
			obs.checked(ev);
		}
	}

	/**
	 * Tests REMOVE exceptions.
	 */
	@Test
	public final void testRemoveExceptions() {
		SpaceEvent ev = null;
		try {
			space.remove(null);
			fail("Uncatched exception on REMOVE!");
		} catch (Exception ex) {
			ev = obs.lookup(SpaceOperationType.DROPPED_REQUEST);
			assertNotNull(ev);
			obs.checked(ev);
		}

		try {
			final LSA lsa = factory.createLSA();
			space.remove(lsa);
			fail("Cannot remove something that is not in the LSA-space");
		} catch (Exception ex) {
			ev = obs.lookup(SpaceOperationType.DROPPED_REQUEST);
			assertNotNull(ev);
			obs.checked(ev);
		}
	}

	/**
	 * Tests UPDATE exceptions.
	 */
	@Test
	public final void testUpdateExceptions() {
		SpaceEvent ev = null;
		try {
			space.update(null);
			fail("Uncatched exception on UPDATE!");
		} catch (Exception ex) {
			ev = obs.lookup(SpaceOperationType.DROPPED_REQUEST);
			assertNotNull(ev);
			obs.checked(ev);
		}

		try {
			final LSA lsa = factory.createLSA();
			space.update(lsa);
			fail("Cannot update something that is not in the LSA-space");
		} catch (Exception ex) {
			ev = obs.lookup(SpaceOperationType.DROPPED_REQUEST);
			assertNotNull(ev);
			obs.checked(ev);
		}
	}

	/**
	 * Tests OBSERVE exceptions.
	 */
	@Test
	public final void testObserveExceptions() {
		SpaceEvent ev = null;
		try {
			space.observe(null, lsaObs);
			fail("Should observe something");
		} catch (Exception ex) {
			ev = obs.lookup(SpaceOperationType.DROPPED_REQUEST);
			assertNotNull(ev);
			obs.checked(ev);
		}

		try {
			final LSAid id = factory.createLSAid();
			space.observe(id, null);
			fail("Cannot avoid observer specification");
		} catch (Exception ex) {
			ev = obs.lookup(SpaceOperationType.DROPPED_REQUEST);
			assertNotNull(ev);
			obs.checked(ev);
		}

		try {
			final LSA lsa = factory.createLSA();
			space.inject(lsa);
			space.observe(lsa.getLSAId(), null);
			fail("Cannot avoid observer specification (2)");
		} catch (Exception ex) {
			ev = obs.lookup(SpaceOperationType.DROPPED_REQUEST);
			assertNotNull(ev);
			obs.checked(ev);
		}
	}

	/**
	 * Tests IGNORE exceptions.
	 */
	@Test
	public final void testIgnoreExceptions() {
		SpaceEvent ev = null;
		try {
			space.ignore(null, lsaObs);
			fail("Should ignore the observation something");
		} catch (Exception ex) {
			ev = obs.lookup(SpaceOperationType.DROPPED_REQUEST);
			assertNotNull(ev);
			obs.checked(ev);
		}

		try {
			final LSAid id = factory.createLSAid();
			space.ignore(id, null);
			fail("Cannot avoid registered observer specification");
		} catch (Exception ex) {
			ev = obs.lookup(SpaceOperationType.DROPPED_REQUEST);
			assertNotNull(ev);
			obs.checked(ev);
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
		SpaceEvent ev = null;
		try {
			final LSA lsa = factory.createLSA();
			final LSAObserver sObs = createScenarioObserver(lsa);

			// 1. Register observer
			space.observe(lsa.getLSAId(), sObs);
			ev = obs.lookup(SpaceOperationType.SYSTEM_ACTION);
			assertNotNull(ev);
			obs.checked(ev);

			// 2. Inject and read the same LSA
			assertEquals(space.inject(lsa).read(lsa.getLSAId()), lsa);
			ev = obs.lookup(SpaceOperationType.SYSTEM_ACTION);
			assertNotNull(ev);
			obs.checked(ev);

			// 3. Update the LSA
			lsa.getSemanticDescription().addProperty(
					factory.createProperty(new URI(
							"http://localhost:8080/sapere#prop"), factory
							.createPropertyValue(true)));
			space.update(lsa);
			ev = obs.lookup(SpaceOperationType.SYSTEM_ACTION);
			assertNotNull(ev);
			obs.checked(ev);

			// 4. Remove the LSA
			space.remove(lsa);
			ev = obs.lookup(SpaceOperationType.SYSTEM_ACTION);
			assertNotNull(ev);
			obs.checked(ev);

			// 5. Ignore the LSA
			space.ignore(lsa.getLSAId(), sObs);
			ev = obs.lookup(SpaceOperationType.SYSTEM_ACTION);
			assertNotNull(ev);
			obs.checked(ev);
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

			/** Then UPDATE. */
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
							SpaceOperationType.SYSTEM_ACTION);
					break;
				case UPDATE:
					assertEquals(ev.getOperationType(),
							SpaceOperationType.SYSTEM_ACTION);
					break;
				case REMOVE:
					assertEquals(ev.getOperationType(),
							SpaceOperationType.SYSTEM_ACTION);
					break;
				default:
					fail("Unexpected LSAEvent: " + ev);
					break;
				}
			}
		};
	}

	/**
	 * setUp().
	 */
	@Before
	public final void runBeforeEveryTest() {
		space = createSpace();
		obs = new Observer();
		space.addSpaceObserver(obs);
	}

	/**
	 * tearDown().
	 */
	@After
	public final void runAfterEveryTest() {
		space.removeSpaceObserver(obs);
	}

	/**
	 * <p>
	 * Should return a valid ExtLSAspace.
	 * </p>
	 * 
	 * @return An ExtLSAspace
	 */
	protected abstract ExtLSAspace createSpace();

	/**
	 * <p>
	 * Utility class that will observe the space behaviour and providing
	 * information to test case.
	 * </p>
	 * 
	 * @author Paolo Contessi
	 * 
	 */
	private static class Observer implements SpaceObserver {

		/** List of occurred events, ordered by time. */
		private final transient Queue<SpaceEvent> events = new LinkedList<SpaceEvent>();

		/** Ensures Thread-Safety. */
		private final transient Lock mutex = new ReentrantLock();

		@Override
		public void eventOccurred(final SpaceEvent ev) {
			mutex.lock();
			try {
				events.add(ev);
			} finally {
				mutex.unlock();
			}
		}

		/**
		 * <p>
		 * Searches the first of occurrence in time of an event of a specific
		 * type.
		 * </p>
		 * 
		 * @param type
		 *            Type of the event
		 * @return The event, if found, <code>null</code> otherwise
		 */
		public SpaceEvent lookup(final SpaceOperationType type) {
			mutex.lock();
			try {
				for (SpaceEvent ev : events) {
					if (ev.getOperationType().equals(type)) {
						return ev;
					}
				}

				return null;
			} finally {
				mutex.unlock();
			}
		}

		/**
		 * <p>
		 * Removes the event from the list, because it has been verified.
		 * </p>
		 * 
		 * @param ev
		 *            The event that has been verified
		 */
		public void checked(final SpaceEvent ev) {
			mutex.lock();
			try {
				events.remove(ev);
			} finally {
				mutex.unlock();
			}
		}
	}

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
