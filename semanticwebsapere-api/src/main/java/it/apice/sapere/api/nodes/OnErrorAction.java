package it.apice.sapere.api.nodes;

/**
 * <p>
 * This enumeration provides a set of action that the system should execute on
 * an agent which is in error state.
 * </p>
 * <ul>
 * <li><code>RESTART</code>: Restarts the agent</li>
 * <li><code>TERMINATE</code>: Terminates the agent</li>
 * </ul>
 * 
 * @author Paolo Contessi
 * 
 */
public enum OnErrorAction {

	/** Restarts the agent when error has been handled. */
	RESTART,

	/** Terminates the agent when error has been handled. */
	TERMINATE;
}
