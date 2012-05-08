package it.apice.sapere.management;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.space.core.CompiledEcolaw;
import it.apice.sapere.api.space.core.EcolawCompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.match.MatchingEcolaw;
import it.apice.sapere.api.space.observation.SpaceEvent;
import it.apice.sapere.api.space.observation.SpaceObserver;
import it.apice.sapere.node.agents.AbstractSAPEREAgent;

import java.util.LinkedList;
import java.util.List;
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
public abstract class AbstractReactionManagerImpl extends AbstractSAPEREAgent
		implements ReactionManager, SpaceObserver {

	/** Reference to the LSA-space. */
	private final transient LSAspaceCore<?> space;

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
	private transient long wakeUpTime;

	/** Thread-safety mutex. */
	private final transient Lock mutex = new ReentrantLock();

	/** Condition variable on scheduling events. */
	private final transient Condition schedulingEvent = mutex.newCondition();

	/** Flag that indicate that programmed eco-law should be verified. */
	private transient boolean abortScheduling;

	/**
	 * <p>
	 * Builds a new {@link AbstractReactionManagerImpl}.
	 * </p>
	 * 
	 * @param lsaSpace
	 *            Reference to the LSA-space
	 * @param lawComp
	 *            Reference to the Eco-law Compiler
	 */
	public AbstractReactionManagerImpl(final LSAspaceCore<?> lsaSpace,
			final EcolawCompiler lawComp) {
		super("reaction_manager");

		if (lsaSpace == null) {
			throw new IllegalArgumentException(
					"Invalid LSA-space reference provided");
		}

		if (lawComp == null) {
			throw new IllegalArgumentException(
					"Invalid Eco-law compiler reference provided");
		}

		space = lsaSpace;
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
			if (law.getLabel().equals(next.getLabel())) {
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
	protected final void execute() {
		while (isRunning()) {
			mutex.lock();
			try {
				while (!abortScheduling
						&& wakeUpTime > System.currentTimeMillis()) {
					schedulingEvent.await(System.currentTimeMillis(),
							TimeUnit.MILLISECONDS);
				}

				if (!abortScheduling) {
					try {
						space.apply(next);
					} catch (SAPEREException e) {
						error("Error while scheduling an eco-law", e);
					}
				}

				scheduleNext();

				notifyLawEnabled(null, 0);
			} catch (InterruptedException ex) {
				spy("Interrupted");
			} finally {
				mutex.unlock();
			}
		}
	}

	/**
	 * <p>
	 * Tries a match with each eco-law and schedules the first occurrence of all
	 * as next law to be applied.
	 * </p>
	 */
	private void scheduleNext() {

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
	private void notifyLawEnabled(final CompiledEcolaw law, final long time) {
		for (ReactionManagerObserver obs : obss) {
			obs.ecolawEnabled(law, time);
		}
	}

	@Override
	public final void eventOccurred(final SpaceEvent ev) {
		mutex.lock();
		try {
			checkDependency(ev, next);
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
	protected abstract void checkDependency(SpaceEvent ev, MatchingEcolaw law)
			throws AbortException;
}
