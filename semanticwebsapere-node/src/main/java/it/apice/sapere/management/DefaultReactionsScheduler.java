package it.apice.sapere.management;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.scheduling.SchedulableMatchResult;
import it.apice.sapere.api.ecolaws.scheduling.SchedulingFunction;
import it.apice.sapere.api.ecolaws.scheduling.impl.SchedulingFunctionImpl;
import it.apice.sapere.api.space.core.CompiledEcolaw;
import it.apice.sapere.api.space.match.MatchResult;
import it.apice.sapere.api.space.match.MatchingEcolaw;
import it.apice.sapere.api.space.observation.SpaceEvent;

import java.util.Map.Entry;

/**
 * <p>
 * This class provides a default implementation of a
 * {@link ReactionsScheduler}.
 * </p>
 * <p>
 * Default behaviour consists in providing a CTMC-based scheduling function and
 * no optimization on dependency check (always abort).
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class DefaultReactionsScheduler implements ReactionsScheduler {

	/** Reference to adopted {@link SchedulingFunction}. */
	private final transient SchedulingFunction sFunct;

	/**
	 * <p>
	 * Builds a new {@link DefaultReactionsScheduler}.
	 * </p>
	 */
	public DefaultReactionsScheduler() {
		sFunct = new SchedulingFunctionImpl();
	}

	/**
	 * <p>
	 * Builds a new {@link DefaultReactionsScheduler}.
	 * </p>
	 * 
	 * @param seed
	 *            Seed for exploited Random Number Generator
	 */
	public DefaultReactionsScheduler(final long seed) {
		sFunct = new SchedulingFunctionImpl(seed);
	}

	@Override
	public void ecolawAdded(final CompiledEcolaw law) {

	}

	@Override
	public void ecolawRemoved(final CompiledEcolaw law) {

	}

	@Override
	public void ecolawEnabled(final MatchingEcolaw law, final long time) {

	}

	@Override
	public void ecolawApplied(final MatchingEcolaw law, final long time) {

	}

	@Override
	public final long ecolawMatched(final SchedulableMatchResult match,
			final long schedulingTime) {
		return schedulingTime;
	}

	@Override
	public final Entry<MatchingEcolaw, Long> next() {
		return null;
	}

	@Override
	public final void checkDependencies(final SpaceEvent ev,
			final MatchingEcolaw law) throws AbortException {
		throw new AbortException();
	}

	@Override
	public final SchedulableMatchResult eval(final MatchResult mResult)
			throws SAPEREException {
		return sFunct.eval(mResult);
	}

	@Override
	public final SchedulableMatchResult[] eval(final MatchResult[] mResults)
			throws SAPEREException {
		return sFunct.eval(mResults);
	}
}
