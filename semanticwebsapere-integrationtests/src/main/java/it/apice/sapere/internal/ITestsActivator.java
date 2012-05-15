package it.apice.sapere.internal;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.node.LogUtils;
import it.apice.sapere.node.agents.NodeServices;
import it.apice.sapere.node.agents.SAPEREAgentSpec;
import it.apice.sapere.node.agents.SAPEREAgentsFactory;
import it.apice.sapere.node.agents.SAPERESysAgentSpec;

import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * <p>
 * Bundle Activator class, used as warning and in order to avoid build cycle
 * failure.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class ITestsActivator implements BundleActivator {

	/** Sleep time between agents spawning. */
	private static final int SLEEP_TIME = 2000;

	@Override
	public final void start(final BundleContext context) throws Exception {
		LogFactory.getLog(ITestsActivator.class).warn(
				"THIS IS AN INTEGRATION TESTS BUNDLE. "
						+ "IT SHOULD NOT BE DEPLOYED WITH THE APPLICATION.");

		final ServiceReference<SAPEREAgentsFactory> ref = context
				.getServiceReference(SAPEREAgentsFactory.class);
		if (ref != null) {
			final SAPEREAgentsFactory fact = context.getService(ref);
			fact.createAgent("test_agent", getSpec()).spawn();
			Thread.sleep(SLEEP_TIME);
			fact.createSysAgent("test_sys_agent", getSysSpec()).spawn();
		}
	}

	@Override
	public void stop(final BundleContext context) throws Exception {

	}

	/**
	 * <p>
	 * Retrieves an agent specification.
	 * </p>
	 * 
	 * @return A {@link SAPEREAgentSpec}
	 */
	private SAPEREAgentSpec getSpec() {
		return new SAPEREAgentSpec() {

			@Override
			public void behaviour(final LSAFactory factory,
					final LSAspace space, final LogUtils out) throws Exception {
				out.log("Hello World!");
				
				out.log("Bye bye.");
			}
		};
	}
	
	/**
	 * <p>
	 * Retrieves a system agent specification.
	 * </p>
	 * 
	 * @return A {@link SAPEREAgentSpec}
	 */
	private SAPERESysAgentSpec getSysSpec() {
		return new SAPERESysAgentSpec() {

			@Override
			public void behaviour(final NodeServices services, 
					final LogUtils out)
					throws Exception {
				out.log("Hello World!");

				out.log("Bye bye.");
			}
		};
	}
}
