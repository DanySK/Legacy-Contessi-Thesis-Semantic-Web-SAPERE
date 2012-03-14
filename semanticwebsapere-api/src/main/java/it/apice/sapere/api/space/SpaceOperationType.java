package it.apice.sapere.api.space;

/**
 * <p>
 * Defines the type of operation that has been observed (on the LSA-space).
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public enum SpaceOperationType {

	/** The inject operation. */
	INJECT,

	/** The update operation. */
	UPDATE,

	/** The remove operation. */
	REMOVE,

	/** The read operation. */
	READ;
}
