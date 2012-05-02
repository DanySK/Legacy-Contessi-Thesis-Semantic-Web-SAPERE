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

	/** The name of variable used as rate. */
	private final String rateVar;

	/** Eco-law's label. */
	private final String label;

	/**
	 * <p>
	 * Builds a new {@link CompiledEcolawImpl}.
	 * </p>
	 * 
	 * @param mQuery
	 *            The query for matching phase
	 * @param uQuery
	 *            The query template for update phase
	 * @param rate
	 *            The rate variable name, or the rate value
	 */
	public CompiledEcolawImpl(final String mQuery,
			final MatchingEcolawTemplate uQuery, final String rate) {
		this(mQuery, uQuery, rate, null);
	}

	/**
	 * <p>
	 * Builds a new {@link CompiledEcolawImpl}.
	 * </p>
	 * 
	 * @param mQuery
	 *            The query for matching phase
	 * @param uQuery
	 *            The query template for update phase
	 * @param rate
	 *            The rate variable name, or the rate value
	 * @param lLabel
	 *            Eco-law's label
	 */
	public CompiledEcolawImpl(final String mQuery,
			final MatchingEcolawTemplate uQuery, final String rate,
			final String lLabel) {
		if (mQuery == null) {
			throw new IllegalArgumentException(
					"Invalid matching query provided");
		}

		if (uQuery == null) {
			throw new IllegalArgumentException(
					"Invalid update query template provided");
		}

		if (rate == null) {
			throw new IllegalArgumentException(
					"Invalid rate variable name provided");
		}

		sparql = mQuery;
		sparulTempl = uQuery;
		vars = sparulTempl.variablesNames();
		rateVar = rate;
		if (lLabel != null) {
			label = "<" + lLabel + ">";
		} else {
			label = "";
		}
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

	@Override
	public final String getRate() {
		return rateVar;
	}

	@Override
	public final boolean hasVariableRate() {
		return rateVar.startsWith("?");
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result *= prime;
		if (sparql != null) {
			result += sparql.hashCode();
		}

		result *= prime;
		if (sparulTempl != null) {
			result += sparulTempl.hashCode();
		}

		return result;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CompiledEcolawImpl other = (CompiledEcolawImpl) obj;
		if (sparql == null) {
			if (other.sparql != null) {
				return false;
			}
		} else if (!sparql.equals(other.sparql)) {
			return false;
		}
		if (sparulTempl == null) {
			if (other.sparulTempl != null) {
				return false;
			}
		} else if (!sparulTempl.equals(other.sparulTempl)) {
			return false;
		}
		return true;
	}

	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder();
		if (!label.equals("")) {
			builder.append(label).append(" ");
		}

		builder.append(sparql).append(" ").append(sparulTempl.toString());
		return builder.toString();
	}

	@Override
	public final String getLabel() {
		return label;
	}
}
