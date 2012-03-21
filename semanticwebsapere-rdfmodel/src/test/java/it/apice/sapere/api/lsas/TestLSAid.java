package it.apice.sapere.api.lsas;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.impl.LSAFactoryImpl;

/**
 * <p>
 * Test LSA-id.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class TestLSAid extends AbstractTestLSAid {

	/** Factory reference. */
	private final transient PrivilegedLSAFactory factory;

	/**
	 * <p>
	 * Builds a new Test Case.
	 * </p>
	 * 
	 * @throws Exception
	 *             Initialization problems
	 */
	public TestLSAid() throws Exception {
		factory = new LSAFactoryImpl(
				"http://www.sapere-project.eu/sapere#node"
						+ System.currentTimeMillis());
	}

	@Override
	protected final LSAFactory createFactory() {
		return factory;
	}

	@Override
	protected final PrivilegedLSAFactory createPrivilegedFactory() {
		return factory;
	}

}
