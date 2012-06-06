package it.apice.api.node.logging.impl;

import org.apache.log4j.Logger;

import it.apice.sapere.api.node.logging.LogUtils;

/**
 * <p>
 * This class implements the {@link LogUtils} interface.
 * </p>
 *
 * @author Paolo Contessi
 *
 */
public class LogUtilsImpl implements LogUtils {

	/** Agent ID. */
	private final transient String agentId;

	/** Logger instance. */
	private final transient Logger log;

	/**
	 * <p>
	 * Builds a new {@link LogUtilsImpl}.
	 * </p>
	 *
	 * @param logger Logger instance
	 * @param reqName Requestor agent ID
	 */
	public LogUtilsImpl(final Logger logger, final String reqName) {
		if (logger == null) {
			throw new IllegalArgumentException("Invalid logger provided");
		}

		if (reqName == null) {
			throw new IllegalArgumentException(
					"Invalid requestor name provided");
		}

		log = logger;
		agentId = reqName;
	}

	@Override
	public final void log(final String msg) {
		log.info(formatMsg(msg));
	}

	@Override
	public final void spy(final String msg) {
		log.debug(formatMsg(msg));
	}

	@Override
	public final void warn(final String msg, final Throwable cause) {
		log.warn(formatMsg(msg), cause);
	}

	@Override
	public final void error(final String msg, final Throwable cause) {
		log.error(formatMsg(msg), cause);
	}
	
	@Override
	public final void fatal(final String msg, final Throwable cause) {
		log.fatal(formatMsg(msg), cause);
	}

	/**
	 * <p>
	 * Formats the message.
	 * </p>
	 * 
	 * @param msg
	 *            Message text
	 * @return The formatted message
	 */
	private String formatMsg(final String msg) {
		return String.format("%s> %s", agentId, msg);
	}
}
