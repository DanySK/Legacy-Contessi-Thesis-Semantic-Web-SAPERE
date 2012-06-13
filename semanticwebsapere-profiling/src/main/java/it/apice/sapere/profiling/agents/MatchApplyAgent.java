package it.apice.sapere.profiling.agents;

import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.node.NodeServices;
import it.apice.sapere.api.node.agents.SAPEREAgent;
import it.apice.sapere.api.node.agents.SAPERESysAgentSpec;
import it.apice.sapere.api.node.logging.LogUtils;
import it.apice.sapere.api.space.core.CompiledLSA;

import java.io.File;

/**
 * <p>
 * Profile agent which injects an LSA and adds an eco-law which will be
 * continuously triggered.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class MatchApplyAgent implements SAPERESysAgentSpec {

	/** Source of data. */
	private final transient File source;

	/**
	 * <p>
	 * Builds a new {@link MatchApplyAgent}.
	 * </p>
	 * 
	 * @param dataFile
	 *            Source of data
	 */
	public MatchApplyAgent(final File dataFile) {
		if (dataFile == null) {
			throw new IllegalArgumentException("Invalid source provided");
		}

		source = dataFile;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void behaviour(final NodeServices services, final LogUtils out,
			final SAPEREAgent me) throws Exception {
		out.log(String.format("Match-Apply profiling with data from \"%s\"..",
				source));

		services.getReactionManager().addEcolaw(
				services.getEcolawCompiler().create(getMatchQueryString(),
						getUpdateQueryString(), "1"));

		final CompiledLSA cLsa = services.getLSACompiler().parse(
				getLSAString(), RDFFormat.TURTLE);
		services.getLSAspace().inject(cLsa);

		out.log("Done. Match-Apply profiling completed");
	}

	/**
	 * <p>
	 * Retrieves the match query.
	 * </p>
	 * 
	 * @return SPARQL query
	 */
	private String getLSAString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("@prefix ex: <http://www.example.org/profile#>. ");
		builder.append("@prefix xsd: <http://www.w3.org/2001/XMLSchema#>. ");

		builder.append("ex:lsa01 <http://www.w3.org/1999/02/"
				+ "22-rdf-syntax-ns#type> <http://"
				+ "www.sapere-project.eu/ontologies/2012/0/"
				+ "sapere-model.owl#LSA> ; ");
		builder.append(" ex:prop \"0\"^^xsd:integer . ");

		return builder.toString();
	}

	/**
	 * <p>
	 * Retrieves the match query.
	 * </p>
	 * 
	 * @return SPARQL query
	 */
	private String getMatchQueryString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("PREFIX ex: <http://www.example.org/profile#> ");
		builder.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ");

		builder.append("SELECT DISTINCT * WHERE { ");
		builder.append("?lsa ex:prop ?value . ");

		return builder.append("}").toString();
	}

	/**
	 * <p>
	 * Retrieves the update query.
	 * </p>
	 * 
	 * @return SPARQL/Update query
	 */
	private String getUpdateQueryString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("PREFIX ex: <http://www.example.org/profile#> ");
		builder.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ");

		builder.append("MODIFY DELETE { !lsa ex:prop !value . } ");
		builder.append("INSERT { !lsa ex:prop ?newval . } ");
		builder.append("WHERE { ");
		builder.append("BIND(( xsd:integer(!value) + 1 ) AS ?newval) }");

		return builder.toString();
	}
}
