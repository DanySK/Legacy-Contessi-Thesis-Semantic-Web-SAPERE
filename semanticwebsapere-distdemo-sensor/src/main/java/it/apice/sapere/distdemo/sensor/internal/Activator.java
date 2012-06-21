package it.apice.sapere.distdemo.sensor.internal;

import it.apice.sapere.api.management.ReactionManager;
import it.apice.sapere.api.node.agents.SAPEREAgentsFactory;
import it.apice.sapere.distdemo.sensor.impl.SensorPlatform;

import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * <p>
 * This is the activator of the Sensor Platform for the Demo Scenario.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class Activator implements BundleActivator {

	/** List of service references. */
	private final transient List<ServiceReference<?>> services = 
			new LinkedList<ServiceReference<?>>();

	@Override
	public void start(final BundleContext context) throws Exception {
		ReactionManager manager = null;
		SAPEREAgentsFactory aFactory = null;

		final ServiceReference<ReactionManager> ref = context
				.getServiceReference(ReactionManager.class);
		if (ref != null) {
			services.add(ref);
			manager = context.getService(ref);
			assert manager != null;
		}
		
		final ServiceReference<SAPEREAgentsFactory> ref2 = context
				.getServiceReference(SAPEREAgentsFactory.class);
		if (ref2 != null) {
			services.add(ref2);
			aFactory = context.getService(ref2);
			assert aFactory != null;
		}

		final SensorPlatform bl = new SensorPlatform(manager, aFactory);
		bl.execute();
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		for (ServiceReference<?> ref : services) {
			context.ungetService(ref);
		}
	}

}
