package it.apice.sapere.space;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.api.space.observation.LSAEvent;
import it.apice.sapere.api.space.observation.LSAObserver;
import it.apice.sapere.api.space.observation.SpaceOperationType;

import java.net.URI;

import junit.framework.TestCase;

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
public abstract class AbstractTestLSAspace extends TestCase {

	/** Reference to tested space. */
	private transient LSAspace space;

	/** Reference to tested space. */
	private transient LSAObserver lObs;

//	/** Reference to tested space. */
//	private transient SpaceObserverTester sObs;

	/**
	 * <p>
	 * Creates a new SAPEREFactory.
	 * </p>
	 * 
	 * @return A reference to a SAPEREFactory
	 */
	protected abstract LSAFactory createFactory();

	/**
	 * <p>
	 * Creates a new Extended SAPEREFactory.
	 * </p>
	 * 
	 * @return A reference to an ExtSAPEREFactory
	 */
	protected abstract PrivilegedLSAFactory createPrivilegedFactory();

	/**
	 * <p>
	 * Creates the space to be tested.
	 * </p>
	 */
	@Before
	public final void setUp() {
		space = createSpace();
		lObs = createLSAObserver();
//		sObs = createSpaceObserver();

		//space.addSpaceObserver(sObs);
	}

	/**
	 * <p>
	 * Cleans the space and cancels the Space Observer.
	 * </p>
	 */
	@After
	public final void tearDown() {
		//space.removeSpaceObserver(sObs);
//		sObs.reset();
		//space.clear();
	}

	/**
	 * Tests INJECT exceptions.
	 */
	@Test
	public final void testInjectExceptions() {
		try {
			space.inject(null);
			fail("Uncatched exception on INJECT!");
		} catch (Exception ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}

//		assertTrue(sObs.noEventOccurred());

		try {
			final LSA lsa = createFactory().createLSA();
			space.inject(lsa);
//			assertTrue(sObs.checkFirstOcc(SpaceOperationType.AGENT_INJECT));
			space.inject(lsa);
			fail("Double insertion should be avoided and notified");
		} catch (Exception ex) {
			assertTrue(ex instanceof SAPEREException);
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
			assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			space.read(createFactory().createLSAid());
			fail("When trying to read something that is "
					+ "not in the LSA-space an exception should be raised");
		} catch (Exception ex) {
			assertTrue(ex instanceof SAPEREException);
		}

//		assertTrue(sObs.noEventOccurred());
	}

	/**
	 * Tests REMOVE exceptions.
	 */
	@Test
	public final void testRemoveExceptions() {
		try {
			space.remove(null);
			fail("Should not accept to remove NULL");
		} catch (Exception ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			space.remove(createFactory().createLSA());
			fail("Should not accept to remove "
					+ "something that is not in the space");
		} catch (Exception ex) {
			assertTrue(ex instanceof SAPEREException);
		}

//		assertTrue(sObs.noEventOccurred());
	}

	/**
	 * Tests UPDATE exceptions.
	 */
	@Test
	public final void testUpdateExceptions() {
		try {
			space.update(null);
			fail("Should not accept to update NULL");
		} catch (Exception ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			space.update(createFactory().createLSA());
			fail("Should not accept to update "
					+ "something that is not in the space");
		} catch (Exception ex) {
			assertTrue(ex instanceof SAPEREException);
		}

//		assertTrue(sObs.noEventOccurred());
	}

	/**
	 * Tests OBSERVE exceptions.
	 */
	@Test
	public final void testObserveExceptions() {
		try {
			space.observe(null, lObs);
			fail("Should not accept to observe NULL");
		} catch (Exception ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			space.observe(createFactory().createLSAid(), lObs);
//			assertTrue(sObs.checkFirstOcc(SpaceOperationType.AGENT_ACTION));
		} catch (Exception ex) {
			fail("Should accept to observe "
					+ "something that is not in the space");
		}

		try {
			final LSA lsa = createFactory().createLSA();
			space.inject(lsa);
//			assertTrue(sObs.checkFirstOcc(SpaceOperationType.AGENT_INJECT));
			space.observe(lsa.getLSAId(), null);
			fail("Should not accept to register a NULL observer");
		} catch (Exception ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}

//		assertTrue(sObs.noEventOccurred());
	}

