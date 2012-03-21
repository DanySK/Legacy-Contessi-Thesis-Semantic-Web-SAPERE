package it.apice.sapere.api;

import it.apice.sapere.api.impl.LSAFactoryImpl;

/**
 * <p>
 * Test Privileged LSA Factory.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class TestPrivilegedLSAFactory extends AbstractTestPrivilegedLSAFactory {

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
	public TestPrivilegedLSAFactory() throws Exception {
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
