package alice.sapere.controller.networking;

/**
 * <p>
 * Enumeration of possible {@link SapereOperationType} that can be communicated
 * over the network.
 * </p>
 */
public enum SapereOperationType {

	/** Inject primitive. */
	INJECT,

	/** Observe primitive. */
	OBSERVE,

	/** Remove primitive. */
	REMOVE,

	/** Update primitive. */
	UPDATE; // ,

	// /** Inject primitive, followed by Observe one.*/
	// INJECT_OBSERVE;
}
