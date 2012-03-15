package it.apice.sapere.api.space;

import it.apice.sapere.api.lsas.LSAid;


/**
 * <p>
 * This interface models an observation event occurred in the LSA-space.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface SpaceObservationEvent {

	/**
	 * <p>
	 * Retrieves the affected LSA's LSA-id.
	 * </p>
	 * 
	 * @return The LSA-id of the LSA which has been involved in this event
	 */
	LSAid getLSAId();

	/**
	 * <p>
	 * Retrieves the type of operation that has raised this event.
	 * </p>
	 * 
	 * @return The type of operation observed
	 */
	SpaceOperationType getExecutedOperation();
}
