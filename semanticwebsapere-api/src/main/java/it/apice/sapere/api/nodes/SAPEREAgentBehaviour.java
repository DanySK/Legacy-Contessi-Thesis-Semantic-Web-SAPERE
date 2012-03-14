package it.apice.sapere.api.nodes;

import it.apice.sapere.api.space.LSASpace;
import it.apice.sapere.api.space.SpaceObservationEvent;

/**
 * <p>
 * This interface is meant to be implemented in order to model a SAPERE
 * ecosystem agent.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface SAPEREAgentBehaviour {

	/**
	 * <p>
	 * Specifies the behaviour of the agent.
	 * </p>
	 * 
	 * @param lsaSpace
	 *            The LSA-space with whom the agent can interact with the rest
	 *            of the system
	 * @param actualNode
	 *            The node on which the agent is actually located
	 */
	void behave(LSASpace lsaSpace, SAPERENode actualNode);

	/**
	 * <p>
	 * Specifies how the agent will handle events relative to required
	 * observations.
	 * </p>
	 * 
	 * @param ev
	 *            The observation event that occurred
	 */
	void handle(SpaceObservationEvent ev);

	/**
	 * <p>
	 * Specifies how to handle error situations.
	 * </p>
	 * 
	 * @param cause
	 *            The exception that caused the situation
	 * @return The action to be executed
	 */
	OnErrorAction error(Throwable cause);
}
