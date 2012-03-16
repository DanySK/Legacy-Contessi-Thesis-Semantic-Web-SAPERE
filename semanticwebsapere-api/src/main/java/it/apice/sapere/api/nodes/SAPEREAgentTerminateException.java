package it.apice.sapere.api.nodes;

/**
 * <p>
 * This class represents an Exception that causes an unfair termination of the
 * Agent inside which it has been raised.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class SAPEREAgentTerminateException extends Exception {

	/** Serialization ID. */
	private static final long serialVersionUID = -8317425841445248965L;

	/**
	 * <p>
	 * Builds a new SAPEREAgentTerminateException.
	 * </p>
	 */
	public SAPEREAgentTerminateException() {
		this("", null);
	}

	/**
	 * <p>
	 * Builds a new SAPEREAgentTerminateException.
	 * </p>
	 * 
	 * @param msg
	 *            Description of the reason of the termination
	 */
	public SAPEREAgentTerminateException(final String msg) {
		this(msg, null);
	}

	/**
	 * <p>
	 * Builds a new SAPEREAgentTerminateException.
	 * </p>
	 * 
	 * @param msg
	 *            Description of the reason of the termination
	 * @param cause
	 *            What caused this exception
	 */
	public SAPEREAgentTerminateException(final String msg, 
			final Throwable cause) {
		super(msg, cause);
	}
}
