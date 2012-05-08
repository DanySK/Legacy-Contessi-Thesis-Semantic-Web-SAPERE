package it.apice.sapere.node.agents;

import it.apice.sapere.node.internal.NodeServices;
import it.apice.sapere.node.internal.NodeServicesImpl;

/**
 * <p>
 * This class provides a basic realization of a SAPERE System Agent, which is an
 * agent concerned with system administration task. That's why this agent should
 * not be used for application specific tasks.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class SystemAgent extends AbstractSAPEREAgent {

	/**
	 * <p>
	 * Builds a new {@link SystemAgent}.
	 * </p>
	 * 
	 * @param agentId
	 *            Local ID of the agent
	 */
	public SystemAgent(final String agentId) {
		super(agentId);
	}

	@Override
	protected final void execute() {
		behaviour(NodeServicesImpl.getInstance());
	}

	/**
	 * <p>
	 * Specifies the behaviour of the agent.
	 * </p>
	 * 
	 * @param services
	 *            Reference to all available services
	 */
	protected abstract void behaviour(final NodeServices services);

}
