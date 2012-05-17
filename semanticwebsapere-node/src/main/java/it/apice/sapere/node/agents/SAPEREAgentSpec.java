package it.apice.sapere.node.agents;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.node.LogUtils;

/**
 * <p>
 * This interface represents the specification of a SAPEREAgent behaviour.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface SAPEREAgentSpec {

	/**
	 * <p>
	 * Agent main-cycle.
	 * </p>
	 * 
	 * @param factory
	 *            Reference to LSA Factory
	 * @param space
	 *            LSA-space Access Point
	 * @param out
	 *            Reference to logging facility (should be used in place of
	 *            System.out and System.err)
	 * @param me
	 *            Reference to the agent itself
	 * @throws Exception
	 *             Each uncaught exception occurred in the agent main-cycle.
	 *             Causes agent termination
	 */
	void behaviour(LSAFactory factory, LSAspace space, LogUtils out,
			SAPEREAgent me) throws Exception;
}
