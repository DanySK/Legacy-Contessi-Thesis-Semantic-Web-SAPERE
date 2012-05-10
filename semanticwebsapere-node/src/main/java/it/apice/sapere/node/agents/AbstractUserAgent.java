package it.apice.sapere.node.agents;

import it.apice.sapere.node.internal.NodeServicesImpl;

/**
 * <p>
 * This class provides a basic realization of a SAPERE User Agent, which is an
 * agent concerned with application logic tasks.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractUserAgent extends AbstractSAPEREAgent {

	/**
	 * <p>
	 * Builds a new {@link AbstractUserAgent}.
	 * </p>
	 * 
	 * @param agentId
	 *            Local ID of the agent
	 */
	public AbstractUserAgent(final String agentId) {
		super(agentId);
	}

	@Override
	protected final void execute() {
		behaviour(NodeServicesImpl.getUserInstance());
	}

	/**
	 * <p>
	 * Specifies the behaviour of the agent.
	 * </p>
	 * 
	 * @param services
	 *            Reference to all available services
	 */
	protected abstract void behaviour(final UserNodeServices services);

}
