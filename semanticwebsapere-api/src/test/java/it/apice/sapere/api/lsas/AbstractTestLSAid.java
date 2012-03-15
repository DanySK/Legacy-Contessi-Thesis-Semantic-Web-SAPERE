package it.apice.sapere.api.lsas;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import it.apice.sapere.api.AbstractModelTest;
import it.apice.sapere.api.SAPEREFactory;
import it.apice.sapere.api.lsas.LSAid;

import org.junit.Test;

/**
 * <p>
 * Test case for LSAid entity.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestLSAid extends AbstractModelTest {

	/** Determines how many LSAid should be created and tested. */
	private static final transient int NUMBER_OF_TESTS = 100;

	/**
	 * <p>
	 * Tests that each LSA-id is unique.
	 * </p>
	 */
	@Test
	public final void testUniqueness() {
		final SAPEREFactory factory = createFactory();
		final LSAid[] ids = new LSAid[NUMBER_OF_TESTS];

		for (int i = 0; i < NUMBER_OF_TESTS; i++) {
			ids[i] = factory.createLSAId();
			assertNotNull("Why no LSAid has been returned?", ids[i]);
			assertNotNull("Why the ID is null?", ids[i].getId());
		}

		for (int i = 0; i < NUMBER_OF_TESTS; i++) {
			for (int j = i + 1; j < NUMBER_OF_TESTS; j++) {
				assertFalse("Each id should be different",
						ids[i].equals(ids[j]));
				assertFalse("Equivalent should be symmetric",
						ids[j].equals(ids[i]));
			}
		}
	}

}
