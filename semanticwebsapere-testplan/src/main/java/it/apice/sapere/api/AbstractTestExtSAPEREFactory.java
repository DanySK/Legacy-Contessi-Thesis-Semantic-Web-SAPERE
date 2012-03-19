package it.apice.sapere.api;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.apice.sapere.api.lsas.LSAid;

import org.junit.Test;

/**
 * <p>
 * Test case for ExtSAPEREFactory entity.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestExtSAPEREFactory extends AbstractTestSAPEREFactory {

	/** Reference to an Extended SAPERE Factory. */
	private final transient ExtSAPEREFactory factory = createExtFactory();

	/**
	 * Checks that unwanted behaviours are handled.
	 */
	@Test
	public final void testUnwanted() {
		super.testUnwanted();

		try {
			factory.createLSA((LSAid) null);
			fail("Unwanted ExtLSA(null)");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			factory.createLSA(null, null);
			fail("Unwanted ExtLSA(null, null)");
		} catch (Exception ex) {
			assertTrue(true);
		}
	}
}
