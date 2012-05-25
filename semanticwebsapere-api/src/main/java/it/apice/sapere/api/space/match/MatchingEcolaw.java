package it.apice.sapere.api.space.match;

import it.apice.sapere.api.SAPEREException;

/**
 * <p>
 * This interface models an Ecolaw whose variables has been bound to some values
 * after a matching phase.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface MatchingEcolaw {

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
	 * Applies this eco-law's effects to the LSA-space.
	 * </p>
	 * 
	 * @throws SAPEREException
	 *             Cannot apply the eco-law to the LSA-space
	 */
	void apply() throws SAPEREException;

	/**
	 * <p>
	 * Retrieves the query that corresponds to the provided eco-law effect.
	 * </p>
	 * 
	 * @return An update query
	 */
	String getUpdateQuery();

	/**
	 * <p>
	 * Retrieves the match outcome that has been applied in order to obtain this
	 * law.
	 * </p>
	 * 
	 * @return The applied {@link MatchResult}
	 */
	MatchResult getAppliedMatch();
}
