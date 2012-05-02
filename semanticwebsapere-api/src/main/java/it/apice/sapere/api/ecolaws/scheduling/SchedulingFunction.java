package it.apice.sapere.api.ecolaws.scheduling;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.space.match.MatchResult;

/**
 * <p>
 * This interface models a function which determines when the eco-law, with
 * specific bindings, can be scheduled.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface SchedulingFunction {

	/**
	 * <p>
	 * Evaluates the match and computes when the relative eco-law can be
	 * scheduled.
	 * </p>
	 * 
	 * @param mResult
	 *            Eco-law's proposed bindings
	 * @return A {@link SchedulableMatchResult} which provides bindings and how
	 *         many milliseconds should be waited before scheduling the eco-law
	 *         (bound to provided values).
	 * @throws SAPEREException
	 *             Cannot compute a rate
	 */
	SchedulableMatchResult eval(final MatchResult mResult)
			throws SAPEREException;

	/**
	 * <p>
	 * Evaluates scheduling time over a list of {@link MatchResult}s.
	 * </p>
	 * 
	 * @param mResults
	 *            Eco-law's proposed bindings list
	 * @return A list of {@link SchedulableMatchResult}, each one indicating how
	 *         many milliseconds should be waited before scheduling the eco-law
	 *         (bound to provided values).
	 * @throws SAPEREException
	 *             Cannot compute a rate
	 */
	SchedulableMatchResult[] eval(final MatchResult[] mResults)
			throws SAPEREException;
}
