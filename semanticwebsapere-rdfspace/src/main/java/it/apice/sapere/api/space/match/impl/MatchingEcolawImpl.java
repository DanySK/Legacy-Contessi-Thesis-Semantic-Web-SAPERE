package it.apice.sapere.api.space.match.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.space.core.LSAspaceCore;
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

	/** Reference to the (local) LSA-space. */
	private final transient LSAspaceCore<?> space;

	/** The concrete query. */
	private final String query;

	/**
	 * <p>
	 * Builds a new {@link MatchingEcolawImpl}.
	 * </p>
	 * 
	 * @param aQuery
	 *            The concrete query to be applied
	 * @param lsaSpace
	 *            The LSA-space we're working on
	 */
	public MatchingEcolawImpl(final String aQuery,
			final LSAspaceCore<?> lsaSpace) {
		if (aQuery == null) {
			throw new IllegalArgumentException("Invalid query provided");
		}

		if (lsaSpace == null) {
			throw new IllegalArgumentException("Invalid bindings provided");
		}

		query = aQuery;
		space = lsaSpace;
	}

	@Override
	public final void apply() throws SAPEREException {
		space.apply(this);
	}

	@Override
	public final String getUpdateQuery() {
		return query;
	}

}
