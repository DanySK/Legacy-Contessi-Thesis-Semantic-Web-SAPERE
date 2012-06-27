package it.apice.sapere.api.space.core;

import it.apice.sapere.api.ecolaws.Ecolaw;

/**
 * <p>
 * This interface models an entity that compiles an eco-law into a query for
 * matching process and a template query for LSA-space status update.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface EcolawCompiler {

	/**
	 * <p>
	 * Compiles an LSA in RDF.
	 * </p>
	 * 
	 * @param law
	 *            The eco-law to be compiled
	 * @return The compiled eco-law
	 */
	CompiledEcolaw compile(Ecolaw law);

	/**
	 * <p>
	 * Creates a new {@link CompiledEcolaw}.
	 * </p>
	 * 
	 * @param mQuery
	 *            The query for matching phase
	 * @param uQuery
	 *            The query template for update phase
	 * @param rate
	 *            The rate variable name ("?&ltname;&gt;"), or the rate value
	 * @return The compiled eco-law
	 */
	CompiledEcolaw create(String mQuery, String uQuery, String rate);

	/**
	 * <p>
	 * Creates a new {@link CompiledEcolaw}.
	 * </p>
	 * 
	 * @param mQuery
	 *            The query for matching phase
	 * @param uQuery
	 *            The query template for update phase
	 * @param rate
	 *            The rate variable name ("?&ltname;&gt;"), or the rate value
	 * @param label
	 *            Label of the eco-law
	 * @return The compiled eco-law
	 */
	CompiledEcolaw create(String mQuery, String uQuery, String rate,
			String label);
}
