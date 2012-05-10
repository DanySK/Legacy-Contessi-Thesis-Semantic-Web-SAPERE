package it.apice.sapere.management.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.scheduling.SchedulableMatchResult;
import it.apice.sapere.api.space.core.CompiledEcolaw;
import it.apice.sapere.api.space.core.EcolawCompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.match.MatchingEcolaw;
import it.apice.sapere.api.space.observation.SpaceEvent;
import it.apice.sapere.api.space.observation.SpaceObserver;
import it.apice.sapere.management.AbortException;
import it.apice.sapere.management.ReactionManager;
import it.apice.sapere.management.ReactionManagerObserver;
import it.apice.sapere.management.ReactionsScheduler;
import it.apice.sapere.node.agents.NodeServices;
import it.apice.sapere.node.agents.impl.AbstractSystemAgent;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * This class provides an implementation for the {@link ReactionManager}
 * interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class ReactionManagerImpl extends AbstractSystemAgent implements
		ReactionManager, SpaceObserver {

	/** Reference to the EcolawCompiler. */
	private final transient EcolawCompiler compiler;

	/** Eco-law list. */
	private final transient List<CompiledEcolaw> laws = 
			new LinkedList<CompiledEcolaw>();

	/** Observers list. */
	private final transient List<ReactionManagerObserver> obss = 
			new LinkedList<ReactionManagerObserver>();

	/** Next law to be applied (scheduled). */
	private transient MatchingEcolaw next;

	/** Time at which the scheduler should apply the chosen law. */
	private transient long wakeUpTime = Long.MAX_VALUE;

	/** Thread-safety mutex. */
	private final transient Lock mutex = new ReentrantLock();

	/** Condition variable on scheduling events. */
	private final transient Condition schedulingEvent = mutex.newCondition();

	/** Flag that indicate that programmed eco-law should be verified. */
	private transient boolean abortScheduling;

	/** The internal scheduler. */
	private final transient ReactionsScheduler scheduler;

	/**
	 * <p>
	 * Builds a new {@link ReactionManagerImpl}.
	 * </p>
	 * 
	 * @param theScheduler
	 *            The scheduler
	 * @param lawComp
	 *            Reference to the Eco-law Compiler
	 */
	public ReactionManagerImpl(final ReactionsScheduler theScheduler,
			final EcolawCompiler lawComp) {
		super("reaction_manager");

		if (theScheduler == null) {
			throw new IllegalArgumentException("Invalid scheduler provided");
		}

		if (lawComp == null) {
			throw new IllegalArgumentException(
					"Invalid Eco-law compiler reference provided");
		}

		scheduler = theScheduler;
		addReactionManagerObserver(scheduler);

		compiler = lawComp;
	}

	@Override
	public final ReactionManager addEcolaw(final Ecolaw law) {
		return addEcolaw(compiler.compile(law));
	}

	@Override
	public final ReactionManager addEcolaw(final CompiledEcolaw law) {
		mutex.lock();
		try {
			laws.remove(law);
			
			if (laws.isEmpty()) {
				wakeUpTime = Long.MAX_VALUE;
			}

			// If a law is added than scheduling should be revised
			abortScheduling = true;
			schedulingEvent.signal();

			notifyLawAdded(law);

			return this;
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public final ReactionManager removeEcolaw(final Ecolaw law) {
		return removeEcolaw(compiler.compile(law));
	}

	@Override
	public final ReactionManager removeEcolaw(final CompiledEcolaw law) {
		mutex.lock();
		try {
			laws.remove(law);

			// If the scheduled law is removed than scheduling should be revised
			if (law.equals(next)) {
				abortScheduling = true;
				schedulingEvent.signal();
			}

			notifyLawRemoved(law);

			return this;
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public final CompiledEcolaw[] ecolaws() {
		return laws.toArray(new CompiledEcolaw[laws.size()]);
	}

	@Override
	public final ReactionManager addReactionManagerObserver(
			final ReactionManagerObserver obs) {
		obss.add(obs);
		return this;
	}

	@Override
	public final ReactionManager removeReactionManagerObserver(
			final ReactionManagerObserver obs) {
		obss.remove(obs);
		return this;
	}

	@Override
	protected final void behaviour(final NodeServices services) {
		final LSAspaceCore<?> space = services.getLSAspace();

		while (isRunning()) {
			mutex.lock();
			try {

				// Wait for the scheduling time or abortion due to
				// scheduling pre-conditions modification
				while ((!abortScheduling && wakeUpTime > System
						.currentTimeMillis())) {
					schedulingEvent.await(
							wakeUpTime - System.currentTimeMillis(),
							TimeUnit.MILLISECONDS);
				}

				if (!abortScheduling) {
					try {
						// If time has come applies the eco-law
						space.apply(next);
						notifyLawApplied(next, System.currentTimeMillis());
					} catch (SAPEREException e) {
						error("Error while scheduling an eco-law", e);
					}
				}

				// Find next scheduling
				findNextScheduling(space);
			} catch (InterruptedException ex) {
				spy("Interrupted");
			} finally {
				mutex.unlock();
			}
		}
	}

	/**
	 * <p>
	 * Finds the next eco-law that should be applied.
	 * </p>
	 * <p>
	 * Before trying a new match with the space, it asks the scheduler if it has
	 * any suggestion.
	 * </p>
	 * 
	 * @param space
	 *            Reference to the LSA-space
	 */
	private void findNextScheduling(final LSAspaceCore<?> space) {
		// Ask the scheduler next pre-computed law
		final Entry<MatchingEcolaw, Long> proposal = scheduler.next();
		if (proposal != null) {
			next = proposal.getKey();
			wakeUpTime = proposal.getValue();

			notifyLawEnabled(next, wakeUpTime);
			return;
		}

		// Try match and find the first law that can be scheduled
		SchedulableMatchResult best = null;
		long bestTime = Long.MAX_VALUE;
		for (CompiledEcolaw law : laws) {
			try {
				final SchedulableMatchResult[] results = scheduler.eval(space
						.match(law));
				final long now = System.currentTimeMillis();

				for (SchedulableMatchResult res : results) {
					// Notifies the scheduler of a newly discovered match
					final long sTime = scheduler.ecolawMatched(res,
							res.getSchedulingTime(now, TimeUnit.MILLISECONDS));

					// Keeps the match that should be scheduled first
					if (sTime < bestTime) {
						best = res;
						bestTime = sTime;
					}
				}
			} catch (SAPEREException e) {
				warn("Cannot complete match", e);
			}
		}

		try {
			if (best == null) {
				throw new IllegalStateException();
			}

			next = best.bind();
			notifyLawEnabled(next, wakeUpTime);
		} catch (SAPEREException e) {
			fatal("Cannot schedule next eco-law", e);
		} catch (IllegalStateException e) {
			fatal("No eco-law chosen for scheduling", e);
		}

	}

	/**
	 * <p>
	 * Notifies all observers that a new eco-law has been added.
	 * </p>
	 * 
	 * @param law
	 *            The eco-law
	 */
	private void notifyLawAdded(final CompiledEcolaw law) {
		for (ReactionManagerObserver obs : obss) {
			obs.ecolawAdded(law);
		}
	}

	/**
	 * <p>
	 * Notifies all observers that an eco-law has been removed.
	 * </p>
	 * 
	 * @param law
	 *            The eco-law
	 */
	private void notifyLawRemoved(final CompiledEcolaw law) {
		for (ReactionManagerObserver obs : obss) {
			obs.ecolawRemoved(law);
		}
	}

	/**
	 * <p>
	 * Notifies all observers that an eco-law is going to be applied at a
	 * certain local time.
	 * </p>
	 * 
	 * @param law
	 *            The eco-law
	 * @param time
	 *            When the eco-law is going to be applied (in milliseconds from
	 *            epoch)
	 */
	private void notifyLawEnabled(final MatchingEcolaw law, final long time) {
		for (ReactionManagerObserver obs : obss) {
			obs.ecolawEnabled(law, time);
		}
	}

	/**
	 * <p>
	 * Notifies all observers that an eco-law has been applied at a certain
	 * local time.
	 * </p>
	 * 
	 * @param law
	 *            The eco-law
	 * @param time
	 *            When the eco-law is going to be applied (in milliseconds from
	 *            epoch)
	 */
	private void notifyLawApplied(final MatchingEcolaw law, final long time) {
		for (ReactionManagerObserver obs : obss) {
			obs.ecolawApplied(law, time);
		}
	}

	@Override
	public final void eventOccurred(final SpaceEvent ev) {
		mutex.lock();
		try {
			checkDependencies(ev, next);
		} catch (AbortException e) {
			abortScheduling = true;
			schedulingEvent.signal();
		} finally {
			mutex.unlock();
		}
	}

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
	protected final void checkDependencies(final SpaceEvent ev,
			final MatchingEcolaw law) throws AbortException {
		scheduler.checkDependencies(ev, law);
	}
}
