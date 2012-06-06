package it.apice.sapere.api.management;

/**
 * <p>
 * This exception is raised when the scheduled eco-law should be revised.
 * </p>
 *
 * @author Paolo Contessi
 *
 */
public class AbortException extends Exception {

	/** Serialization ID. */
	private static final long serialVersionUID = -4411330696987368464L;

	/**
	 * <p>
	 * Builds a new {@link AbortException}.
	 * </p>
	 */
	public AbortException() {
		super();
	}
}
