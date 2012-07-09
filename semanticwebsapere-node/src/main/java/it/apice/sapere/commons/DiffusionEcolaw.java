package it.apice.sapere.commons;

import it.apice.sapere.api.space.core.CompiledEcolaw;
import it.apice.sapere.api.space.core.EcolawCompiler;

/**
 * <p>
 * This class is meant to provide a DIFFUSION eco-law, according to WP1
 * definition.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public final class DiffusionEcolaw {

	/**
	 * <p>
	 * Hidden constructor.
	 * </p>
	 */
	private DiffusionEcolaw() {

	}

	/**
	 * <p>
	 * Retrieves a periodic diffusion eco-law, which selects LSAs by type and
	 * sends them to a specific neighbour.
	 * </p>
	 * 
	 * @param compiler
	 *            Reference to {@link EcolawCompiler}
	 * @param toName
	 *            Name of the node to which diffuse
	 * @param type
	 *            LSA's rdf:type to be diffused
	 * @param rate
	 *            Fixed Scheduling rate
	 * @return The eco-law, compiled
	 */
	public static CompiledEcolaw createDirectPeriodicDiffusionByType(
			final EcolawCompiler compiler, final String toName,
			final String type, final double rate) {
		return compiler.create(getMatchQuery(type, toName), getUpdateQuery(),
				"" + rate, "DIRECT-T-DIFF-BY-TYPE");
	}

	/**
	 * <p>
	 * Retrieves an ASAP diffusion eco-law, which selects LSAs by type and
	 * sends them to a specific neighbour.
	 * </p>
	 * 
	 * @param compiler
	 *            Reference to {@link EcolawCompiler}
	 * @param toName
	 *            Name of the node to which diffuse
	 * @param type
	 *            LSA's rdf:type to be diffused
	 * @return The eco-law, compiled
	 */
	public static CompiledEcolaw createDirectASAPDiffusionByType(
			final EcolawCompiler compiler, final String toName,
			final String type) {
		return compiler.create(getMatchQuery(type, toName), getUpdateQuery(),
				getASAPRate(), "DIRECT-ASAP-DIFF-BY-TYPE");
	}

	/**
	 * <p>
	 * Retrieves Match Query.
	 * </p>
	 * 
	 * @param type
	 *            LSA's rdf:type to be diffused
	 * @param toName
	 *            Name of the node to which diffuse
	 * @return SPARQL Query
	 */
	private static String getMatchQuery(final String type, 
			final String toName) {
		final StringBuilder builder = new StringBuilder();

		builder.append("PREFIX sapere: <http://www.sapere-project.eu/"
				+ "ontologies/2012/0/sapere-model.owl#>")
		.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ")
		.append("SELECT DISTINCT * WHERE { ")
		.append("?sourceLsa sapere-model:location sapere-model:local;")
		.append("rdf:type ").append(type).append(". ")
		.append("?newLoc rdf:type sapere:neighbour; ")
		.append("sapere:name ").append(toName).append(". ")
		.append("BIND (sapere-fn:generateLSA-id() AS !clonedLsa). } ");

		
		return builder.toString();
		// return "PREFIX sapere: <http://www.sapere-project.eu/"
		// + "ontologies/2012/0/sapere-model.owl#> "
		// + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
		// + "PREFIX sensing: <http://www.sapere-project.eu/distdemo#> "
		// + "SELECT DISTINCT * WHERE { " + "?observ rdf:type " + type
		// + "; sapere:location sapere:local. "
		// + "?newloc sapere:type sapere:neighbour; " + "sapere:name \""
		// + toName + "\" }";
	}

	/**
	 * <p>
	 * Retrieves Update Query.
	 * </p>
	 * 
	 * @return SPARQL/Update Query
	 */
	private static String getUpdateQuery() {
		final StringBuilder builder = new StringBuilder();

		builder.append(
				"PREFIX sapere: <http://www.sapere-project.eu/"
						+ "ontologies/2012/0/sapere-model.owl#> ")
				// 1. Create new blank-nodes
				.append("INSERT {!clonedLsa ?prop [ sapere-model:tmp ?bnode ")
				.append(")] . } WHERE { ")
				.append("!sourceLsa ?prop ?bnode. FILTER EXISTS { ")
				.append("?bnode ?prop2 ?value2.	}}; ")
				// 2. Fill each blank-node with relative nested data
				.append("MODIFY DELETE { ?newBNode sapere-model:tmp ?bnode. ")
				.append("} INSERT { ?newBNode ?bProp ?bValue. } WHERE { ")
				.append("!sourceLsa ?prop ?bnode. ?bnode ?bProp ?bValue. ")
				.append("!clonedLsa ?prop ?newBNode. ")
				.append("?newBNode sapere-model:tmp ?bnode. }; ")
				// 3. Clone the remaining triples
				.append("INSERT { !clonedLsa ?prop ?value. } WHERE { ")
				.append("!sourceLsa ?prop ?value. 	FILTER NOT EXISTS { ")
				.append("?value ?bProp ?bValue. }}; ")
				// 4. Change location
				.append("MODIFY DELETE { !clonedLsa ")
				.append("sapere-model:location sapere-model:local. ")
				.append("} INSERT { !clonedLsa sapere-model:location ")
				.append("?newLoc. } WHERE { }");

		return builder.toString();
	}

	/**
	 * <p>
	 * Retrieves Query Rate.
	 * </p>
	 * 
	 * @return Scheduling Rate
	 */
	private static String getASAPRate() {
		return "" + Double.MAX_VALUE;
	}

}
