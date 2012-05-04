package it.apice.sapere.node.agents;

/**
 * <p>
 * Possible interface for a factory capable of binding an agent to a SAPEREnode.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface SAPEREAgentsFactory {

	/**
	 * <p>
	 * Possible factory method.
	 * </p>
	 * 
	 * @param agentClass
	 *            The class of the agent that should be created
	 * @return An instance of the agent
	 */
	SAPEREAgent createAgent(Class<? extends SAPEREAgent> agentClass);
}
