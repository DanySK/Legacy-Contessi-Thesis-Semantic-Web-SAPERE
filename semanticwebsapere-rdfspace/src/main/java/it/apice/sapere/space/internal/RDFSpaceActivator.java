package it.apice.sapere.space.internal;

import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.space.core.EcolawCompiler;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.core.impl.EcolawCompilerImpl;
import it.apice.sapere.api.space.core.impl.LSACompilerImpl;
import it.apice.sapere.api.space.core.impl.ReasoningLevel;
import it.apice.sapere.api.space.match.functions.MatchFunctRegistry;
import it.apice.sapere.api.space.match.functions.impl.GenLSAidFunctImpl;
import it.apice.sapere.api.space.match.functions.impl.MatchFunctRegistryImpl;
import it.apice.sapere.space.impl.LSAspaceImpl;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.function.Function;

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
	private static final transient String REASONING_LEVEL = "sapere"
			+ ".space.reasoner";

	/** LSA-space Optimization System property Key. */
	private static final transient String LSA_SPACE_OPTIM = "sapere"
			+ ".space.optimization";

	/** LSA-space Service registration. */
	private transient ServiceRegistration<?> lsaSpaceServiceReg;

	/** LSA Compiler Service registration. */
	private transient ServiceRegistration<?> lsaCompilerServiceReg;

	/** Eco-law Compiler Service registration. */
	private transient ServiceRegistration<?> elCompilerServiceReg;

	/** Match Function Customization Service registration. */
	private transient ServiceRegistration<?> customFunctionServiceReg;

	/** Reference to LSA Factory service. */
	private transient ServiceReference<PrivilegedLSAFactory> lsaFactoryRef;

	@Override
	public final void start(final BundleContext context) throws Exception {
		log("Starting up..");

		final PrivilegedLSAFactory fact = retrieveLSAFactoryService(context);

		registerLSACompiler(context, fact);
		registerEcolawCompiler(context);

		registerLSAspace(context, fact);
		registerCustomFunctions(context);
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

		log("Eco-law Compiler REGISTERED.");
	}

	/**
	 * <p>
	 * Registers the Match Functions Customization service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle Context
	 * @return A direct reference to the service implementation
	 */
	private MatchFunctRegistry<Function> registerCustomFunctions(
			final BundleContext context) {
		customFunctionServiceReg = context.registerService(
				MatchFunctRegistry.class, MatchFunctRegistryImpl.getInstance(),
				null);

		MatchFunctRegistryImpl.getInstance().register(
				MatchFunctRegistryImpl.getInstance().buildFunctionURI(
						"generateLSA-id"), GenLSAidFunctImpl.class);

		if (customFunctionServiceReg != null) {
			log("Match Functions Customization REGISTERED.");
		}

		return MatchFunctRegistryImpl.getInstance();
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

		if (lsaCompilerServiceReg != null) {
			log("LSA Compiler REGISTERED.");
		}

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
	 * @param fact
	 *            The {@link PrivilegedLSAFactory}
	 */
	private void registerLSAspace(final BundleContext context,
			final PrivilegedLSAFactory fact) {
		final String optEn = System.getProperty(LSA_SPACE_OPTIM);
		if (optEn != null) {
			LSAspaceImpl.setOptimizationEnabled(Boolean.parseBoolean(optEn));
		}

		lsaSpaceServiceReg = context.registerService(
				LSAspaceCore.class.getName(),
				new LSAspaceImpl(fact, getReasoningLevel(System
						.getProperty(REASONING_LEVEL))), declareSpaceProps());

		log("Optimization enabled: " + LSAspaceImpl.getOptimizationEnabled());
		if (lsaSpaceServiceReg != null) {
			log("LSA-space REGISTERED.");
		}
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
				log("RDFS reasoner enabled");
				return ReasoningLevel.RDFS_INF;
			}

			if (levelString.equals("owl-dl")) {
				log("OWL-DL reasoner enabled");
				return ReasoningLevel.OWL_DL;
			}
		}

		log("NO reasoner enabled");
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
		lsaFactoryRef = context.getServiceReference(PrivilegedLSAFactory.class);
		if (lsaFactoryRef == null) {
			throw new IllegalStateException("Cannot retrieve a reference to "
					+ "PrivilegedLSAFactory service.");
		}

		if (lsaFactoryRef != null) {
			log("LSA Factory ACQUIRED");
		}

		return context.getService(lsaFactoryRef);
	}

	@Override
	public final void stop(final BundleContext context) throws Exception {
		log("Stopping..");
		if (lsaCompilerServiceReg != null) {
			lsaCompilerServiceReg.unregister();
			log("LSA Compiler UNREGISTERED.");
		}

		if (elCompilerServiceReg != null) {
			elCompilerServiceReg.unregister();
			log("Eco-law Compiler UNREGISTERED.");
		}

		if (customFunctionServiceReg != null) {
			customFunctionServiceReg.unregister();
			log("Match Function Customization UNREGISTERED.");
		}

		if (lsaSpaceServiceReg != null) {
			lsaSpaceServiceReg.unregister();
			log("LSA-Space UNREGISTERED.");
		}

		if (lsaFactoryRef != null) {
			context.ungetService(lsaFactoryRef);
			lsaFactoryRef = null;
			log("LSA Factory RELEASED.");
		}
	}

	/**
	 * <p>
	 * Logs a message.
	 * </p>
	 * 
	 * @param msg
	 *            The message to be logged
	 */
	private void log(final String msg) {
		System.out.println("rdf-space> " + msg);
	}
}
