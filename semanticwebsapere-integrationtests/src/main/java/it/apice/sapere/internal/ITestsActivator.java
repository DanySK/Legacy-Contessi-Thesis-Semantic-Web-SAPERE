package it.apice.sapere.internal;

import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

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

	@Override
	public final void start(final BundleContext context) throws Exception {
		LogFactory.getLog(ITestsActivator.class).warn(
				"THIS IS AN INTEGRATION TESTS BUNDLE. "
						+ "IT SHOULD NOT BE DEPLOYED WITH THE APPLICATION.");
	}

	@Override
	public void stop(final BundleContext context) throws Exception {

	}

}
