package it.apice.sapere.api.ecolaws.match;

/**
 * <p>
 * This interface models a MatchResult that can be scheduled at the computed
 * rate.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface RatedMatchResult extends MatchResult {

	/**
	 * <p>
	 * Retrieves the rate associated to this MatchResult.
	 * </p>
	 * 
	 * @return The computed rate
	 */
	double getRate();
}
