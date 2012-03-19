package it.apice.sapere.api.space;

import it.apice.sapere.api.ExtSAPEREFactory;
import it.apice.sapere.api.SAPEREFactory;
import it.apice.sapere.api.impl.SAPEREFactoryImpl;
import it.apice.sapere.api.nodes.SAPEREAgent;
import it.apice.sapere.api.nodes.SAPEREAgentBehaviour;
import it.apice.sapere.api.nodes.SAPEREAgentTerminateException;
import it.apice.sapere.api.nodes.SAPERENode;
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
public class TestLSAspace extends AbstractTestLSAspace {

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
	public TestLSAspace() throws Exception {
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
	protected final LSAspace createSpace() {
		return new LSAspaceImpl(factory);
	}

	@Override
	protected final SAPEREAgent createAgent() {
		try {
			return factory.createAgent(
					new URI("http://www.sapere-project.eu/sapere#agent"
							+ System.currentTimeMillis()),
					new SAPEREAgentBehaviour() {

						@Override
						public void behave(final LSAspace lsaSpace,
								final SAPERENode actualNode)
								throws SAPEREAgentTerminateException {

						}
					});
		} catch (Exception ex) {
			return null;
		}
	}

}
