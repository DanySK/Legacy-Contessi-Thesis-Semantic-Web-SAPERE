package it.apice.sapere.api.space.core;

import it.apice.sapere.api.lsas.LSA;

/**
 * <p>
 * This inteface models an entity that compiles LSAs into a set of RDF
 * Statements.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface LSACompiler {

	/**
	 * <p>
	 * Compiles an LSA in RDF.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA to be compiled
	 * @return The compiled LSA
	 */
	CompiledLSA compile(LSA lsa);
}
