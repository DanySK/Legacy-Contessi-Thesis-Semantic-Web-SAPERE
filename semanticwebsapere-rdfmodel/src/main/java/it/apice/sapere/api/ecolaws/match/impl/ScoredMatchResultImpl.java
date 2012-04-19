package it.apice.sapere.api.ecolaws.match.impl;

import it.apice.sapere.api.ecolaws.match.MatchResult;
import it.apice.sapere.api.ecolaws.match.ScoredMatchResult;
import it.apice.sapere.api.ecolaws.scores.ScoreFunction;

/**
 * <p>
 * This class implements the {@link ScoredMatchResult} interface.
 * </p>
 *
 * @author Paolo Contessi
 *
 */
public class ScoredMatchResultImpl extends MatchResultImpl implements
		ScoredMatchResult, Comparable<ScoredMatchResult> {

	/** Matching score. */
	private final transient double score;

	/**
	 * <p>
	 * Builds a new {@link ScoredMatchResultImpl}.
	 * </p>
	 * 
	 * @param result
	 *            The matching result
	 * @param function
	 *            The score function
	 */
	public ScoredMatchResultImpl(final MatchResult result,
			final ScoreFunction function) {
		super(result);
		score = function.computeScore(result);
		setExtraInfo(String.format(":%.3f", score));
	}

	@Override
	public final double getTotalScore() {
		return score;
	}

	@Override
	public final int compareTo(final ScoredMatchResult o) {
		return -1 * new Double(score).compareTo(o.getTotalScore());
	}

}
