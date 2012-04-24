package it.apice.sapere.api.ecolaws.match;

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
	 */
	void apply();

	/**
	 * <p>
	 * Retrieves the query that corresponds to the provided eco-law effect.
	 * </p>
	 * 
	 * @return An update query
	 */
	String getUpdateQuery();
}
