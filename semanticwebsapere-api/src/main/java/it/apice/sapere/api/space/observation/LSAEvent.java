package it.apice.sapere.api.space.observation;

import it.apice.sapere.api.lsas.LSA;

/**
 * <p>
 * An LSAEvent registers a modification of an LSA inside the LSA-space.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface LSAEvent {

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
	 * Retrieves the actual LSA status.
	 * </p>
	 * 
	 * @return The observed LSA
	 */
	LSA getLSA();

	/**
	 * <p>
	 * Retrieves the type of LSA-space operation that has been run on the
	 * observed LSA.
	 * </p>
	 * 
	 * @return What has been done
	 */
	SpaceOperationType getOperationType();
}
