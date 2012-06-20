package it.apice.sapere.api.space;

import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.SAPEREException;

import java.net.URI;

/**
 * <p>
 * This interface models an entity capable of loading ontologies.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface OntologyHandler {

	/**
	 * <p>
	 * Instructs the LSA-space to load and exploit the provided URI.
	 * </p>
	 * 
	 * @param ontoURI
	 *            Where the ontology can be found
	 * @throws SAPEREException
	 *             Cannot load ontology
	 */
	void loadOntology(URI ontoURI) throws SAPEREException;

	/**
	 * <p>
	 * Retrieves a list of all ontologies that this entity is aware of.
	 * </p>
	 * 
	 * @return A list of all loaded ontologies
	 */
	URI[] getLoadedOntologies();

	/**
	 * <p>
	 * Instructs the LSA-space to load and exploit the provided URI.
	 * </p>
	 * 
	 * @param ontoURI
	 *            Where the ontology can be found
	 * @param format
	 *            The {@link RDFFormat} of the provided ontology
	 * @throws SAPEREException
	 *             Cannot load ontology
	 */
	void loadOntology(URI ontoURI, RDFFormat format) throws SAPEREException;
}
