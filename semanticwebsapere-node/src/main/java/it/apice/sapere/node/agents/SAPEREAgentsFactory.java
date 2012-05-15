package it.apice.sapere.node.agents;

import it.apice.sapere.api.SAPEREException;

/**
 * <p>
 * This interface models a service capable of creating (local) SAPEREAgents on a
 * SAPERE-node.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface SAPEREAgentsFactory {

	/**
	 * <p>
	 * Creates a new {@link SAPEREAgent} and binds it to this SAPERE-node or
	 * retrieves the homonyomus one.
	 * </p>
	 * <p>
	 * If the agent has been already created then the provided behaviour
	 * specification is ignored. In this case the
	 * {@link SAPEREAgentsFactory#getAgent(String)} method should be preferred.
	 * </p>
	 * 
	 * @param agentLocalId
	 *            The local name of the agent (a URI fragment)
	 * @param spec
	 *            The agent behaviour specification
	 * @return The {@link SAPEREAgent}, linked to the node
	 * @throws SAPEREException
	 *             Invalid agentId
	 */
	SAPEREAgent createAgent(String agentLocalId, final SAPEREAgentSpec spec)
			throws SAPEREException;

	/**
	 * <p>
	 * Retrieves a previously created agent on this SAPERE-node.
	 * </p>
	 * 
	 * @param agentLocalId
	 *            The local name of the agent (a URI fragment)
	 * @return The {@link SAPEREAgent}
	 * @throws SAPEREException
	 *             Invalid agentId
	 */
	SAPEREAgent getAgent(String agentLocalId) throws SAPEREException;

	/**
	 * <p>
	 * Creates an agent like
	 * {@link SAPEREAgentsFactory#createAgent(String, SAPEREAgentSpec)}, but
	 * with FULL ACCESS TO NODE SERVICES.
	 * </p>
	 * <p>
	 * THIS METHOD SHOULD BE HIDDEN WHEN LSA MODEL SUPPORT IS PROVIDED.
	 * </p>
	 * 
	 * @param agentLocalId
	 *            The local name of the agent (a URI fragment)
	 * @param spec
	 *            The agent behaviour specification
	 * @return The {@link SAPEREAgent}, linked to the node
	 * @throws SAPEREException
	 *             Invalid agentId
	 */
	SAPEREAgent createSysAgent(String agentLocalId,
			final SAPERESysAgentSpec spec) throws SAPEREException;

	/**
	 * <p>
	 * Kills all {@link SAPEREAgent}s.
	 * </p>
	 */
	void killAll();
}
