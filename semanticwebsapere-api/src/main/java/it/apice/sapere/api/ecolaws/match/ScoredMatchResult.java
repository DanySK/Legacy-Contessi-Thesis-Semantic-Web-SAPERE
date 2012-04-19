package it.apice.sapere.api.ecolaws.match;

/**
 * <p>
 * This interface models a MatchResult that can be updated, adding binding
 * information.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface ScoredMatchResult extends MatchResult {

	/**
	 * <p>
	 * Retrieves the score associated to this MatchResult.
	 * </p>
	 * 
	 * @return MatchResult Score
	 */
	double getTotalScore();
}
