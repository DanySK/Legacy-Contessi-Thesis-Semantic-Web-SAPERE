package it.apice.sapere.api.nodes;

import it.apice.sapere.api.ExtSAPEREFactory;
import it.apice.sapere.api.SAPEREFactory;
import it.apice.sapere.api.impl.SAPEREFactoryImpl;
import it.apice.sapere.nodes.impl.SAPERENodeImpl;

import java.net.URI;

/**
 * <p>
 * Test case.
 * </p>
 *
 * @author Paolo Contessi
 *
 */
public class TestSAPERENode extends AbstractTestSAPERENode {

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
	public TestSAPERENode() throws Exception {
		factory = new SAPEREFactoryImpl(new SAPERENodeImpl(new URI(
				"http://www.sapere-project.eu/sapere#node"
						+ System.currentTimeMillis())));
	}

	@Override
	protected final SAPERENode createNode(final URI nodeId) throws Exception {
		return new SAPERENodeImpl(nodeId);
	}

	@Override
	protected final SAPEREFactory createFactory() {
		return factory;
	}

	@Override
	protected final ExtSAPEREFactory createExtFactory() {
		return factory;
	}

}
