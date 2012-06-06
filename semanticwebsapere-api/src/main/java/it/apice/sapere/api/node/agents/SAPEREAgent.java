package it.apice.sapere.api.node.agents;

import it.apice.sapere.api.node.agents.networking.Subscriber;

import java.net.URI;

/**
 * <p>
 * This interface models a generic agent running on the SAPERE middleware.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface SAPEREAgent extends Subscriber {

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
	 * Retrieves the URI that globally identifies the agent.
	 * </p>
	 * 
	 * @return The Agent's URI
	 */
	URI getAgentURI();

	/**
	 * <p>
	 * Getter for the agent identifier.
	 * </p>
	 * 
	 * @return the agent identifier
	 */
	String getLocalAgentId();

	/**
	 * <p>
	 * Check if someone requested the termination.
	 * </p>
	 * 
	 * @return True if no termination requested, false otherwise
	 */
	boolean isRunning();
}
