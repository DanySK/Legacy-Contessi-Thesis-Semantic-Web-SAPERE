package it.apice.sapere.api.space.observation;

import it.apice.sapere.api.lsas.LSAid;

/**
 * <p>
 * A SpaceEvent registers a modification in LSA-space status that should be
 * interesting for an observer.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface SpaceEvent {

	/**
	 * <p>
	 * Retrieves a description of the event.
	 * </p>
	 * 
	 * @return Description of the event
	 */
	String getMessage();

	/**
	 * <p>
	 * Retrieves the LSA-id of all LSAs involved in the event.
	 * </p>
	 * 
	 * @return A set of LSA-id
	 */
	LSAid[] getLSAid();

	/**
	 * <p>
	 * Retrieves the type of LSA-space operation that has been run.
	 * </p>
	 * 
	 * @return What has been done
	 */
	SpaceOperationType getOperationType();
}
