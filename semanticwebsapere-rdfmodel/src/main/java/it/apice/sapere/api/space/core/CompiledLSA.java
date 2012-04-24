package it.apice.sapere.api.space.core;

import it.apice.sapere.api.lsas.LSAid;

import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * <p>
 * This inteface models an LSA that has been compiled in a set of RDF
 * statements.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface CompiledLSA {

	/**
	 * <p>
	 * Retrieves the compiled LSA's identifier.
	 * </p>
	 * 
	 * @return The LSA-id
	 */
	LSAid getLSAid();

	/**
	 * <p>
	 * Retrieves an iterator over all statements that compose the LSA.
	 * </p>
	 * 
	 * @return An RDF Statement iterator
	 */
	StmtIterator getStatements();
}
