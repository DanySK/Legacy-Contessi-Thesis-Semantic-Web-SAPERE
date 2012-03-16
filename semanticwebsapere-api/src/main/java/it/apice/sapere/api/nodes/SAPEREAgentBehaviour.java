package it.apice.sapere.api.nodes;

import it.apice.sapere.api.space.LSAspace;

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
	 * @throws SAPEREAgentTerminateException
	 *             Terminates the Agent unfairly
	 */
	void behave(LSAspace lsaSpace, SAPERENode actualNode)
			throws SAPEREAgentTerminateException;

}
