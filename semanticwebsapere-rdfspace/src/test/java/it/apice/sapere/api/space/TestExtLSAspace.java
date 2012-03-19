package it.apice.sapere.api.space;

import it.apice.sapere.api.ExtSAPEREFactory;
import it.apice.sapere.api.SAPEREFactory;
import it.apice.sapere.api.impl.SAPEREFactoryImpl;
import it.apice.sapere.nodes.impl.SAPERENodeImpl;
import it.apice.sapere.space.impl.LSAspaceImpl;

import java.net.URI;

/**
 * <p>
 * Tests case.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class TestExtLSAspace extends AbstractTestExtLSAspace {

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
	public TestExtLSAspace() throws Exception {
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
	protected final ExtLSAspace createSpace() {
		return new LSAspaceImpl(factory);
	}

}
