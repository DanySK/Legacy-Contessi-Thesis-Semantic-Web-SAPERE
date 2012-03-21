package it.apice.sapere.api;

/**
 * <p>
 * This class represents an exception occurred in the SAPERE domain.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class SAPEREException extends Exception {

	/** Serialization ID. */
	private static final long serialVersionUID = -6805167499473162995L;

	/**
	 * <p>
	 * Builds a new SAPEREException.
	 * </p>
	 */
	public SAPEREException() {
		super();
	}

	/**
	 * <p>
	 * Builds a new SAPEREException.
	 * </p>
	 * 
	 * @param msg
	 *            Description of exception
	 */

	public SAPEREException(final String msg) {
		super(msg);
	}

	/**
	 * <p>
	 * Builds a new SAPEREException.
	 * </p>
	 * 
	 * @param msg
	 *            Description of exception
	 * @param cause
	 *            The cause of this exception
	 */
	public SAPEREException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
