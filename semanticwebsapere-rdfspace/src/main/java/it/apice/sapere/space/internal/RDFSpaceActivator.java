package it.apice.sapere.space.internal;

import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.space.core.EcolawCompiler;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.core.impl.EcolawCompilerImpl;
import it.apice.sapere.api.space.core.impl.LSACompilerImpl;
import it.apice.sapere.api.space.core.impl.ReasoningLevel;
import it.apice.sapere.space.impl.LSAspaceImpl;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import com.hp.hpl.jena.rdf.model.StmtIterator;

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

	/** LSA Compiler Service registration. */
	private transient ServiceRegistration<?> lsaCompilerServiceReg;

	/** Eco-law Compiler Service registration. */
	private transient ServiceRegistration<?> elCompilerServiceReg;

	@Override
	public final void start(final BundleContext context) throws Exception {
		System.out.println("SemanticWebSAPERE [RDFSpace]: Starting up..");

		final PrivilegedLSAFactory fact = retrieveLSAFactoryService(context);

		final LSACompiler<StmtIterator> comp = registerLSACompiler(context,
				fact);
		registerEcolawCompiler(context);
		registerLSAspace(context, comp, fact);
	}

	/**
	 * <p>
	 * Registers the Eco-law Compiler service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle Context
	 */
	private void registerEcolawCompiler(final BundleContext context) {
		elCompilerServiceReg = context.registerService(EcolawCompiler.class,
				new EcolawCompilerImpl(), declareCompilersProps());

		System.out.println("SemanticWebSAPERE [RDFSpace]: "
				+ "Eco-law Compiler REGISTERED.");
	}

	/**
	 * <p>
	 * Registers the LSA Compiler service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle Context
	 * @param fact
	 *            The {@link PrivilegedLSAFactory} to be used
	 * @return A direct reference to the service implementation
	 */
	private LSACompiler<StmtIterator> registerLSACompiler(
			final BundleContext context, final PrivilegedLSAFactory fact) {
		final LSACompiler<StmtIterator> comp = new LSACompilerImpl(fact);
		lsaCompilerServiceReg = context.registerService(LSACompiler.class,
				comp, declareCompilersProps());

		System.out.println("SemanticWebSAPERE [RDFSpace]: "
				+ "LSA Compiler REGISTERED.");

		return comp;
	}
	
	/**
	 * <p>
	 * Defines properties of Compiler services that will be registered.
	 * </p>
	 * 
	 * @return Service properties
	 */
	private Hashtable<String, ?> declareCompilersProps() {
		final Hashtable<String, Object> props = new Hashtable<String, Object>();

		props.put("sapere.rdf-based", Boolean.TRUE);
		props.put("sapere.acid-transactions", Boolean.FALSE);
		props.put("sapere.lsa-space.with-reasoner", Boolean.TRUE);

		return props;
	}

	/**
	 * <p>
	 * Registers the LSA-space service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle Context
	 * @param comp
	 *            The {@link LSACompiler}
	 * @param fact
	 *            The {@link PrivilegedLSAFactory}
	 */
	private void registerLSAspace(final BundleContext context,
			final LSACompiler<StmtIterator> comp,
			final PrivilegedLSAFactory fact) {
		lsaSpaceServiceReg = context.registerService(LSAspaceCore.class
				.getName(), new LSAspaceImpl(comp, fact,
				getReasoningLevel(System.getProperty(REASONING_LEVEL))),
				declareSpaceProps());

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
	 * Defines properties of LSA-space service that will be registered.
	 * </p>
	 * 
	 * @return Service properties
	 */
	private Hashtable<String, ?> declareSpaceProps() {
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
		
		if (lsaCompilerServiceReg != null) {
			context.ungetService(lsaCompilerServiceReg.getReference());
			lsaCompilerServiceReg = null;
			System.out.println("SemanticWebSAPERE [RDFSpace]: "
					+ "LSA Compiler UNREGISTERED.");
		}
		
		if (elCompilerServiceReg != null) {
			context.ungetService(elCompilerServiceReg.getReference());
			elCompilerServiceReg = null;
			System.out.println("SemanticWebSAPERE [RDFSpace]: "
					+ "Eco-law Compiler UNREGISTERED.");
		}
	}

}
