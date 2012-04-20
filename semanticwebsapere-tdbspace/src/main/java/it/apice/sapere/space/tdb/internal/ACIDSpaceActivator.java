package it.apice.sapere.space.tdb.internal;

import java.util.Hashtable;

import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.space.tdb.impl.ACIDLSAspaceImpl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * <p>
 * SemanticWebSAPERE - RDF Space Activator.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class ACIDSpaceActivator implements BundleActivator {

	/** LSA-space Service registration. */
	private transient ServiceRegistration<?> lsaSpaceServiceReg;

	/** Direct reference to LSA-space. */
	private transient ACIDLSAspaceImpl space;

	@Override
	public final void start(final BundleContext context) throws Exception {
		space = new ACIDLSAspaceImpl(retrieveLSAFactoryService(context));
		lsaSpaceServiceReg = context.registerService(LSAspace.class.getName(),
				space, declareProps());
	}

	/**
	 * <p>
	 * Defines properties of services that will be registered.
	 * </p>
	 * 
	 * @return Service properties
	 */
	private Hashtable<String, ?> declareProps() {
		final Hashtable<String, Object> props = new Hashtable<String, Object>();

		props.put("sapere.rdf-based", Boolean.TRUE);
		props.put("sapere.acid-transactions", Boolean.TRUE);

		return props;
	}

	/**
	 * <p>
	 * Retrieves the LSA-Factory Service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle context
	 * @return A reference to a PrivilegedLSAFactory
	 */
	private PrivilegedLSAFactory retrieveLSAFactoryService(
			final BundleContext context) {
		final ServiceReference<PrivilegedLSAFactory> ref = context
				.getServiceReference(PrivilegedLSAFactory.class);
		if (ref == null) {
			throw new IllegalStateException("Cannot retrieve a reference to "
					+ "PrivilegedLSAFactory service.");
		}

		return context.getService(ref);
	}

	@Override
	public final void stop(final BundleContext context) throws Exception {
		if (lsaSpaceServiceReg != null) {
			context.ungetService(lsaSpaceServiceReg.getReference());
			lsaSpaceServiceReg = null;
			space.shutdown();
		}
	}

}
