package it.apice.sapere.node.agents.impl;

import it.apice.sapere.node.agents.LSAspaceAccessController;
import it.apice.sapere.node.agents.SAPEREAgentSpec;
import it.apice.sapere.node.internal.LoggerFactoryImpl;
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
public class UserAgent extends AbstractSAPEREAgent {

	/** Specification of agent behaviour. */
	private final transient SAPEREAgentSpec spec;

	/** LSA-space Access Control entity. */
	private final transient LSAspaceAccessController ruler;

	/**
	 * <p>
	 * Builds a new {@link UserAgent}.
	 * </p>
	 * 
	 * @param agentId
	 *            Local ID of the agent
	 * @param aSpec
	 *            Specification of agent behaviour
	 * @param sysRuler
	 *            LSA-space Access Control Entity
	 */
	public UserAgent(final String agentId, final SAPEREAgentSpec aSpec,
			final LSAspaceAccessController sysRuler) {
		super(agentId);

		if (aSpec == null) {
			throw new IllegalArgumentException(
					"Invalid agent behaviour specification provided");
		}

		if (sysRuler == null) {
			throw new IllegalArgumentException(
					"Invalid LSA-space Access Control Entity provided");
		}

		spec = aSpec;
		ruler = sysRuler;
	}

	@Override
	protected final void execute() throws Exception {
		spec.behaviour(NodeServicesImpl.getInstance().getLSAFactory(), ruler,
				LoggerFactoryImpl.getInstance().getLogger(this));
	}

}
