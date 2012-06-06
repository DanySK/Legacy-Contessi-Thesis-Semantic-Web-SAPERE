package it.apice.sapere.api.node.agents;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.node.logging.LogUtils;
import it.apice.sapere.api.space.LSAspace;

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
	 * @param parser
	 *            Reference to LSA Parser
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
	void behaviour(LSAFactory factory, LSAParser parser, LSAspace space,
			LogUtils out, SAPEREAgent me) throws Exception;
}
