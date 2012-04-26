package it.apice.sapere.api.space.match;

import it.apice.sapere.api.SAPEREException;

/**
 * <p>
 * This interface models a MatchResult that can be updated, adding binding
 * information.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface MutableMatchResult extends MatchResult {

	/**
	 * <p>
	 * Registers a new binding.
	 * </p>
	 * 
	 * @param varName
	 *            The name of the variable
	 * @param value
	 *            The value that matches
	 * @param score
	 *            The score of the registered assignment
	 * @throws SAPEREException
	 *             Clash
	 */
	void register(String varName, String value, double score)
			throws SAPEREException;

	/**
	 * <p>
	 * Clears all recorded bindings.
	 * </p>
	 */
	void reset();
}
