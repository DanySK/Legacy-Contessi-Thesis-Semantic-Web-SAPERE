package it.apice.sapere.api.space.core;

import it.apice.sapere.api.space.match.MatchingEcolawTemplate;

/**
 * <p>
 * This interface models an eco-law converted in a runnable SPARQL query.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface CompiledEcolaw {

	/**
	 * <p>
	 * Retrieves the query that corresponds to the provided eco-law.
	 * </p>
	 * 
	 * @return A query
	 */
	String getMatchQuery();

	/**
	 * <p>
	 * Retrieves the name of all the variables used in the query.
	 * </p>
	 * 
	 * @return A list of all variables names
	 */
	String[] variablesNames();
	
	/**
	 * <p>
	 * Retrieves the query that corresponds to the provided eco-law.
	 * </p>
	 * 
	 * @return An update query template
	 */
	MatchingEcolawTemplate getUpdateQueryTemplate();
}
