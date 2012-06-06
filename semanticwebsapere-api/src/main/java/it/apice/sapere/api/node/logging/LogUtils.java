package it.apice.sapere.api.node.logging;

/**
 * <p>
 * This interface models logging utilities.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface LogUtils {

	/**
	 * <p>
	 * Logs an information.
	 * </p>
	 * 
	 * @param msg
	 *            The message
	 */
	void log(String msg);

	/**
	 * <p>
	 * Logs a DEBUG information.
	 * </p>
	 * 
	 * @param msg
	 *            The message
	 */
	void spy(String msg);

	/**
	 * <p>
	 * Logs a WARNING.
	 * </p>
	 * 
	 * @param msg
	 *            The message
	 * @param cause
	 *            The cause of the warning
	 */
	void warn(String msg, Throwable cause);

	/**
	 * <p>
	 * Logs an ERROR.
	 * </p>
	 * 
	 * @param msg
	 *            The message
	 * @param cause
	 *            The cause of the error
	 */
	void error(String msg, Throwable cause);
	
	/**
	 * <p>
	 * Logs a FATAL error.
	 * </p>
	 * 
	 * @param msg
	 *            The message
	 * @param cause
	 *            The cause of the fatal error
	 */
	void fatal(String msg, Throwable cause);
}
