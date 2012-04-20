package it.apice.sapere.api;

import it.apice.sapere.api.impl.LSAFactoryImpl;
import it.apice.sapere.api.impl.LSAParserImpl;

/**
 * <p>
 * Test LSA Parser.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class TestLSAParser extends AbstractTestLSAParser {

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
	public TestLSAParser() throws Exception {
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

	@Override
	protected final LSAParser createParser(final PrivilegedLSAFactory fact) {
		return new LSAParserImpl(fact);
	}

}
