package it.apice.sapere.management;

import it.apice.sapere.api.space.core.CompiledEcolaw;
import it.apice.sapere.api.space.match.MatchingEcolaw;

/**
 * <p>
 * This is an interface for an entity which wants to observe the
 * {@link ReactionManager} behaviour.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface ReactionManagerObserver {

	/**
	 * <p>
	 * Notifies all observers that a new eco-law has been added.
	 * </p>
	 * 
	 * @param law
	 *            The eco-law
	 */
	void ecolawAdded(CompiledEcolaw law);

	/**
	 * <p>
	 * Notifies all observers that an eco-law has been removed.
	 * </p>
	 * 
	 * @param law
	 *            The eco-law
	 */
	void ecolawRemoved(CompiledEcolaw law);

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
	void ecolawEnabled(MatchingEcolaw law, long time);

	/**
	 * <p>
	 * Notifies all observers that an eco-law has been applied at a
	 * certain local time.
	 * </p>
	 * 
	 * @param law
	 *            The eco-law
	 * @param time
	 *            When the eco-law is going to be applied (in milliseconds from
	 *            epoch)
	 */
	void ecolawApplied(MatchingEcolaw law, long time);

}
