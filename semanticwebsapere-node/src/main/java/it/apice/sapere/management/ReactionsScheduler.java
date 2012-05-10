package it.apice.sapere.management;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.scheduling.SchedulableMatchResult;
import it.apice.sapere.api.space.match.MatchResult;
import it.apice.sapere.api.space.match.MatchingEcolaw;
import it.apice.sapere.api.space.observation.SpaceEvent;

import java.util.Map.Entry;

/**
 * <p>
 * This interface models an entity that, passed to the ReactionManager adds
 * features and modifies its behaviour as the system programmer desires.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface ReactionsScheduler extends ReactionManagerObserver {

	/**
	 * <p>
	 * Notifies that a proposed match can be scheduled at a specific time.
	 * </p>
	 * 
	 * @param match
	 *            A possible match
	 * @param schedulingTime
	 *            When it should be scheduled
	 * @return The scheduling time according to the strategy (generally equals
	 *         to schedulingTime parameter)
	 */
	long ecolawMatched(SchedulableMatchResult match, long schedulingTime);

	/**
	 * <p>
	 * Checks if, according to available strategy information, exists an eco-law
	 * ready to be scheduled.
	 * </p>
	 * 
	 * @return The next eco-law that should be scheduled (according to available
	 *         strategy information), or null if there's no proposal
	 */
	Entry<MatchingEcolaw, Long> next();

	/**
	 * <p>
	 * Checks if the given {@link SpaceEvent} has modified a precondition for
	 * the provided eco-law. If so an {@link AbortException} should be raised in
	 * order to revise the scheduling.
	 * </p>
	 * 
	 * @param ev
	 *            The occurred event
	 * @param law
	 *            The law that is going to be scheduled
	 * @throws AbortException
	 *             Next reaction should be re-scheduled
	 */
	void checkDependencies(SpaceEvent ev, MatchingEcolaw law)
			throws AbortException;

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
	SchedulableMatchResult eval(MatchResult mResult) throws SAPEREException;

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
	SchedulableMatchResult[] eval(MatchResult[] mResults)
			throws SAPEREException;
}
