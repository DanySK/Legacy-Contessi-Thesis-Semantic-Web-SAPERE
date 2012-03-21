package it.apice.sapere.api;

import it.apice.sapere.api.impl.LSAFactoryImpl;

/**
 * <p>
 * Test LSA Factory.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class TestLSAFactory extends AbstractTestLSAFactory {

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
	public TestLSAFactory() throws Exception {
		factory = new LSAFactoryImpl("http://www.sapere-project.eu/sapere#node"
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
