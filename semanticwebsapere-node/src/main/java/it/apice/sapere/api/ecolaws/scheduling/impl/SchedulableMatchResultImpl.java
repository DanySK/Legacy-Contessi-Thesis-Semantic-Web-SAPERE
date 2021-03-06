package it.apice.sapere.api.ecolaws.scheduling.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.scheduling.SchedulableMatchResult;
import it.apice.sapere.api.space.core.CompiledEcolaw;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.match.MatchResult;
import it.apice.sapere.api.space.match.MatchingEcolaw;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * This class implements the {@link SchedulableMatchResult} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class SchedulableMatchResultImpl implements SchedulableMatchResult,
		Comparable<SchedulableMatchResult> {

	/** Seconds to Millieconds conversion constant. */
	private static final transient double S_TO_MS = 1000;

	/** Reference to match result. */
	private final MatchResult result;

	/** Computed wait time. */
	private final long time;

	/**
	 * <p>
	 * Builds a new {@link SchedulableMatchResultImpl}.
	 * </p>
	 * 
	 * @param mResult
	 *            The match result that contains the bindings
	 * @param waitTime
	 *            The (computed) scheduling interleaving time
	 */
	public SchedulableMatchResultImpl(final MatchResult mResult,
			final double waitTime) {
		if (mResult == null) {
			throw new IllegalArgumentException("Invalid match result provided");
		}

		if (waitTime <= 0) {
			throw new IllegalArgumentException("Invalid wait time");
		}

		result = mResult;
		time = Math.round(waitTime * S_TO_MS);
	}

	@Override
	public final String lookup(final String varName) throws SAPEREException {
		return result.lookup(varName);
	}

	@Override
	public final Double getAssignmentScore(final String varName)
			throws SAPEREException {
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

	@Override
	public final CompiledEcolaw getRelCompiledEcolaw() {
		return result.getRelCompiledEcolaw();
	}

	@Override
	public final int hashCode() {
		return result.hashCode();
	}

	@Override
	public final boolean equals(final Object obj) {
		return result.equals(obj);
	}

	@Override
	public final String toString() {
		return "SchedulableMatchResultImpl [result=" + result + ", time="
				+ time + "]";
	}

	@Override
	public final int compareTo(final SchedulableMatchResult other) {
		return Double.compare(time, other.getWaitTime());
	}

	@Override
	public final long getWaitTime() {
		return time;
	}

	@Override
	public final long getSchedulingTime(final long currentTime,
			final TimeUnit tu) {
		return tu.convert(tu.toMillis(currentTime) + time,
				TimeUnit.MILLISECONDS);
	}

	@Override
	public final MatchingEcolaw bind() throws SAPEREException {
		return getRelCompiledEcolaw().getUpdateQueryTemplate().bind(this);
	}

}
