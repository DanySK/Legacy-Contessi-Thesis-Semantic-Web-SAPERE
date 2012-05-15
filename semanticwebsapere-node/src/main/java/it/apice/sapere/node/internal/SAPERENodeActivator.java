package it.apice.sapere.node.internal;

import it.apice.sapere.api.EcolawFactory;
import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.formulas.FormulaFactory;
import it.apice.sapere.api.space.core.EcolawCompiler;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.core.strategy.CustomStrategyPipelineStep;
import it.apice.sapere.management.DefaultReactionsScheduler;
import it.apice.sapere.management.ReactionManager;
import it.apice.sapere.management.impl.DiffusionHandler;
import it.apice.sapere.management.impl.ReactionManagerImpl;
import it.apice.sapere.management.impl.ReactionManagerLogger;
import it.apice.sapere.management.impl.SynthPropsHandler;
import it.apice.sapere.node.LoggerFactory;
import it.apice.sapere.node.agents.SAPEREAgentsFactory;
import it.apice.sapere.node.agents.impl.SAPEREAgentsFactoryImpl;
import it.apice.sapere.node.networking.bluetooth.impl.BluetoothManagerAgent;
import it.apice.sapere.node.networking.impl.NetworkManager;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * <p>
 * SemanticWebSAPERE - Node Activator.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class SAPERENodeActivator implements BundleActivator {

	/** Console Log level System property Key. */
	private static final transient String CONSOLE_LOG_LEVEL = "sapere"
			+ ".console.level";

	/** LSA Factory service. */
	private transient PrivilegedLSAFactory lsaFactory;

	/** LSA Compiler service. */
	private transient LSACompiler<?> lsaCompiler;

	/** LSA Parser service. */
	private transient LSAParser lsaParser;

	/** Eco-law Factory service. */
	private transient EcolawFactory lawFactory;

	/** Eco-law Compiler service. */
	private transient EcolawCompiler lawCompiler;

	/** Formula Factory service. */
	private transient FormulaFactory fFactory;

	/** LSA-space service. */
	private transient LSAspaceCore<?> lsaSpace;

	/** List of published registrations. */
	private final transient List<ServiceRegistration<?>> regs = 
			new LinkedList<ServiceRegistration<?>>();

	/** List of retrieved references. */
	private final transient List<ServiceReference<?>> refs = 
			new LinkedList<ServiceReference<?>>();

	/** Reference to eco-laws scheduler. */
	private transient ReactionManager rManager;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public final void start(final BundleContext context) throws Exception {
		LoggerFactoryImpl.init(context.getProperty(CONSOLE_LOG_LEVEL));

		/* === WELCOME MESSAGE === */

		log("---------------------------------------------------------------");
		log("SAPERE-node (Self-Aware Pervasive Service Ecosystems)");
		log("---------------------------------------------------------------");
		log("");

		/* === OSGi SERVICES IMPORTATION === */

		log("Looking for services:");
		initLSAFactory(context);
		log("   + LSA Factory");
		initLSACompiler(context);
		log("   + LSA Compiler");
		initLSAParser(context);
		log("   + LSA Parser");
		initELFactory(context);
		log("   + Eco-law Factory");
		initELCompiler(context);
		log("   + Eco-law Compiler");
		initFFactory(context);
		log("   + Eco-law's Formula Factory");
		initLSAspace(context);
		log("   + LSA-space");

		/* === CONNECTIVITY DAEMONS === */

		log("");
		log("Configuring connectivity:");

		// Node Services getter initialization
		NodeServicesImpl.init(lsaFactory, lsaCompiler, lsaParser, lawFactory,
				lawCompiler, fFactory, lsaSpace);

//		NotifierAgent.getInstance().start();
//		log("   + \"Remote\" observation enabled");
//
//		try {
//			GuestsHandlerAgent.getInstance().start();
//			log("   + TCP/IP communication supported");
//		} catch (Exception ex) {
//			log("   - TCP/IP communication NOT supported", ex);
//		}

		try {
			BluetoothManagerAgent.getInstance(NetworkManager.getInstance())
					.start();
			log("   + Bluetooth communication supported");
		} catch (IllegalStateException ex) {
			log("   - Bluetooth communication NOT supported", ex.getCause());
		}

		/* === (CUSTOM) COMPONENTS CREATION === */

		log("");
		log("Components/Agents creation:");

		final CustomStrategyPipelineStep syntPropsHdl = new SynthPropsHandler();
		lsaSpace.getCustomStrategyPipeline()
				.addStepAtTheBeginning(syntPropsHdl);
		log("   + Synthetic Properties handler");

		lsaSpace.addSpaceObserver(new DiffusionHandler());
		log("   + Diffusion handler");

		// Reaction manager initialization
		rManager = new ReactionManagerImpl(new DefaultReactionsScheduler(),
				lawCompiler);
		rManager.addReactionManagerObserver(
				new ReactionManagerLogger(rManager));
		rManager.spawn();

		// Reaction manager registration
		NodeServicesImpl.registerReactionManager(rManager);
		log("   + Reaction Manager (eco-law scheduler)");

		/* === OSGi SERVICES PUBLICATION === */

		log("");
		log("Publishing services:");

		registerLogFactory(context);
		log("   + Log Facility");
		registerSAPEREAgentsFactory(context);
		log("   + SAPERE Agents Factory");

		log("");
		log("---------------------------------------------------------------");
		log("READY (id = sapere:" + lsaFactory.getNodeID().split("#")[1] + ")");
		log("---------------------------------------------------------------");
		log("");
	}

	/**
	 * <p>
	 * Registers the SAPEREAgents Factory service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle context
	 */
	private void registerSAPEREAgentsFactory(final BundleContext context) {
		// SAPEREAgentsFactoryImpl
		// .defineAccessPolicyClass(DefaultAccessPolicy.class);
		regs.add(context.registerService(SAPEREAgentsFactory.class,
				SAPEREAgentsFactoryImpl.getInstance(), null));
	}

	/**
	 * <p>
	 * Registers the Log Factory service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle context
	 */
	private void registerLogFactory(final BundleContext context) {
		regs.add(context.registerService(LoggerFactory.class,
				LoggerFactoryImpl.getInstance(), null));
	}

	/**
	 * <p>
	 * Initializes the LSA Factory service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle context
	 * @throws SAPEREException
	 *             Cannot find the service
	 */
	private void initLSAFactory(final BundleContext context)
			throws SAPEREException {
		final ServiceReference<PrivilegedLSAFactory> ref = context
				.getServiceReference(PrivilegedLSAFactory.class);
		if (ref == null) {
			throw new SAPEREException("Cannot find \"LSA Factory\" service");
		}

		lsaFactory = context.getService(ref);
		refs.add(ref);
	}

	/**
	 * <p>
	 * Initializes the Formula Factory service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle context
	 * @throws SAPEREException
	 *             Cannot find the service
	 */
	private void initFFactory(final BundleContext context)
			throws SAPEREException {
		final ServiceReference<FormulaFactory> ref = context
				.getServiceReference(FormulaFactory.class);
		if (ref == null) {
			throw new SAPEREException("Cannot find "
					+ "\"Formula Factory\" service");
		}

		fFactory = context.getService(ref);
		refs.add(ref);
	}

	/**
	 * <p>
	 * Initializes the Eco-Law Compiler service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle context
	 * @throws SAPEREException
	 *             Cannot find the service
	 */
	private void initELCompiler(final BundleContext context)
			throws SAPEREException {
		final ServiceReference<EcolawCompiler> ref = context
				.getServiceReference(EcolawCompiler.class);
		if (ref == null) {
			throw new SAPEREException(
					"Cannot find \"Eco-law Compiler\" service");
		}

		lawCompiler = context.getService(ref);
		refs.add(ref);
	}

	/**
	 * <p>
	 * Initializes the Eco-law Factory service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle context
	 * @throws SAPEREException
	 *             Cannot find the service
	 */
	private void initELFactory(final BundleContext context)
			throws SAPEREException {
		final ServiceReference<EcolawFactory> ref = context
				.getServiceReference(EcolawFactory.class);
		if (ref == null) {
			throw new SAPEREException("Cannot find "
					+ "\"Eco-law Factory\" service");
		}

		lawFactory = context.getService(ref);
		refs.add(ref);
	}

	/**
	 * <p>
	 * Initializes the LSA Parser service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle context
	 * @throws SAPEREException
	 *             Cannot find the service
	 */
	private void initLSAParser(final BundleContext context)
			throws SAPEREException {
		final ServiceReference<LSAParser> ref = context
				.getServiceReference(LSAParser.class);
		if (ref == null) {
			throw new SAPEREException("Cannot find \"LSA Parser\" service");
		}

		lsaParser = context.getService(ref);
		refs.add(ref);
	}

	/**
	 * <p>
	 * Initializes the LSA Compiler service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle context
	 * @throws SAPEREException
	 *             Cannot find the service
	 */
	@SuppressWarnings("rawtypes")
	private void initLSACompiler(final BundleContext context)
			throws SAPEREException {
		final ServiceReference<LSACompiler> ref = context
				.getServiceReference(LSACompiler.class);
		if (ref == null) {
			throw new SAPEREException("Cannot find \"LSA Compiler\" service");
		}

		lsaCompiler = context.getService(ref);
		refs.add(ref);
	}

	/**
	 * <p>
	 * Initializes the LSA-space service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle context
	 * @throws SAPEREException
	 *             Cannot find the service
	 */
	@SuppressWarnings("rawtypes")
	private void initLSAspace(final BundleContext context)
			throws SAPEREException {
		final ServiceReference<LSAspaceCore> ref = context
				.getServiceReference(LSAspaceCore.class);
		if (ref == null) {
			throw new SAPEREException("Cannot find \"LSA-space\" service");
		}

		lsaSpace = context.getService(ref);
		refs.add(ref);

		try {
			lsaSpace.loadOntology(getClass()
					.getResource("sapere-model.owl").toURI());
		} catch (URISyntaxException e) {
			log("Unable to load SAPERE ontology", e);
		}
	}

	@Override
	public final void stop(final BundleContext context) throws Exception {
		log("");
		log("Shutting down:");

		log("   - Killing agents");
		SAPEREAgentsFactoryImpl.getInstance().killAll();
		rManager.kill();
		BluetoothManagerAgent.getInstance(NetworkManager.getInstance()).kill();
//		GuestsHandlerAgent.getInstance().kill();
//		NotifierAgent.getInstance().kill();
		
		
		// Release imported services
		for (ServiceReference<?> ref : refs) {
			context.ungetService(ref);
		}
		refs.clear();
		log("   - Imported services RELEASED");

		// Cancel registered services
		for (ServiceRegistration<?> reg : regs) {
			reg.unregister();
		}
		regs.clear();
		log("   - Published services UNREGISTERED");

		log("");
		log("---------------------------------------------------------------");
		log("TERMINATED");
		log("---------------------------------------------------------------");
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
		LoggerFactoryImpl.getInstance().getLogger(this).log(msg);
	}

	/**
	 * <p>
	 * Logs a message (warning).
	 * </p>
	 * 
	 * @param msg
	 *            The message to be logged
	 * @param cause
	 *            Reason of the notification
	 */
	private void log(final String msg, final Throwable cause) {
		LoggerFactoryImpl
				.getInstance()
				.getLogger(this)
				.warn(String.format("%s (reason: %s)", msg,
						cause.getMessage()), null);
	}
}
