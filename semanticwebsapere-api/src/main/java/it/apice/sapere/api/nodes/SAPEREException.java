package it.apice.sapere.api.nodes;

/**
 * <p>
 * Generic error class that will be adopted in the model to remark that
 * something doesn't fit model requirements.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class SAPEREException extends Exception {

	/** Serialization ID. */
	private static final long serialVersionUID = -6516948830118838759L;

	/**
	 * <p>
	 * Builds a new SAPEREException.
	 * </p>
	 */
	public SAPEREException() {
		this("", null);
	}

	/**
	 * <p>
	 * Builds a new SAPEREException.
	 * </p>
	 * 
	 * @param msg
	 *            Description of the raised exception
	 */
	public SAPEREException(final String msg) {
		this(msg, null);
	}

	/**
	 * <p>
	 * Builds a new SAPEREException.
	 * </p>
	 * 
	 * @param msg
	 *            Description of the raised exception
	 * @param cause
	 *            What caused this exception
	 */
	public SAPEREException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
