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
}
