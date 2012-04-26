package it.apice.sapere.api;

/**
 * <p>
 * This enumeration defines supported RDFFormat for parsing.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public enum RDFFormat {
	/** RDF/XML format. */
	RDF_XML("RDF/XML"),
	/** Turtle format (preferred). */
	TURTLE("TURTLE"),
	/** N3 format (super-set of Turtle). */
	N3("N3"),
	/** N-Triples. */
	N_TRIPLES("N-TRIPLE");

	/** Jena compatible lang value. */
	private final String value;

	/**
	 * <p>
	 * Builds a new RDFFormat.
	 * </p>
	 * 
	 * @param val
	 *            The lang parameter for Jena's Model.read method
	 */
	private RDFFormat(final String val) {
		value = val;
	}

	@Override
	public String toString() {
		return value;
	}
}
