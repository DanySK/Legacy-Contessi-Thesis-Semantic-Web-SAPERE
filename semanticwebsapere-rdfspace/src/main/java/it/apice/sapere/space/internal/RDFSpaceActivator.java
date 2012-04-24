package it.apice.sapere.space.internal;

import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.api.space.core.impl.LSACompilerImpl;
import it.apice.sapere.space.impl.LSAspaceImpl;

import java.util.Hashtable;

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
public class RDFSpaceActivator implements BundleActivator {

	/** LSA-space Service registration. */
	private transient ServiceRegistration<?> lsaSpaceServiceReg;

	@Override
	public final void start(final BundleContext context) throws Exception {
		System.out.println("SemanticWebSAPERE [RDFSpace]: Starting up..");
		lsaSpaceServiceReg = context.registerService(LSAspace.class.getName(),
				new LSAspaceImpl(new LSACompilerImpl(),
						retrieveLSAParserService(context)), declareProps());
		System.out.println("SemanticWebSAPERE [RDFSpace]: "
				+ "LSA-space REGISTERED.");
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
		props.put("sapere.acid-transactions", Boolean.FALSE);

		return props;
	}

	/**
	 * <p>
	 * Retrieves the LSA-Parser Service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle context
	 * @return A reference to an LSAParser
	 */
	private LSAParser retrieveLSAParserService(final BundleContext context) {
		final ServiceReference<LSAParser> ref = context
				.getServiceReference(LSAParser.class);
		if (ref == null) {
			throw new IllegalStateException("Cannot retrieve a reference to "
					+ "PrivilegedLSAFactory service.");
		}

		return context.getService(ref);
	}

	@Override
	public final void stop(final BundleContext context) throws Exception {
		System.out.println("SemanticWebSAPERE [RDFSpace]: Stopping..");
		if (lsaSpaceServiceReg != null) {
			context.ungetService(lsaSpaceServiceReg.getReference());
			lsaSpaceServiceReg = null;
			System.out.println("SemanticWebSAPERE [RDFSpace]: "
					+ "LSA-space UNREGISTERED.");
		}
	}

}
