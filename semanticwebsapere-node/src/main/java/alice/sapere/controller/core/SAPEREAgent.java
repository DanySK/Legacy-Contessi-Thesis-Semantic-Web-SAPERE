package alice.sapere.controller.core;

/**
 * <p>
 * This interface models a generic agent running on the SAPERE middleware.
 * </p>
 * 
 * @author Paolo Contessi
 *
 */
public interface SAPEREAgent {

	/**
	 * <p>
	 * Starts the agent.
	 * </p>
	 */
	void spawn();
	
	/**
	 * <p>
	 * Requests agent termination.
	 * </p>
	 */
	void kill();
	
	/**
	 * <p>
	 * Check if someone requested the termination.
	 * </p>
	 * 
	 * @return True if no termination requested, false otherwise
	 */
	boolean isRunning();
}
