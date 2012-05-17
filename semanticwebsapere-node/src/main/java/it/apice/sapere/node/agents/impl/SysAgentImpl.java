package it.apice.sapere.node.agents.impl;

import it.apice.sapere.node.agents.LSAspaceAccessPolicy;
import it.apice.sapere.node.agents.SAPERESysAgentSpec;
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
public class SysAgentImpl extends AbstractSAPEREAgentImpl {

	/** Specification of agent behaviour. */
	private final transient SAPERESysAgentSpec spec;

	/** LSA-space Access Control entity. */
	private final transient LSAspaceAccessPolicy sap;

	/**
	 * <p>
	 * Builds a new {@link SysAgentImpl}.
	 * </p>
	 * 
	 * @param agentId
	 *            Local ID of the agent
	 * @param aSpec
	 *            Specification of agent behaviour
	 * @param sysRuler
	 *            LSA-space Access Control Entity
	 */
	public SysAgentImpl(final String agentId, final SAPERESysAgentSpec aSpec,
			final LSAspaceAccessPolicy sysRuler) {
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
		sap = sysRuler;
		sap.setRequestor(this);
	}

	@Override
	protected final void execute() throws Exception {
		spec.behaviour(NodeServicesImpl.getInstance(),
				LoggerFactoryImpl.getInstance().getLogger(this), this);
	}
}
