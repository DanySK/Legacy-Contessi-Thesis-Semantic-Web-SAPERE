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
	 * @param destType
	 *            LSA's rdf:type where the aggregated value should be published
	 * @return The eco-law, compiled
	 */
	public static CompiledEcolaw createASAPMaxAggregation1(
			final EcolawCompiler compiler, final String aggregType,
			final String destType) {
		return compiler.create(getMatchQuery1(aggregType, destType),
				getUpdateQuery1(), getASAPRate(), "1-AGGR");
	}

	/**
	 * <p>
	 * Retrieves an ASAP max-aggregation eco-law, for aggregation with
	 * comparsison.
	 * </p>
	 * 
	 * @param compiler
	 *            Reference to {@link EcolawCompiler}
	 * @param aggregType
	 *            LSA's rdf:type to be aggregated
	 * @param destType
	 *            LSA's rdf:type where the aggregated value should be published
	 * @return The eco-law, compiled
	 */
	public static CompiledEcolaw createASAPMaxAggregation2(
			final EcolawCompiler compiler, final String aggregType,
			final String destType) {
		return compiler.create(getMatchQuery2(aggregType, destType),
				getUpdateQuery2(), getASAPRate(), "2-AGGR");
	}

	/**
	 * <p>
	 * Retrieves Match Query.
	 * </p>
	 * 
	 * @param obsType
	 *            LSA's rdf:type to be aggregated
	 * @param sitType
	 *            LSA's rdf:type where the aggregated value should be published
	 * @return SPARQL Query
	 */
	private static String getMatchQuery1(final String obsType,
			final String sitType) {
		final StringBuilder builder = initWithPrefixes();

		builder.append("SELECT DISTINCT * WHERE { ");

		appendBasicMatch(builder, obsType, sitType);
		builder.append("FILTER NOT EXISTS { ");
		appendFilter(builder);

		builder.append("} ");

		builder.append("}");

		return builder.toString();
	}

	/**
	 * <p>
	 * Retrieves Match Query for aggregation with comparison.
	 * </p>
	 * 
	 * @param obsType
	 *            LSA's rdf:type to be aggregated
	 * @param sitType
	 *            LSA's rdf:type where the aggregated value should be published
	 * @return SPARQL Query
	 */
	private static String getMatchQuery2(final String obsType,
			final String sitType) {
		final StringBuilder builder = initWithPrefixes();

		builder.append("SELECT DISTINCT * WHERE { ");

		appendBasicMatch(builder, obsType, sitType);
		appendFilter(builder);

		builder.append("}");

		return builder.toString();
	}

	/**
	 * <p>
	 * Retrieves Update Query.
	 * </p>
	 * 
	 * @return SPARQL/Update Query
	 */
	private static String getUpdateQuery1() {
		final StringBuilder builder = initWithPrefixes();

		builder.append("MODIFY DELETE { ");
		appendObsCleanup(builder);

		builder.append("} INSERT { ");
		appendInsert(builder);

		builder.append("} WHERE { ");
		appendObsSelect(builder);
		builder.append("BIND(!obs_value AS ?max_value). ");
//		builder.append("BIND(!obs AS ?proven). ");

		builder.append("} ");

		return builder.toString();
	}

	/**
	 * <p>
	 * Retrieves Update Query, for aggregation with comparison.
	 * </p>
	 * 
	 * @return SPARQL/Update Query
	 */
	private static String getUpdateQuery2() {
		final StringBuilder builder = initWithPrefixes();

		builder.append("MODIFY DELETE { ");
		appendObsCleanup(builder);
		builder.append("!situ situation:situation !situ_val; ");
		builder.append("sapere:updateTime !upd_time. ");

		builder.append("} INSERT {");
		appendInsert(builder);

		builder.append("} WHERE {");
		appendObsSelect(builder);
		builder.append("BIND(afn:max(!situ_val, !obs_value) AS ?max_value). ");
//		builder.append("BIND(sapere:source(?max_value, "
//				+ "!obs_value, !obs, !source_obs) AS ?proven). ");

		builder.append("}");

		return builder.toString();
	}

	/**
	 * <p>
	 * Appends the insert clause.
	 * </p>
	 * 
	 * @param builder
	 *            Query builder
	 * @return The updated Query builder
	 */
	private static StringBuilder appendInsert(final StringBuilder builder) {
		builder.append("!situ situation:situation ?max_value; ");
		builder.append("sapere:updateTime ?now; ");
//		builder.append("provenance:derivedFrom ?proven");

		return builder;
	}

	/**
	 * <p>
	 * Appends observation selection for deletion and current time retrieval.
	 * </p>
	 * 
	 * @param builder
	 *            Query builder
	 * @return The updated Query builder
	 */
	private static StringBuilder appendObsSelect(final StringBuilder builder) {
		builder.append("!obs ?obs_prop ?obs_obj. ");
		builder.append("BIND (afn:now() AS ?now). ");

		return builder;
	}

	/**
	 * <p>
	 * Appends observation deletion and other minor stuffs.
	 * </p>
	 * 
	 * @param builder
	 *            Query builder
	 * @return The updated Query builder
	 */
	private static StringBuilder appendObsCleanup(final StringBuilder builder) {
		builder.append("!obs ?obs_prop ?obs_obj. ");
		builder.append("!situ sapere:updateTime !upd_time. ");

		return builder;
	}

	/**
	 * <p>
	 * Retrieves prefix declaration and initializes the query.
	 * </p>
	 * 
	 * @return A {@link StringBuilder} with pre-initialized prefixes
	 */
	private static StringBuilder initWithPrefixes() {
		final StringBuilder builder = new StringBuilder();

		builder.append("PREFIX rdf: "
				+ "<http://www.w3.org/1999/02/22-rdf-syntax-ns#> ");
		builder.append("PREFIX afn: "
				+ "<http://jena.hpl.hp.com/ARQ/function#> ");
		builder.append("PREFIX sapere: <http://www.sapere-project.eu/"
				+ "ontologies/2012/0/sapere-model.owl#> ");
		builder.append("PREFIX sensing: "
				+ "<http://www.sapere-project.eu/distdemo#> ");
		builder.append("PREFIX situation: "
				+ "<http://www.sapere-project.eu/distdemo/analyzer#> ");
		builder.append("PREFIX provenance: "
				+ "<http://www.sapere-project.eu/distdemo/provenance#> ");

		return builder;
	}

	/**
	 * <p>
	 * Appends basic match for an observation and a situation.
	 * </p>
	 * 
	 * @param builder
	 *            Query builder
	 * @param obsType
	 *            Type of observation
	 * @param situType
	 *            Type of situation
	 * @return The updated Query builder
	 */
	private static StringBuilder appendBasicMatch(final StringBuilder builder,
			final String obsType, final String situType) {
		builder.append("?obs rdf:type ").append(obsType).append("; ");
		builder.append("sensing:value ?obs_value. ");
		builder.append("?situ rdf:type ").append(situType).append("; ");
		builder.append("sapere:updateTime ?upd_time. ");

		return builder;
	}

	/**
	 * <p>
	 * Appends further filter.
	 * </p>
	 * 
	 * @param builder
	 *            Query builder
	 * 
	 * @return The updated Query builder
	 */
	private static StringBuilder appendFilter(final StringBuilder builder) {
		builder.append("?situ situation:situation ?situ_val; ");
//		builder.append("provenance:derivedFrom ?source_obs. ");

		return builder;
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
