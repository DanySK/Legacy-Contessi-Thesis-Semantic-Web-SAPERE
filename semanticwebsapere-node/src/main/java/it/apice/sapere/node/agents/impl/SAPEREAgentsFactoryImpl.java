package it.apice.sapere.node.agents.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.node.agents.SAPEREAgent;
import it.apice.sapere.node.agents.SAPEREAgentSpec;
import it.apice.sapere.node.agents.SAPEREAgentsFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class implements the {@link SAPEREAgentsFactory} interface.
 * </p>
 * 
 * @author Paolo Cntessi
 * 
 */
public class SAPEREAgentsFactoryImpl implements SAPEREAgentsFactory {

	/** Map of all local SAPEREAgents by name. */
	private final transient Map<String, SAPEREAgent> localAgents = 
			new HashMap<String, SAPEREAgent>();

	/** Singleton instance. */
	private static final transient SAPEREAgentsFactoryImpl INSTANCE = 
			new SAPEREAgentsFactoryImpl();

	@Override
	public final SAPEREAgent createAgent(final String agentLocalId,
			final SAPEREAgentSpec spec) throws SAPEREException {
		SAPEREAgent agent = getAgent(agentLocalId);
		if (agent == null) {
			agent = new UserAgent(agentLocalId, spec);
			localAgents.put(agentLocalId, agent);
		}

		return agent;
	}

	@Override
	public final SAPEREAgent getAgent(final String agentLocalId)
			throws SAPEREException {
		if (isLocalIdInvalid(agentLocalId)) {
			throw new SAPEREException("Invalid agent local ID provided");
		}

		return localAgents.get(agentLocalId);
	}

	/**
	 * <p>
	 * Checks if the provided ID is valid.
	 * </p>
	 * 
	 * @param agentLocalId
	 *            The ID to be verified
	 * @return True if invalid, false otherwise
	 */
	private boolean isLocalIdInvalid(final String agentLocalId) {
		return agentLocalId == null || agentLocalId.equals("")
				|| agentLocalId.contains(" ");
	}

	/**
	 * <p>
	 * Singleton method.
	 * </p>
	 *
	 * @return The singleton instance
	 */
	public static SAPEREAgentsFactoryImpl getInstance() {
		return INSTANCE;
	}
}
