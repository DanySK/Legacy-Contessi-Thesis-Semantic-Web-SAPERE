package it.apice.sapere.management;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.node.agents.SAPEREAgent;

/**
 * <p>
 * This interface models the entity responsible of handling eco-laws.
 * </p>
 * <p>
 * After an initialization phase in which eco-laws can be added and/or removed
 * it provides scheduling logic.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface ReactionManager extends SAPEREAgent {

	/**
	 * <p>
	 * Stores an eco-law in order to be scheduled.
	 * </p>
	 * 
	 * @param law
	 *            The eco-law
	 * @return The {@link ReactionManager} itself
	 */
	ReactionManager addEcolaw(Ecolaw law);

	/**
	 * <p>
	 * Dismisses an eco-law in order to stop its scheduling.
	 * </p>
	 * 
	 * @param law
	 *            The eco-law
	 * @return The {@link ReactionManager} itself
	 */
	ReactionManager removeEcolaw(Ecolaw law);

	/**
	 * <p>
	 * Retrieves a list of all the eco-laws the {@link ReactionManager} is aware
	 * of.
	 * </p>
	 * 
	 * @return All the eco-laws that can be scheduled
	 */
	Ecolaw[] ecolaws();

	/**
	 * <p>
	 * Registers a new {@link ReactionManagerObserver}.
	 * </p>
	 * 
	 * @param obs The observer
	 */
	void addReactionManagerObserver(ReactionManagerObserver obs);

	/**
	 * <p>
	 * Removes the registration of a {@link ReactionManagerObserver}.
	 * </p>
	 * 
	 * @param obs The observer
	 */
	void removeReactionManagerObserver(ReactionManagerObserver obs);
}
