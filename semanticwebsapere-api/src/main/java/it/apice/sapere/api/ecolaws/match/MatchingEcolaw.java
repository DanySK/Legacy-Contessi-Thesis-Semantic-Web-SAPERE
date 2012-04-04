package it.apice.sapere.api.ecolaws.match;

import it.apice.sapere.api.ecolaws.Ecolaw;

/**
 * <p>
 * This interface models an Ecolaw whose variables has been bound to some values
 * after a matching phase.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface MatchingEcolaw extends Ecolaw {

	/**
	 * <p>
	 * Applies this ecolaw to the LSA-space.
	 * </p>
	 */
	void apply();

	/**
	 * <p>
	 * Retrieves the score associated to this MatchingEcolaw.
	 * </p>
	 * 
	 * @return Matching Ecolaw Score
	 */
	double getScore();
}
