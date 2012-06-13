package it.apice.sapere.profiling.internal;

import it.apice.sapere.api.node.agents.SAPEREAgentsFactory;
import it.apice.sapere.api.node.agents.SAPERESysAgentSpec;

import java.io.File;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * <p>
 * Profiling Bundle Activator.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class SAPEREProfilingActivator implements BundleActivator {

	/** Profiling class (simple) name Property name. */
	private static final transient String CLASS_PROPERTY = 
			"sapere.profiling.classname";

	/** Source data file Property name. */
	private static final transient String SOURCE_PROPERTY = 
			"sapere.profiling.sourcefile";

	/** Profiling classes package. */
	private static final transient String CLASS_PACKAGE = 
			"it.apice.sapere.profiling.agents";

	@Override
	public void start(final BundleContext context) throws Exception {
		final String cName = String.format("%s.%s", CLASS_PACKAGE,
				context.getProperty(CLASS_PROPERTY));
		final String dataFile = context.getProperty(SOURCE_PROPERTY);
		
		System.out.println("Profiled class: " + cName);
		System.out.println("Source file: " + dataFile);

		final File file = new File(dataFile);
		if (!file.exists()) {
			System.err.println("Invalid file: " + file.getAbsolutePath());
			return;
		}

		SAPERESysAgentSpec spec = null;
		try {
			spec = (SAPERESysAgentSpec) Class.forName(cName)
					.getConstructor(File.class).newInstance(file);
		} catch (Exception ex) {
			System.err.println("Cannot load the Profiling Class: " + cName);
		}

		final ServiceReference<SAPEREAgentsFactory> ref = context
				.getServiceReference(SAPEREAgentsFactory.class);
		if (ref != null && spec != null) {
			final SAPEREAgentsFactory fact = context.getService(ref);
			
			// Starting profiling agent
			fact.createSysAgent("profiling_agent", spec).spawn();

			context.ungetService(ref);
		}
	}

	@Override
	public void stop(final BundleContext context) throws Exception {

	}

}
