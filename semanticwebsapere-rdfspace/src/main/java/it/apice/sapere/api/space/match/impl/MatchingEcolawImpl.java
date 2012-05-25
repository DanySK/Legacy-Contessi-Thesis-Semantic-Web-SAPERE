package it.apice.sapere.api.space.match.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.space.match.MatchResult;
import it.apice.sapere.api.space.match.MatchingEcolaw;

/**
 * <p>
 * This class implements the {@link MatchingEcolaw} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class MatchingEcolawImpl implements MatchingEcolaw {

	/** Reference to applied bindings. */
	private final transient MatchResult mResult;

	/** The concrete query. */
	private final String query;

	/**
	 * <p>
	 * Builds a new {@link MatchingEcolawImpl}.
	 * </p>
	 * 
	 * @param aQuery
	 *            The concrete query to be applied
	 * @param bindings
	 *            Applied bindings
	 */
	public MatchingEcolawImpl(final String aQuery, final MatchResult bindings) {
		if (aQuery == null) {
			throw new IllegalArgumentException("Invalid query provided");
		}

		if (bindings == null) {
			throw new IllegalArgumentException("Invalid bindings provided");
		}

		query = aQuery;
		mResult = bindings;
	}

	@Override
	public final void apply() throws SAPEREException {
		mResult.getLSAspace().apply(this);
	}

	@Override
	public final String getUpdateQuery() {
		return query;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result *= prime;
		if (query != null) {
			result += query.hashCode();
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
		final MatchingEcolawImpl other = (MatchingEcolawImpl) obj;
		if (query == null) {
			if (other.query != null) {
				return false;
			}
		} else if (!query.equals(other.query)) {
			return false;
		}
		return true;
	}

	@Override
	public final String toString() {
		return query;
	}

	@Override
	public final String getLabel() {
		return mResult.getRelCompiledEcolaw().getLabel();
	}

	@Override
	public MatchResult getAppliedMatch() {
		return mResult;
	}
}
