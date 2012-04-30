package it.apice.sapere.space.internal;

import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.core.impl.LSACompilerImpl;
import it.apice.sapere.api.space.core.impl.ReasoningLevel;
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

	/** Reasoning level Property name. */
	private static final transient String REASONING_LEVEL = "reasoningLevel";

	/** LSA-space Service registration. */
	private transient ServiceRegistration<?> lsaSpaceServiceReg;

	@Override
	public final void start(final BundleContext context) throws Exception {
		System.out.println("SemanticWebSAPERE [RDFSpace]: Starting up..");

		final PrivilegedLSAFactory fact = retrieveLSAFactoryService(context);
		lsaSpaceServiceReg = context.registerService(LSAspaceCore.class
				.getName(), new LSAspaceImpl(new LSACompilerImpl(fact), fact,
				getReasoningLevel(System.getProperty(REASONING_LEVEL))),
				declareProps());

		System.out.println("SemanticWebSAPERE [RDFSpace]: "
				+ "LSA-space REGISTERED.");
	}

	/**
	 * <p>
	 * Retrieves the desired reasoning level (or the default one).
	 * </p>
	 * 
	 * @param levelString
	 *            The level as a String
	 * @return The {@link ReasoningLevel}
	 */
	private ReasoningLevel getReasoningLevel(final String levelString) {
		if (levelString != null) {
			if (levelString.equals("rdfs")) {
				System.out.println("SemanticWebSAPERE [RDFSpace]: "
						+ "RDFS reasoner enabled");
				return ReasoningLevel.RDFS_INF;
			}

			if (levelString.equals("owl-dl")) {
				System.out.println("SemanticWebSAPERE [RDFSpace]: "
						+ "OWL-DL reasoner enabled");
				return ReasoningLevel.OWL_DL;
			}
		}

		System.out.println("SemanticWebSAPERE [RDFSpace]: "
				+ "NO reasoner enabled");
		return ReasoningLevel.NONE;
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
		props.put("sapere.lsa-space.with-reasoner", Boolean.TRUE);

		return props;
	}

	/**
	 * <p>
	 * Retrieves the LSA-Parser Service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle context
	 * @return A reference to an PrivilegedLSAFactory
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
		System.out.println("SemanticWebSAPERE [RDFSpace]: Stopping..");
		if (lsaSpaceServiceReg != null) {
			context.ungetService(lsaSpaceServiceReg.getReference());
			lsaSpaceServiceReg = null;
			System.out.println("SemanticWebSAPERE [RDFSpace]: "
					+ "LSA-space UNREGISTERED.");
		}
	}

}
