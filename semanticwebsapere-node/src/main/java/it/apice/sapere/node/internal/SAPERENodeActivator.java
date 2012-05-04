package it.apice.sapere.node.internal;

import it.apice.sapere.api.EcolawFactory;
import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.formulas.FormulaFactory;
import it.apice.sapere.api.space.core.EcolawCompiler;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.node.agents.SAPEREAgentsFactory;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.LogFactory;
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

	/** LSA Factory service. */
	private transient PrivilegedLSAFactory sysLsaFactory;

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
	private transient LSAspaceCore<?> sysSpace;

	/** List of published registrations. */
	private final transient List<ServiceRegistration<?>> regs = 
			new LinkedList<ServiceRegistration<?>>();

	/** List of retrieved references. */
	private final transient List<ServiceReference<?>> refs = 
			new LinkedList<ServiceReference<?>>();

	@Override
	public final void start(final BundleContext context) throws Exception {
		log("Initializing...");

		log("Looking for services:");
		initLSAFactory(context);
		log(" - LSA Factory");
		initLSACompiler(context);
		assert lsaCompiler != null;
		log(" - LSA Compiler");
		initLSAParser(context);
		log(" - LSA Parser");
		initELFactory(context);
		log(" - Eco-law Factory");
		initELCompiler(context);
		assert lawCompiler != null;
		log(" - Eco-law Compiler");
		initFFactory(context);
		log(" - Eco-law's Formula Factory");
		initLSAspace(context);
		log(" - LSA-space");

		// TODO Implement it! (REGISTRATION)
		log("Registering SAPEREAgents Factory..");
		registerSAPEREAgentsFactory(context);

		log("Ready.");
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
		regs.add(context.registerService(SAPEREAgentsFactory.class,
				new SAPEREAgentsFactoryImpl(sysLsaFactory, lsaParser,
						lawFactory, fFactory, sysSpace), null));
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

		sysLsaFactory = context.getService(ref);
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

		sysSpace = context.getService(ref);
		refs.add(ref);
	}

	@Override
	public final void stop(final BundleContext context) throws Exception {
		log("Shutting down...");

		// Release imported services
		for (ServiceReference<?> ref : refs) {
			context.ungetService(ref);
		}
		refs.clear();
		log("Services RELEASED");

		// Cancel registered services
		for (ServiceRegistration<?> reg : regs) {
			reg.unregister();
		}
		regs.clear();
		log("Published services UNREGISTERED");

		log("Bye bye.");
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
		LogFactory.getLog(SAPERENodeActivator.class).info("node> " + msg);
	}
}
