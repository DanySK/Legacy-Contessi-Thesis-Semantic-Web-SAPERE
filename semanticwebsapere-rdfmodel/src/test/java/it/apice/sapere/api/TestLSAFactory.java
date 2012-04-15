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
	private transient PrivilegedLSAFactory factory;

	@Override
	protected final LSAFactory createFactory() {
		if (factory == null) {
			factory = initFactory();
		}

		return factory;
	}

	@Override
	protected final PrivilegedLSAFactory createPrivilegedFactory() {
		if (factory == null) {
			factory = initFactory();
		}

		return factory;
	}

	/**
	 * <p>
	 * Initializes the factory.
	 * </p>
	 *
	 * @return Instance to the tested factory
	 */
	private PrivilegedLSAFactory initFactory() {
		return new LSAFactoryImpl(
				"http://www.sapere-project.eu/sapere#node"
						+ System.currentTimeMillis());
	}
}
