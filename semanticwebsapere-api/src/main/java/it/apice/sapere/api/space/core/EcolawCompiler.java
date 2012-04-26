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
	 * @return The compiled eco-law
	 */
	CompiledEcolaw create(String mQuery, String uQuery);
}
