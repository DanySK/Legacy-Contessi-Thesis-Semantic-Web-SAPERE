package it.apice.sapere.api.space.observation;


/**
 * <p>
 * An LSA Observer is an entity capable of listening remarkable events that
 * involve a specific LSA in the LSA-space.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface LSAObserver {

	/**
	 * <p>
	 * Notifies the observer of a new event.
	 * </p>
	 * 
	 * @param ev
	 *            Details about the event
	 */
	void eventOccurred(LSAEvent ev);

}
