package it.apice.sapere.api;

import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.nodes.SAPEREException;

import java.io.InputStream;
import java.io.Reader;
import java.util.Set;

/**
 * <p>
 * A SAPEREParser provides basic functionalities that enables the creation of
 * LSAs from a description. This description should be provided sin one of the
 * following Semantic Web formats:
 * </p>
 * <ul>
 * <li>RDF/XML</li>
 * <li>Turtle</li>
 * <li>N-Triples</li>
 * </ul>
 * 
 * @author Paolo Contessi
 * 
 */
public interface SAPEREParser {

	/**
	 * <p>
	 * Parses the provided String in order to create a bunch of LSAs.
	 * </p>
	 * <p>
	 * Supported formats are:
	 * </p>
	 * <ul>
	 * <li>RDF/XML</li>
	 * <li>Turtle</li>
	 * <li>N-Triples</li>
	 * </ul>
	 * 
	 * @param input
	 *            A String representation of LSAs in the form of RDF/XML, Turtle
	 *            or N-Triples.
	 * @return All parsed LSAs
	 * @throws SAPEREException
	 *             Cannot complete creation due to some model constraints
	 *             violation or invalid parameters
	 */
	Set<LSA> parseLSAs(String input) throws SAPEREException;

	/**
	 * <p>
	 * Parses the provided String in order to create a bunch of LSAs.
	 * </p>
	 * <p>
	 * Supported formats are:
	 * </p>
	 * <ul>
	 * <li>RDF/XML</li>
	 * <li>Turtle</li>
	 * <li>N-Triples</li>
	 * </ul>
	 * 
	 * @param input
	 *            An Input Stream from which the representation of LSA can be
	 *            retrieved
	 * @return All parsed LSAs
	 * @throws SAPEREException
	 *             Cannot complete creation due to some model constraints
	 *             violation or invalid parameters
	 */
	Set<LSA> parseLSAs(InputStream input) throws SAPEREException;

	/**
	 * <p>
	 * Parses the provided String in order to create a bunch of LSAs.
	 * </p>
	 * <p>
	 * Supported formats are:
	 * </p>
	 * <ul>
	 * <li>RDF/XML</li>
	 * <li>Turtle</li>
	 * <li>N-Triples</li>
	 * </ul>
	 * 
	 * @param input
	 *            A Reader from which the representation of LSA can be retrieved
	 * @return All parsed LSAs
	 * @throws SAPEREException
	 *             Cannot complete creation due to some model constraints
	 *             violation or invalid parameters
	 */
	Set<LSA> parseLSAs(Reader input) throws SAPEREException;

}
