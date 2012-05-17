package it.apice.sapere.api.avoid;

import it.apice.sapere.api.EcolawFactory;
import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.ecolaws.formulas.FormulaFactory;
import it.apice.sapere.api.ecolaws.formulas.impl.FormulaFactoryImpl;
import it.apice.sapere.api.impl.EcolawFactoryImpl;
import it.apice.sapere.api.impl.LSAFactoryImpl;
import it.apice.sapere.api.impl.LSAParserImpl;

/**
 * <p>
 * Utility class that publishes bundles services when used outside an OSGi
 * context.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class RDFModelServices {

	/** LSA Factory. */
	private final transient PrivilegedLSAFactory factory;

	/** LSA Parser. */
	private final transient LSAParser parser;

	/** Ecolaw Factory. */
	private final transient EcolawFactory lFactory;

	/** Formula Factory. */
	private final transient FormulaFactory fFactory;

	/** Singleton instance. */
	private static transient RDFModelServices instance;

	/**
	 * <p>
	 * Builds a new {@link RDFModelServices}.
	 * </p>
	 */
	protected RDFModelServices() {
		factory = new LSAFactoryImpl();
		parser = new LSAParserImpl(factory);
		lFactory = new EcolawFactoryImpl();
		fFactory = new FormulaFactoryImpl();
	}

	/**
	 * <p>
	 * Builds a new {@link RDFModelServices}.
	 * </p>
	 * 
	 * @param nodeUri
	 *            Node URI
	 */
	protected RDFModelServices(final String nodeUri) {
		factory = new LSAFactoryImpl(nodeUri);
		parser = new LSAParserImpl(factory);
		lFactory = new EcolawFactoryImpl();
		fFactory = new FormulaFactoryImpl();
	}

	/**
	 * <p>
	 * Retrieves the {@link LSAFactory} reference.
	 * </p>
	 * 
	 * @return The {@link LSAFactory} reference
	 */
	public final PrivilegedLSAFactory getLSAFactory() {
		return factory;
	}

	/**
	 * <p>
	 * Retrieves the {@link LSAParser} reference.
	 * </p>
	 * 
	 * @return The {@link LSAParser} reference
	 */
	public final LSAParser getLSAParser() {
		return parser;
	}

	/**
	 * <p>
	 * Retrieves the {@link EcolawFactory} reference.
	 * </p>
	 * 
	 * @return The {@link EcolawFactory} reference
	 */
	public final EcolawFactory getEcolawFactory() {
		return lFactory;
	}

	/**
	 * <p>
	 * Retrieves the {@link FormulaFactory} reference.
	 * </p>
	 * 
	 * @return The {@link FormulaFactory} reference
	 */
	public final FormulaFactory getFormulaFactory() {
		return fFactory;
	}

	/**
	 * <p>
	 * Initializes the {@link RDFModelServices}.
	 * </p>
	 */
	public static void init() {
		instance = new RDFModelServices();
	}

	/**
	 * <p>
	 * Initializes {@link RDFModelServices}.
	 * </p>
	 * 
	 * @param nodeUri
	 *            Node URI
	 */
	public static void init(final String nodeUri) {
		instance = new RDFModelServices(nodeUri);
	}

	/**
	 * <p>
	 * Singleton method.
	 * </p>
	 * 
	 * @return The singleton instance
	 */
	public static RDFModelServices getInstance() {
		if (instance == null) {
			init();
		}

		return instance;
	}
}
