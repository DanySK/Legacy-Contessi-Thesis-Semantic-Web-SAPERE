package it.apice.sapere.node.internal;

import it.apice.sapere.node.LogUtils;
import it.apice.sapere.node.LoggerFactory;
import it.apice.sapere.node.agents.SAPEREAgent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.osgi.framework.BundleActivator;

/**
 * <p>
 * This class implements the {@link LoggerFactory} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public final class LoggerFactoryImpl implements LoggerFactory {

	/** Maximum number of log file backups. */
	private static final int NUMBER_OF_LOG_BACKUPS = 2;

	/** The maximum size of the log file. */
	private static final String LOG_FILE_SIZE = "8192KB";

	/** Pattern used for each Console's log entry. */
	private static final transient String CONSOLE_PATTERN = "%-5p $%m%n";

	/** Pattern used for each File's log entry. */
	private static final transient String FILE_PATTERN = "%d"
			+ "{yyyy-MM-dd HH:mm:ss.SSS} %5p %c@%F:%L $%m%n";

	/** Pattern used for each File's log entry (faster). */
	private static final transient String EASY_FILE_PATTERN = "%d"
			+ "{yyyy-MM-dd HH:mm:ss.SSS} %5p %c@%t $%m%n";

	/** Console Log level. */
	private final transient Level level;

	/** Deep = slower. */
	private final transient boolean deep;

	/** Set of already inited loggers. */
	private final transient Set<Logger> inited = new HashSet<Logger>();

	/** Singleton instance. */
	private static transient LoggerFactory instance;

	/**
	 * <p>
	 * Builds a new {@link LoggerFactoryImpl}.
	 * </p>
	 * 
	 * @param lLevel
	 *            Console Log level
	 * @param deepDebug
	 *            True if detailed info should be displayed (extremely slower),
	 *            false otherwise
	 */
	private LoggerFactoryImpl(final Level lLevel, final boolean deepDebug) {
		if (lLevel == null) {
			throw new IllegalArgumentException("Invalid logger level");
		}

		level = lLevel;
		deep = deepDebug;

		LogManager.resetConfiguration();
	}

	/**
	 * <p>
	 * Initializes the logger.
	 * </p>
	 * 
	 * @param logger
	 *            A logger instance
	 * @param agentId
	 *            Local ID of the agent
	 * @throws IOException
	 *             Cannot access log file
	 */
	private void initLogger(final Logger logger, final String agentId)
			throws IOException {
		logger.setLevel(Level.ALL);
		logger.removeAllAppenders();

		logger.addAppender(initConsoleAppender());
		logger.addAppender(initFileAppender(agentId));
	}

	/**
	 * <p>
	 * Initializes the Appender for standard output.
	 * </p>
	 * 
	 * @return The initialized Appender
	 */
	private Appender initConsoleAppender() {
		final ConsoleAppender app = new ConsoleAppender(new PatternLayout(
				CONSOLE_PATTERN));
		app.setTarget("System.out");
		app.setThreshold(level);

		return app;
	}

	/**
	 * <p>
	 * Initializes the Appender for Log File.
	 * </p>
	 * 
	 * @param agentId
	 *            Local ID of the agent
	 * @return The initialized Appender
	 * @throws IOException
	 *             Cannot access log file
	 */
	private Appender initFileAppender(final String agentId) throws IOException {
		String pattern = EASY_FILE_PATTERN;
		if (deep) {
			pattern = FILE_PATTERN;
		}

		final RollingFileAppender app = new RollingFileAppender(
				new PatternLayout(pattern), String.format(
						"sapere-logs/sapere-%s.log", agentId));
		app.setMaxBackupIndex(NUMBER_OF_LOG_BACKUPS);
		app.setMaxFileSize(LOG_FILE_SIZE);
		app.setThreshold(Level.ALL);

		return app;
	}

	@Override
	public LogUtils getLogger(final SAPEREAgent agent) {
		return retrieveLogger(agent.getClass(), agent.getLocalAgentId());
	}

	@Override
	public LogUtils getLogger(final BundleActivator bActiv) {
		return retrieveLogger(bActiv.getClass(), bActiv.getClass()
				.getSimpleName());
	}

	@Override
	public LogUtils getLogger(final Class<?> clazz) {
		return retrieveLogger(clazz, clazz.getSimpleName());
	}

	/**
	 * <p>
	 * Creates or retrieves a Logger.
	 * </p>
	 * 
	 * @param reqClass
	 *            Requestor class
	 * @param reqName
	 *            Requestor name
	 * @return A {@link Logger} reference
	 */
	private LogUtils retrieveLogger(final Class<?> reqClass,
			final String reqName) {
		final Logger logger = Logger.getLogger(reqClass);
		if (!inited.contains(logger)) {
			try {
				initLogger(logger, reqName);
				inited.add(logger);
			} catch (IOException e) {
				throw new IllegalStateException("Cannot "
						+ "initialize log file", e);
			}
		}

		return new LogUtilsImpl(logger, reqName);
	}

	/**
	 * <p>
	 * Initializes the facility.
	 * </p>
	 * 
	 * @param consoleLevel
	 *            Console log level: ALL, TRACE, DEBUG, INFO, WARN, ERROR,
	 *            FATAL, OFF
	 * @param deepDebug
	 *            True if detailed info should be displayed (extremely slower),
	 *            false otherwise
	 */
	public static void init(final String consoleLevel, 
			final boolean deepDebug) {
		if (instance != null) {
			throw new IllegalStateException("Already initialized");
		}

		instance = new LoggerFactoryImpl(
				Level.toLevel(consoleLevel, Level.INFO), deepDebug);
	}

	/**
	 * <p>
	 * Initializes the facility (slowest version).
	 * </p>
	 * 
	 * @param consoleLevel
	 *            Console log level: ALL, TRACE, DEBUG, INFO, WARN, ERROR,
	 *            FATAL, OFF
	 */
	public static void init(final String consoleLevel) {
		init(consoleLevel, false);
	}

	/**
	 * <p>
	 * Singleton method.
	 * </p>
	 * 
	 * @return The singleton instance
	 */
	public static LoggerFactory getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Should be initialized first");
		}

		return instance;
	}

}
