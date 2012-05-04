package it.apice.sapere.api.space.observation;

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
	AGENT_INJECT,

	/** The update operation. */
	AGENT_UPDATE,

	/** The remove operation. */
	AGENT_REMOVE,

	/** The read operation. */
	AGENT_READ,

	/** A generic action perpetrated by an agent. */
	AGENT_ACTION,

	/** A generic action perpetrated by the system. */
	SYSTEM_ACTION,

	/** The system found an LSA to be diffused. */
	SYSTEM_DIFFUSE;
}
