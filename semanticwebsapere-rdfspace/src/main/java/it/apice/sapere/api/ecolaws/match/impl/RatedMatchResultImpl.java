package it.apice.sapere.api.ecolaws.match.impl;

import it.apice.sapere.api.ecolaws.match.MatchResult;
import it.apice.sapere.api.ecolaws.match.RatedMatchResult;
import it.apice.sapere.api.space.core.LSAspaceCore;

/**
 * <p>
 * This class implements the {@link RatedMatchResult} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class RatedMatchResultImpl implements RatedMatchResult {

	/** Reference to match result. */
	private final MatchResult result;

	/** Computed scheduling rate. */
	private final double rate;

	/**
	 * <p>
	 * Builds a new {@link RatedMatchResultImpl}.
	 * </p>
	 * 
	 * @param mResult
	 *            The match result that contains the bindings
	 * @param mRate
	 *            The (computed) scheduling rate
	 */
	public RatedMatchResultImpl(final MatchResult mResult, final double mRate) {
		if (mResult == null) {
			throw new IllegalArgumentException("Invalid match result provided");
		}

		if (mRate <= 0) {
			throw new IllegalArgumentException("Invalid rate");
		}

		result = mResult;
		rate = mRate;
	}

	@Override
	public final double getRate() {
		return rate;
	}

	@Override
	public final String lookup(final String varName) {
		return result.lookup(varName);
	}

	@Override
	public final Double getAssignmentScore(final String varName) {
		return result.getAssignmentScore(varName);
	}

	@Override
	public final String[] getAllMatchedVariablesNames() {
		return result.getAllMatchedVariablesNames();
	}

	@Override
	public final LSAspaceCore<?> getLSAspace() {
		return result.getLSAspace();
	}

}
