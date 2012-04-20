package it.apice.sapere.api;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.apice.sapere.api.lsas.LSAid;

import org.junit.Test;

/**
 * <p>
 * Test case for PrivilegedLSAFactory entity.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestPrivilegedLSAFactory extends
		AbstractTestLSAFactory {

	/** Reference to an Privileged LSA Factory. */
	private final transient PrivilegedLSAFactory factory = 
			createPrivilegedFactory();

	/**
	 * Checks that unwanted behaviours are handled.
	 */
	@Test
	public final void testPrivilegedUnwanted() {
		try {
			factory.createLSA((LSAid) null);
			fail("Unwanted ExtLSA(null)");
		} catch (Exception ex) {
			assertTrue(true);
		}
	}
}
