package it.apice.sapere.api;

import it.apice.sapere.api.impl.SAPEREFactoryImpl;
import it.apice.sapere.api.impl.SAPEREParserImpl;
import it.apice.sapere.nodes.impl.SAPERENodeImpl;

import java.net.URI;

/**
 * <p>
 * Tests case.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class TestSAPEREParser extends AbstractTestSAPEREParser {

	/** Factory reference. */
	private final transient ExtSAPEREFactory factory;

	/**
	 * <p>
	 * Builds a new Test Case.
	 * </p>
	 * 
	 * @throws Exception
	 *             Initialization problems
	 */
	public TestSAPEREParser() throws Exception {
		factory = new SAPEREFactoryImpl(new SAPERENodeImpl(new URI(
				"http://www.sapere-project.eu/sapere#node"
						+ System.currentTimeMillis())));
	}

	@Override
	protected final SAPEREFactory createFactory() {
		return factory;
	}

	@Override
	protected final ExtSAPEREFactory createExtFactory() {
		return factory;
	}

	@Override
	protected final SAPEREParser createParser(final ExtSAPEREFactory fact) {
		return new SAPEREParserImpl(fact);
	}

}
