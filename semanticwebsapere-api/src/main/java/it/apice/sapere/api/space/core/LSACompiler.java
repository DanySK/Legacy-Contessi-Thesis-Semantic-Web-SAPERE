package it.apice.sapere.api.space.core;

import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.lsas.LSA;

/**
 * <p>
 * This interface models an entity that compiles LSAs into a set of RDF
 * Statements.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <RDFStmtIterType>
 *            The class that represents an iterator over RDF Statements
 */
public interface LSACompiler<RDFStmtIterType> {

	/**
	 * <p>
	 * Compiles an LSA in RDF.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA to be compiled
	 * @return The compiled LSA
	 */
	CompiledLSA<RDFStmtIterType> compile(LSA lsa);

	/**
	 * <p>
	 * Parses provided string in order to extract a {@link CompiledLSA}.
	 * </p>
	 * 
	 * @param rdf
	 *            A String, in one of the supported RDF formats, which contains
	 *            information about the LSA
	 * @param format
	 *            The format in which the String is written
	 * @return The corresponding compiled LSA
	 */
	CompiledLSA<RDFStmtIterType> parse(String rdf, RDFFormat format);

	/**
	 * <p>
	 * Parses provided string in order to extract a {@link CompiledLSA}.
	 * </p>
	 * 
	 * @param rdf
	 *            A String, in one of the supported RDF formats, which contains
	 *            information about the LSA
	 * @param format
	 *            The format in which the String is written
	 * @param makeCopy
	 *            If true generates a fresh LSA-id for the returned
	 *            {@link CompiledLSA}, so to obtain a copy
	 * @return The corresponding compiled LSA
	 */
	CompiledLSA<RDFStmtIterType> parse(String rdf, RDFFormat format,
			boolean makeCopy);

	/**
	 * <p>
	 * Creates an empty {@link CompiledLSA}.
	 * </p>
	 * 
	 * @return An empty {@link CompiledLSA}
	 */
	CompiledLSA<RDFStmtIterType> create();

}
