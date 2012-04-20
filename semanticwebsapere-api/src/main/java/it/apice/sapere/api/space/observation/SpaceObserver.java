package it.apice.sapere.api.space.observation;

/**
 * <p>
 * An LSA-space Observer is an entity capable of listening remarkable events
 * occurred inside the LSA-space.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface SpaceObserver {

	/**
	 * <p>
	 * Notifies the observer of a new event.
	 * </p>
	 * 
	 * @param ev
	 *            Details about the event
	 */
	void eventOccurred(SpaceEvent ev);

}
