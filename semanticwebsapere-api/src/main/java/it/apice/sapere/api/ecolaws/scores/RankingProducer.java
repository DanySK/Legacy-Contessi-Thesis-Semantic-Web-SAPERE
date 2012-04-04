package it.apice.sapere.api.ecolaws.scores;

import it.apice.sapere.api.ecolaws.match.MatchResult;
import it.apice.sapere.api.ecolaws.match.ScoredMatchResult;

/**
 * <p>
 * This interface models an entity responsible for retrieving possible (Ecolaw)
 * matches, compute a score and produce a ranking.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface RankingProducer {

	/**
	 * <p>
	 * Produces a ranking of the possible matching alternatives, ordered by a
	 * score (internally computed with a <code>ScoreFunction</code>).
	 * </p>
	 * 
	 * @param alternatives
	 *            Possible matches for an ecolaw
	 * @return A list of matching solution ordered from the most promising to
	 *         the worst
	 */
	ScoredMatchResult[] rank(MatchResult[] alternatives);

}
