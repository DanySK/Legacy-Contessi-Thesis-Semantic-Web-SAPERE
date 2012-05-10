package it.apice.sapere.api.ecolaws.scheduling;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.space.match.MatchResult;
import it.apice.sapere.api.space.match.MatchingEcolaw;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * This interface models a MatchResult that can be scheduled at the computed
 * rate.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface SchedulableMatchResult extends MatchResult {

	/**
	 * <p>
	 * Retrieves how many milliseconds should be waited before scheduling the
	 * relative eco-law.
	 * </p>
	 * 
	 * @return Milliseconds to scheduling time
	 */
	long getWaitTime();

	/**
	 * <p>
	 * Computes the exact scheduling time, according to provided current time.
	 * </p>
	 * 
	 * @param currentTime
	 *            Current time
	 * @param tu
	 *            Unit of Measure of the provided (and desired) time value
	 * @return Scheduling time (in <code>tu</code> unit of measure)
	 */
	long getSchedulingTime(long currentTime, TimeUnit tu);

	/**
	 * <p>
	 * Binds the eco-law with proposed bindings and retrieves the resulting
	 * {@link MatchingEcolaw}.
	 * </p>
	 * 
	 * @return A {@link MatchingEcolaw} to be applied
	 * @throws SAPEREException
	 *             Cannot complete the binding
	 */
	MatchingEcolaw bind() throws SAPEREException;
}
