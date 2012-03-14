package it.apice.sapere.api.nodes;


import java.net.URI;

/**
 * <p>
 * This interface models a SAPERE Agent running on a specific node.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface SAPEREAgent {

	/**
	 * <p>
	 * Retrieves the identifier of this agent.
	 * </p>
	 * 
	 * @return The URI that identifies the agent
	 */
	URI getId();

	/**
	 * <p>
	 * Retrieves the node on which the agent is located.
	 * </p>
	 * 
	 * @return The location of the agent
	 */
	SAPERENode getNode();

	/**
	 * Start the agent.
	 */
	void spawn();

	/**
	 * <p>
	 * Checks if the agent is running.
	 * </p>
	 * 
	 * @return True if is running, false otherwise
	 */
	boolean isRunning();
}
