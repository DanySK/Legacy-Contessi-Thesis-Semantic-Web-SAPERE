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
public abstract class AbstractDiffusionEcolawFactory {

	/** Name of the rate variable. */
	private static final transient String RATE_VAR = "?rDiffByType";

	/** The eco-law. */
	private final transient CompiledEcolaw law;

	/**
	 * <p>
	 * Builds a new {@link AbstractDiffusionEcolawFactory}.
	 * </p>
	 * 
	 * @param compiler
	 *            Reference to {@link EcolawCompiler}
	 */
	protected AbstractDiffusionEcolawFactory(final EcolawCompiler compiler) {
		law = compiler.create(getMatchQuery(), getUpdateQuery(),
				RATE_VAR, "T-DIFF");
	}

	/**
	 * <p>
	 * Retrieves a periodic diffusion eco-law, which selects LSAs by type and
	 * sends them to a specific neighbour.
	 * </p>
	 * 
	 * @return The eco-law, compiled
	 */
	public final CompiledEcolaw getEcolaw() {
		return law;
	}

	/**
	 * <p>
	 * Defines a set of SPARQL statements (inside the WHERE clause) that will be
	 * used to determine DIFFUSE eligible LSAs.
	 * </p>
	 * 
	 * @param sourceLsaVar
	 *            The string to be used as variable name for the candidate LSA
	 *            resource
	 * @return A set of SPARQL statements that will be put inside the WHERE
	 *         clause
	 */
	protected abstract String getSourceLSASelector(String sourceLsaVar);

	/**
	 * <p>
	 * Defines a list of names that are diffusion destinations.
	 * </p>
	 * 
	 * @return A list of nodes involved in diffusion (their name)
	 */
	protected abstract String[] getDestinationsNames();

	/**
	 * <p>
	 * Specifies how the eco-law should retrieve the rate.
	 * </p>
	 * 
	 * @param rateVar
	 *            The string to be used as variable name for the rate
	 * @return Eco-law scheduling rate
	 */
	protected abstract String getRateValue(String rateVar);

	/**
	 * <p>
	 * Retrieves Match Query.
	 * </p>
	 * 
	 * @return SPARQL Query
	 */
	private String getMatchQuery() {
		final StringBuilder builder = new StringBuilder();

		builder.append(
				"PREFIX sapere: <http://www.sapere-project.eu/"
						+ "ontologies/2012/0/sapere-model.owl#>")
				.append("PREFIX rdf: "
						+ "<http://www.w3.org/1999/02/22-rdf-syntax-ns#> ")
				.append("SELECT DISTINCT * WHERE { ")
				.append("?sourceLsa sapere-model:location sapere-model:local. ")
				.append(getSourceLSASelector("?sourceLsa")).append(" ")
				.append(getRateValue(RATE_VAR)).append(" ");

		final String[] toNames = getDestinationsNames();
		for (int i = 0; i < toNames.length; i++) {
			builder.append("?newLoc").append(i)
					.append(" rdf:type sapere:neighbour; ")
					.append("sapere:name ").append(toNames[i]).append(". ");
		}

		builder.append("BIND (sapere-fn:generateLSA-id() AS !clonedLsa). } ");

		return builder.toString();
	}

	/**
	 * <p>
	 * Retrieves Update Query.
	 * </p>
	 * 
	 * @return SPARQL/Update Query
	 */
	private String getUpdateQuery() {
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
				.append("} INSERT { ");

		final String[] toNames = getDestinationsNames();
		for (int i = 0; i < toNames.length; i++) {
			builder.append("!clonedLsa sapere-model:location ")
					.append("!newLoc").append(i).append(". ");
		}

		builder.append(" } WHERE { }");

		return builder.toString();
	}

}
