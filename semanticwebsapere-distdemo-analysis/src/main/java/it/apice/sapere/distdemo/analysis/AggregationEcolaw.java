package it.apice.sapere.distdemo.analysis;

import it.apice.sapere.api.space.core.CompiledEcolaw;
import it.apice.sapere.api.space.core.EcolawCompiler;

/**
 * <p>
 * Utility class which provides pre-compiled aggregation eco-law.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public final class AggregationEcolaw {

	/**
	 * <p>
	 * Hidden constructor.
	 * </p>
	 */
	private AggregationEcolaw() {

	}

	/**
	 * <p>
	 * Retrieves an ASAP max-aggregation eco-law.
	 * </p>
	 * 
	 * @param compiler
	 *            Reference to {@link EcolawCompiler}
	 * @param aggregType
	 *            LSA's rdf:type to be aggregated
	 * @return The eco-law, compiled
	 */
	public static CompiledEcolaw createASAPMaxAggregation(
			final EcolawCompiler compiler, final String aggregType) {
		return compiler.create(getASAPMatchQuery(aggregType),
				getASAPUpdateQuery(), getASAPRate());
	}

	/**
	 * <p>
	 * Retrieves Match Query.
	 * </p>
	 * 
	 * @param type
	 *            LSA's rdf:type to be aggregated
	 * @return SPARQL Query
	 */
	private static String getASAPMatchQuery(final String type) {
		return "PREFIX sapere: <http://www.sapere-project.eu/"
				+ "ontologies/2012/0/sapere-model.owl#> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX sensing: <http://www.sapere-project.eu/distdemo#> "
				+ "SELECT DISTINCT * WHERE { "
				+ "?observ rdf:type " + type + ". "
				+ " }";
	}

	/**
	 * <p>
	 * Retrieves Update Query.
	 * </p>
	 * 
	 * @return SPARQL/Update Query
	 */
	private static String getASAPUpdateQuery() {
		return "PREFIX sapere: <http://www.sapere-project.eu/"
				+ "ontologies/2012/0/sapere-model.owl#> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX sensing: <http://www.sapere-project.eu/distdemo#> "
				+ "MODIFY DELETE { !observ ?obs_prop ?obs_val . } "
				+ "INSERT {  } WHERE { !observ ?obs_prop ?obs_val . }";
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