	/**
	 * Tests IGNORE exceptions.
	 */
	@Test
	public final void testIgnoreExceptions() {
		try {
			space.ignore(null, lObs);
			fail("Should not accept to ignore NULL");
		} catch (Exception ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}

//		assertTrue(sObs.noEventOccurred());

		try {
			space.ignore(createFactory().createLSAid(), lObs);
//			assertTrue(sObs.checkFirstOcc(SpaceOperationType.AGENT_ACTION));
		} catch (Exception ex) {
			fail("Ignoring something not observed could be dropped silently");
		}

		try {
			final LSA lsa = createFactory().createLSA();
			space.inject(lsa);
//			assertTrue(sObs.checkFirstOcc(SpaceOperationType.AGENT_INJECT));
			space.observe(lsa.getLSAId(), null);
			fail("Should not accept to unregister a NULL observer");
		} catch (Exception ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}

//		assertTrue(sObs.noEventOccurred());
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
			final LSA lsa = createFactory().createLSA();
			final LSAObserver scObs = createScenarioObserver(lsa);

//			assertTrue(sObs.noEventOccurred());

			// 1. Register observer
			space.observe(lsa.getLSAId(), scObs);

//			assertTrue(sObs.checkFirstOcc(SpaceOperationType.AGENT_ACTION));

			// 2. Inject and read the same LSA
			assertEquals(space.inject(lsa).read(lsa.getLSAId()), lsa);

//			assertTrue(sObs.checkFirstOcc(SpaceOperationType.AGENT_INJECT));
//			assertTrue(sObs.checkFirstOcc(SpaceOperationType.AGENT_READ));

			// 3. Update the LSA
			lsa.getSemanticDescription().addProperty(
					createFactory().createProperty(
							new URI("http://localhost:8080/sapere#prop"),
							createFactory().createPropertyValue(Boolean.TRUE)));
			space.update(lsa);

//			assertTrue(sObs.checkFirstOcc(SpaceOperationType.AGENT_UPDATE));

			// 4. Remove the LSA
			space.remove(lsa);

//			assertTrue(sObs.checkFirstOcc(SpaceOperationType.AGENT_REMOVE));

			// 5. Ignore the LSA
			space.ignore(lsa.getLSAId(), scObs);

//			assertTrue(sObs.checkFirstOcc(SpaceOperationType.AGENT_ACTION));
//			assertTrue(sObs.noEventOccurred());
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
	protected final LSAObserver createScenarioObserver(final LSA observedLSA) {
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

//	/**
//	 * <p>
//	 * Creates a mock Space observer that registers events occurred for testing
//	 * purposes.
//	 * </p>
//	 * 
//	 * @return A reference to an LSAObserver
//	 */
//	private SpaceObserverTester createSpaceObserver() {
//		return new SpaceObserverTester();
//	}

//	/**
//	 * <p>
//	 * Test utility.
//	 * </p>
//	 * 
//	 * @author Paolo Contessi
//	 * 
//	 */
//	private static class SpaceObserverTester implements SpaceObserver {
//
//		/** Event queue. */
//		private final transient List<SpaceOperationType> queue = 
//				new LinkedList<SpaceOperationType>();
//
//		@Override
//		public void eventOccurred(final SpaceEvent ev) {
//			queue.add(ev.getOperationType());
//		}
//
//		/**
//		 * <p>
//		 * Checks if an event occurred.
//		 * </p>
//		 * 
//		 * @param type
//		 *            Type of interesting event
//		 * @return True if occurred, false otherwise
//		 */
//		public boolean checkFirstOcc(final SpaceOperationType type) {
//			final int idx = queue.indexOf(type);
//			if (idx >= 0) {
//				queue.remove(idx);
//				return true;
//			}
//
//			return false;
//		}
//
//		/**
//		 * <p>
//		 * Clears event queue.
//		 * </p>
//		 */
//		public void reset() {
//			queue.clear();
//		}
//
//		/**
//		 * <p>
//		 * Checks if queue is empty.
//		 * </p>
//		 * 
//		 * @return True if empty, false otherwise
//		 */
//		public boolean noEventOccurred() {
//			return queue.isEmpty();
//		}
//	}
}
