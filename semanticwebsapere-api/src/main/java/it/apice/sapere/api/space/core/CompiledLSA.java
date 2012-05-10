package it.apice.sapere.api.space.core;

import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.lsas.LSAid;

import java.net.URI;

/**
 * <p>
 * This interface models an LSA that has been compiled in a set of RDF
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

	/**
	 * <p>
	 * Converts this CompiledLSA to a String format, according to RDFFormat.
	 * </p>
	 * 
	 * @param format
	 *            The format of the String
	 * @return An equivalent String in the desired format
	 */
	String toString(RDFFormat format);

	/**
	 * <p>
	 * Adds a property to this compiled LSA.
	 * </p>
	 * <p>
	 * THIS METHOD SHOULD BE USED ONLY BY THE MIDDLEWARE, IN ORDER TO HANDLE
	 * SYNTHETIC PROPERTIES AND LOW-LEVEL ASPECTS.
	 * </p>
	 * 
	 * @param propURI
	 *            The URI of the property
	 * @param propValue
	 *            The value of the property (literal)
	 */
	void assertProperty(URI propURI, String propValue);

	/**
	 * <p>
	 * Adds a property to this compiled LSA.
	 * </p>
	 * <p>
	 * THIS METHOD SHOULD BE USED ONLY BY THE MIDDLEWARE, IN ORDER TO HANDLE
	 * SYNTHETIC PROPERTIES AND LOW-LEVEL ASPECTS.
	 * </p>
	 * 
	 * @param propURI
	 *            The URI of the property
	 * @param propValue
	 *            The value of the property (a URI)
	 */
	void assertProperty(URI propURI, URI propValue);

	/**
	 * <p>
	 * Reads a property value, considering it a literal.
	 * </p>
	 * 
	 * @param propURI
	 *            The URI of the property
	 * @return The property values
	 */
	String[] readLiteralProperty(URI propURI);

	/**
	 * <p>
	 * Reads a property value, considering it a URI.
	 * </p>
	 * 
	 * @param propURI
	 *            The URI of the property
	 * @return The property values
	 */
	URI[] readURIProperty(URI propURI);

	/**
	 * <p>
	 * Removes all information about the specified property from this compiled
	 * LSA.
	 * </p>
	 * 
	 * @param propURI
	 *            The URI of the property
	 */
	void clearProperty(URI propURI);
}
