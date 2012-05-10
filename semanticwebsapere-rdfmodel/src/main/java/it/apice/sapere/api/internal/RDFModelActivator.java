package it.apice.sapere.api.internal;

import it.apice.sapere.api.EcolawFactory;
import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.ecolaws.formulas.FormulaFactory;
import it.apice.sapere.api.ecolaws.formulas.impl.FormulaFactoryImpl;
import it.apice.sapere.api.impl.EcolawFactoryImpl;
import it.apice.sapere.api.impl.LSAFactoryImpl;
import it.apice.sapere.api.impl.LSAParserImpl;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * <p>
 * SemanticWebSAPERE - RDF Model Activator.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class RDFModelActivator implements BundleActivator {

	/** SAPERE node identifier. */
	private transient String nodeURI;

	/** LSA-Factory Service registration. */
	private ServiceRegistration<?> lsaFactoryServiceReg;

	/** LSA-Parser Service registration. */
	private ServiceRegistration<?> lsaParserServiceReg;

	/** Ecolaw-Factory Service registration. */
	private ServiceRegistration<?> ecolawFactoryServiceReg;

	/** Formula-Factory Service registration. */
	private ServiceRegistration<?> formulaFactoryServiceReg;

	/** Node-Uri property key. */
	private static final transient String NODE_URI_PROPERTY = "node-uri";

	@Override
	public final void start(final BundleContext context) throws Exception {
		System.out.println("SemanticWebSAPERE [RDFModel]: Starting up..");
		final PrivilegedLSAFactory lsaFactory = initLSAFactoryService(context);
		initLSAParserService(context, lsaFactory);
		initEcolawFactoryService(context);
		initFormulaFactoryService(context);

		log("Node URI = " + nodeURI);
	}

	/**
	 * <p>
	 * Registers the LSA-Parser Service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle context
	 * @param lsaFactory
	 *            The PrivilegedLSAFactory
	 */
	private void initLSAParserService(final BundleContext context,
			final PrivilegedLSAFactory lsaFactory) {
		lsaParserServiceReg = context.registerService(
				LSAParser.class.getName(), new LSAParserImpl(lsaFactory),
				declareProps());
		log("LSAParser REGISTERED");
	}

	/**
	 * <p>
	 * Registers the LSA-Factory Service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle context
	 * @return A reference to the LSAFactory implementation
	 */
	private PrivilegedLSAFactory initLSAFactoryService(
			final BundleContext context) {
		nodeURI = context.getProperty(NODE_URI_PROPERTY);

		PrivilegedLSAFactory lsaFactory;
		if (nodeURI != null) {
			lsaFactory = new LSAFactoryImpl(nodeURI);
		} else {
			lsaFactory = new LSAFactoryImpl();
			nodeURI = lsaFactory.getNodeID();
		}

		lsaFactoryServiceReg = context.registerService(
				PrivilegedLSAFactory.class.getName(), lsaFactory,
				declareProps());
		log("LSAFactory REGISTERED");

		return lsaFactory;
	}

	/**
	 * <p>
	 * Registers the Ecolaw-Factory Service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle context
	 */
	private void initEcolawFactoryService(final BundleContext context) {
		ecolawFactoryServiceReg = context.registerService(
				EcolawFactory.class.getName(), new EcolawFactoryImpl(),
				declareProps());
		log("EcolawFactory REGISTERED");

	}

	/**
	 * <p>
	 * Registers the Formula-Factory Service.
	 * </p>
	 * 
	 * @param context
	 *            Bundle context
	 */
	private void initFormulaFactoryService(final BundleContext context) {
		formulaFactoryServiceReg = context.registerService(
				FormulaFactory.class.getName(), new FormulaFactoryImpl(),
				declareProps());
		log("FormulaFactory REGISTERED");

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

		return props;
	}

	@Override
	public final void stop(final BundleContext context) throws Exception {
		System.out.println("SemanticWebSAPERE [RDFModel]: Stopping..");
		if (lsaParserServiceReg != null) {
			lsaParserServiceReg.unregister();
			log("LSAParser UNREGISTERED.");
		}

		if (lsaFactoryServiceReg != null) {
			lsaFactoryServiceReg.unregister();
			log("LSAFactory UNREGISTERED.");
		}

		if (ecolawFactoryServiceReg != null) {
			context.ungetService(ecolawFactoryServiceReg.getReference());
			log("EcolawFactory UNREGISTERED.");
		}

		if (formulaFactoryServiceReg != null) {
			formulaFactoryServiceReg.unregister();
			log("FormulaFactory UNREGISTERED.");
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
		System.out.println("rdf-model> " + msg);
	}
}
