package it.apice.sapere.api.ecolaws.match;

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
}
