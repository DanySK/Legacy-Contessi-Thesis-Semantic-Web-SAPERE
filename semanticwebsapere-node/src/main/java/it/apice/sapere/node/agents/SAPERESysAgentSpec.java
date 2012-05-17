package it.apice.sapere.node.agents;

import it.apice.sapere.node.LogUtils;

/**
 * <p>
 * This interface represents the specification of a SAPEREAgent behaviour.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface SAPERESysAgentSpec {

	/**
	 * <p>
	 * Agent main-cycle.
	 * </p>
	 * 
	 * @param services
	 *            Reference to all available node services
	 * @param out
	 *            Reference to logging facility (should be used in place of
	 *            System.out and System.err)
	 * @param me
	 *            Reference to the agent itself
	 * @throws Exception
	 *             Each uncaught exception occurred in the agent main-cycle.
	 *             Causes agent termination
	 */
	void behaviour(NodeServices services, LogUtils out, SAPEREAgent me)
			throws Exception;
}
