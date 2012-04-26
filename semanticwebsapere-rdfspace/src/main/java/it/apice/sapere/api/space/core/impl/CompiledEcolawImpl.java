package it.apice.sapere.api.space.core.impl;

import it.apice.sapere.api.space.core.CompiledEcolaw;
import it.apice.sapere.api.space.match.MatchingEcolawTemplate;

/**
 * <p>
 * This class implements the {@link CompiledEcolaw} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class CompiledEcolawImpl implements CompiledEcolaw {

	/** The SPARQL query for matching phase. */
	private final String sparql;

	/** The SPARUL query template for update phase. */
	private final MatchingEcolawTemplate sparulTempl;

	/** List of shared variables. */
	private final String[] vars;

	/**
	 * <p>
	 * Builds a new {@link CompiledEcolawImpl}.
	 * </p>
	 * 
	 * @param mQuery
	 *            The query for matching phase
	 * @param uQuery
	 *            The query template for update phase
	 */
	public CompiledEcolawImpl(final String mQuery,
			final MatchingEcolawTemplate uQuery) {
		if (mQuery == null) {
			throw new IllegalArgumentException(
					"Invalid matching query provided");
		}

		if (uQuery == null) {
			throw new IllegalArgumentException(
					"Invalid update query template provided");
		}

		sparql = mQuery;
		sparulTempl = uQuery;
		vars = sparulTempl.variablesNames();
	}

	@Override
	public final String getMatchQuery() {
		return sparql;
	}

	@Override
	public final String[] variablesNames() {
		return vars;
	}

	@Override
	public final MatchingEcolawTemplate getUpdateQueryTemplate() {
		return sparulTempl;
	}

}
