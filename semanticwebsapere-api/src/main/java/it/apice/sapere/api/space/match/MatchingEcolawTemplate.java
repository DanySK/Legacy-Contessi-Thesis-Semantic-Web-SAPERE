package it.apice.sapere.api.space.match;

import it.apice.sapere.api.SAPEREException;

/**
 * <p>
 * This class represents a precompiled updated query with a set of variable that
 * should be bound in order to be applied to the LSA-space.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface MatchingEcolawTemplate {

	/**
	 * <p>
	 * Retrieves the label associated to the eco-law (if any).
	 * </p>
	 * 
	 * @return Ecolaw's label
	 */
	String getLabel();

	/**
	 * <p>
	 * Binds template variables to provided values and returns an applicable
	 * {@link MatchingEcolaw}.
	 * </p>
	 * 
	 * @param bindings
	 *            MatchResult with all bindings
	 * @return The corresponding {@link MatchingEcolaw}
	 * @throws SAPEREException
	 *             Cannot complete binding (invalid bindings, or not all
	 *             variables are bound)
	 */
	MatchingEcolaw bind(MatchResult bindings) throws SAPEREException;

	/**
	 * <p>
	 * Retrieves the name of all the variables used in the update query.
	 * </p>
	 * 
	 * @return A list of all variables names
	 */
	String[] variablesNames();

	/**
	 * <p>
	 * Retrieves the corresponding query in which all variables are unbound.
	 * </p>
	 * 
	 * @return A plain SPARUL query
	 */
	String getPlainQuery();
}
