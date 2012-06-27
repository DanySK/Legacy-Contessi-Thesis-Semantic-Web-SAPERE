package it.apice.api.node.logging.impl;

import it.apice.sapere.api.node.agents.SAPEREAgent;
import it.apice.sapere.api.node.logging.LogUtils;
import it.apice.sapere.api.node.logging.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

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

	/** Console Log dispLevel. */
	private final transient Level dispLevel;

	/** File Log dispLevel. */
	private final transient Level fileLevel;

	/** Deep = slower. */
	private final transient boolean deep;
	
	/** A unique identifier, for the platform. */
	private final transient long timeId;

	/** Set of already inited loggers. */
	private final transient Set<Logger> inited = new HashSet<Logger>();

	/** Singleton instance. */
	private static transient LoggerFactory instance;

	/**
	 * <p>
	 * Builds a new {@link LoggerFactoryImpl}.
	 * </p>
	 * 
	 * @param dLevel
	 *            Console Log dispLevel
	 * @param fLevel
	 *            File Log dispLevel
	 * @param deepDebug
	 *            True if detailed info should be displayed (extremely slower),
	 *            false otherwise
	 */
	private LoggerFactoryImpl(final Level dLevel, final Level fLevel,
			final boolean deepDebug) {
		if (dLevel == null) {
			throw new IllegalArgumentException("Invalid logger dispLevel");
		}

		if (fLevel == null) {
			throw new IllegalArgumentException("Invalid logger fileLevel");
		}

		dispLevel = dLevel;
		fileLevel = fLevel;
		deep = deepDebug;
		timeId = System.currentTimeMillis();

		// LogManager.resetConfiguration();
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

		if (!dispLevel.equals(Level.OFF)) {
			logger.addAppender(initConsoleAppender());
		}

		if (!fileLevel.equals(Level.OFF)) {
			logger.addAppender(initFileAppender(agentId));
		}
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
		app.setThreshold(dispLevel);

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
						"sapere-logs/sapere%d-%s.log",
						timeId, agentId));
		app.setMaxBackupIndex(NUMBER_OF_LOG_BACKUPS);
		app.setMaxFileSize(LOG_FILE_SIZE);
		app.setThreshold(fileLevel);

		return app;
	}

	@Override
	public LogUtils getLogger(final SAPEREAgent agent) {
		return retrieveLogger(agent.getClass(), agent.getLocalAgentId());
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
		Logger logger = Logger.getLogger(reqClass);
		if (!reqClass.getSimpleName().equals(reqName)) {
			logger = Logger.getLogger(String.format("%s::%s",
					reqClass.getCanonicalName(), reqName));
		}

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
	 *            Console log dispLevel: ALL, TRACE, DEBUG, INFO, WARN, ERROR,
	 *            FATAL, OFF
	 * @param fileLevel
	 *            Console log dispLevel: ALL, TRACE, DEBUG, INFO, WARN, ERROR,
	 *            FATAL, OFF
	 */
	public static void init(final String consoleLevel, final String fileLevel) {
		if (instance != null) {
			throw new IllegalStateException("Already initialized");
		}

		instance = new LoggerFactoryImpl(
				Level.toLevel(consoleLevel, Level.INFO), Level.toLevel(
						fileLevel, Level.ALL), false);
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
