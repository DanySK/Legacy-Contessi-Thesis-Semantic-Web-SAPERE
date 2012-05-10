package it.apice.sapere.api.space.core;

import it.apice.sapere.api.ecolaws.Ecolaw;
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
	 * Retrieves the label associated to the eco-law (if any).
	 * </p>
	 * 
	 * @return Ecolaw's label
	 */
	String getLabel();

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
	 * Retrieves the name of the variable used as rate, or the constant value
	 * specified for it.
	 * </p>
	 * 
	 * @return A String representing actual rate
	 */
	String getRate();

	/**
	 * <p>
	 * Checks if the rate is expressed as a variable term.
	 * </p>
	 * 
	 * @return True if variable, false otherwise
	 */
	boolean hasVariableRate();

	/**
	 * <p>
	 * Retrieves the query that corresponds to the provided eco-law.
	 * </p>
	 * 
	 * @return An update query template
	 */
	MatchingEcolawTemplate getUpdateQueryTemplate();

	/**
	 * <p>
	 * Retrieves the eco-law that originated this compiled one.
	 * </p>
	 * 
	 * @return The source of the compilation, or <code>null</code> if created
	 *         from scratch (not compiled)
	 */
	Ecolaw getCompilationSource();
}
