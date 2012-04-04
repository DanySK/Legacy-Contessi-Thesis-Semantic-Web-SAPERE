package it.apice.sapere.api.ecolaws.scores;

import it.apice.sapere.api.ecolaws.match.MatchResult;

/**
 * <p>
 * This interface models an entity responsible for computing a score for the
 * proposed match.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface ScoreFunction {

	/**
	 * <p>
	 * Computes a score for the match.
	 * </p>
	 * 
	 * @param bindings
	 *            The proposed match
	 * @return The score
	 */
	double computeScore(MatchResult bindings);
}
