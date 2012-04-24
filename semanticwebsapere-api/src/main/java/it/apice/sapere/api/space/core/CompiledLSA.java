package it.apice.sapere.api.space.core;

import it.apice.sapere.api.lsas.LSAid;

/**
 * <p>
 * This inteface models an LSA that has been compiled in a set of RDF
 * statements.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <RDFStmtIterType>
 *            The class that represents an iterator over RDF Statements
 */
public interface CompiledLSA<RDFStmtIterType> {

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
	RDFStmtIterType getStatements();
}
