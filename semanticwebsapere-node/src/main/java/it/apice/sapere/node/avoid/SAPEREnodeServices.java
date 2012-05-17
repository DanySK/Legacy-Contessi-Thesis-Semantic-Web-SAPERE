package it.apice.sapere.node.avoid;

import it.apice.sapere.api.avoid.RDFModelServices;
import it.apice.sapere.management.DefaultReactionsScheduler;
import it.apice.sapere.management.ReactionManager;
import it.apice.sapere.management.impl.ReactionManagerImpl;
import it.apice.sapere.management.impl.ReactionManagerLogger;
import it.apice.sapere.node.LoggerFactory;
import it.apice.sapere.node.agents.NodeServices;
import it.apice.sapere.node.agents.SAPEREAgentsFactory;
import it.apice.sapere.node.agents.impl.SAPEREAgentsFactoryImpl;
import it.apice.sapere.node.internal.LoggerFactoryImpl;
import it.apice.sapere.node.internal.NodeServicesImpl;
import it.apice.sapere.node.networking.bluetooth.impl.BluetoothManagerAgent;
import it.apice.sapere.node.networking.impl.NetworkManager;
import it.apice.sapere.space.avoid.RDFSpaceServices;

/**
 * <p>
 * Utility class that publishes bundles services when used outside an OSGi
 * context.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class SAPEREnodeServices extends RDFSpaceServices {

	/** TEMPORARY Console log level. */
	private static final String DEFAULT_CONSOLE_LOG_LEVEL = "info";

	/** Reaction manager. */
	private final transient ReactionManager rManager;

	/** Logger factory. */
	private final transient LoggerFactory logFactory;

	/** SAPERE-agent factory. */
	private final transient SAPEREAgentsFactory agFactory;

	/** Network manager. */
	private final transient NetworkManager nManager;

	/** Bluetooth manager agent. */
	private final transient BluetoothManagerAgent bManager;

	/** Reaction manager. */
	private final transient NodeServices services;

	/** Singleton instance. */
	private static transient SAPEREnodeServices instance;

	/**
	 * <p>
	 * Builds a new {@link SAPEREnodeServices}.
	 * </p>
	 */
	protected SAPEREnodeServices() {
		this(DEFAULT_CONSOLE_LOG_LEVEL);
	}

	/**
	 * <p>
	 * Builds a new {@link SAPEREnodeServices}.
	 * </p>
	 * 
	 * @param consoleLogLevel
	 *            Console log level
	 */
	protected SAPEREnodeServices(final String consoleLogLevel) {
		LoggerFactoryImpl.init(consoleLogLevel);

		NodeServicesImpl.init(getLSAFactory(), getLSACompiler(),
				getLSAParser(), getEcolawFactory(), getEcolawCompiler(),
				getFormulaFactory(), getSpace());
		services = NodeServicesImpl.getInstance();

		nManager = NetworkManager.getInstance();
		bManager = BluetoothManagerAgent.getInstance(nManager);
		bManager.start();

		final ReactionManagerImpl rMng = new ReactionManagerImpl(
				new DefaultReactionsScheduler(), getEcolawCompiler());
		rManager = rMng;
		rManager.addReactionManagerObserver(new ReactionManagerLogger(
				rManager));
		getSpace().addSpaceObserver(rMng);
		rManager.spawn();
		NodeServicesImpl.registerReactionManager(rManager);

		// SAPEREAgentsFactoryImpl
		// .defineAccessPolicyClass(DefaultAccessPolicy.class);
		agFactory = SAPEREAgentsFactoryImpl.getInstance();
		logFactory = LoggerFactoryImpl.getInstance();
	}

	/**
	 * <p>
	 * Retrieves the {@link SAPEREAgentsFactory} reference.
	 * </p>
	 * 
	 * @return The {@link SAPEREAgentsFactory} reference
	 */
	public final SAPEREAgentsFactory getAgentsFactory() {
		return agFactory;
	}

	/**
	 * <p>
	 * Retrieves the {@link LoggerFactory} reference.
	 * </p>
	 * 
	 * @return The {@link LoggerFactory} reference
	 */
	public final LoggerFactory getLogFactory() {
		return logFactory;
	}

	/**
	 * <p>
	 * Retrieves the {@link NodeServices} reference.
	 * </p>
	 * 
	 * @return The {@link NodeServices} reference
	 */
	public final NodeServices getNodeServices() {
		return services;
	}

	/**
	 * <p>
	 * Retrieves the {@link NetworkManager} reference.
	 * </p>
	 * 
	 * @return The {@link NetworkManager} reference
	 */
	public final NetworkManager getNetworkManager() {
		return nManager;
	}

	/**
	 * <p>
	 * Retrieves the {@link BluetoothManagerAgent} reference.
	 * </p>
	 * 
	 * @return The {@link BluetoothManagerAgent} reference
	 */
	public final BluetoothManagerAgent getBluetoothManagerAgent() {
		return bManager;
	}

	/**
	 * <p>
	 * Retrieves the {@link ReactionManager} reference.
	 * </p>
	 * 
	 * @return The {@link ReactionManager} reference
	 */
	public final ReactionManager getReactionManager() {
		return rManager;
	}

	/**
	 * <p>
	 * Initializes the {@link SAPEREnodeServices}.
	 * </p>
	 */
	public static void init() {
		RDFModelServices.init();
		instance = new SAPEREnodeServices();
	}

	/**
	 * <p>
	 * Initializes {@link SAPEREnodeServices}.
	 * </p>
	 * 
	 * @param consoleLogLevel
	 *            Console log level
	 */
	public static void init(final String consoleLogLevel) {
		RDFModelServices.init();
		instance = new SAPEREnodeServices(consoleLogLevel);
	}

	/**
	 * <p>
	 * Initializes {@link SAPEREnodeServices}.
	 * </p>
	 * 
	 * @param nodeUri
	 *            Node URI
	 * @param consoleLogLevel
	 *            Console log level
	 */
	public static void init(final String nodeUri, 
			final String consoleLogLevel) {
		RDFModelServices.init(nodeUri);
		instance = new SAPEREnodeServices(consoleLogLevel);
	}

	/**
	 * <p>
	 * Singleton method.
	 * </p>
	 * 
	 * @return The singleton instance
	 */
	public static SAPEREnodeServices getInstance() {
		if (instance == null) {
			init();
		}

		return instance;
	}
}
