package it.apice.sapere.node.agents.impl;

import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.node.agents.LSAspaceAccessPolicy;
import it.apice.sapere.api.node.agents.SAPEREAgent;
import it.apice.sapere.api.node.agents.SAPEREAgentSpec;
import it.apice.sapere.api.node.agents.SAPEREAgentsFactory;
import it.apice.sapere.api.node.agents.SAPERESysAgentSpec;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.node.agents.AbstractAccessPolicy;
import it.apice.sapere.node.internal.LoggerFactoryImpl;
import it.apice.sapere.node.internal.NodeServicesImpl;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class implements the {@link SAPEREAgentsFactory} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class SAPEREAgentsFactoryImpl implements SAPEREAgentsFactory {

	/** Map of all local SAPEREAgents by name. */
	private final transient Map<String, SAPEREAgent> localAgents = 
			new HashMap<String, SAPEREAgent>();

	/** Singleton instance. */
	private static final transient SAPEREAgentsFactoryImpl INSTANCE = 
			new SAPEREAgentsFactoryImpl();

	/** The access policy for User Agents. */
	private static transient Class<? extends AbstractAccessPolicy> policy = 
			DefaultAccessPolicy.class;

	@Override
	public final SAPEREAgent createAgent(final String agentLocalId,
			final SAPEREAgentSpec spec) throws SAPEREException {
		SAPEREAgent agent = getAgent(agentLocalId);
		if (agent == null) {
			agent = new UserAgentImpl(agentLocalId, spec, 
					obtainPolicyEnforcer());
			localAgents.put(agentLocalId, agent);
		}

		return agent;
	}

	/**
	 * <p>
	 * Creates a new Policy enforcer.
	 * </p>
	 * 
	 * @return The instantiated policy
	 * @throws SAPEREException
	 *             Unable to instantiate
	 */
	private LSAspaceAccessPolicy obtainPolicyEnforcer() throws SAPEREException {
		try {
			return getConstructor().newInstance(
					NodeServicesImpl.getInstance().getLSAspace(),
					NodeServicesImpl.getInstance().getLSACompiler(),
					NodeServicesImpl.getInstance().getLSAParser());
		} catch (Exception e) {
			throw new SAPEREException("Cannot instantiate policy");
		}
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
	 * Defines an LSA-space Access Policy to be applied to each
	 * {@link UserAgentImpl}.
	 * </p>
	 * <p>
	 * Default policy = ALLOW ALL.
	 * </p>
	 * 
	 * @param aPolicy
	 *            The policy class to be used
	 */
	public static void defineAccessPolicyClass(
			final Class<? extends AbstractAccessPolicy> aPolicy) {
		if (getConstructor() == null) {
			throw new IllegalArgumentException(
					"Invalid policy provided. Unable to build it.");
		}

		policy = aPolicy;
	}

	/**
	 * <p>
	 * Looks for the needed constructor.
	 * </p>
	 * 
	 * @return The constructor
	 */
	private static Constructor<? extends AbstractAccessPolicy> 
			getConstructor() {
		try {
			return policy.getConstructor(LSAspaceCore.class, LSACompiler.class,
					LSAParser.class);
		} catch (Exception e) {
			LoggerFactoryImpl.getInstance()
					.getLogger(SAPEREAgentsFactoryImpl.class)
					.warn("Cannot create a policy", e);
		}

		return null;
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

	@Override
	public final SAPEREAgent createSysAgent(final String agentLocalId,
			final SAPERESysAgentSpec spec) throws SAPEREException {
		SAPEREAgent agent = getAgent(agentLocalId);
		if (agent == null) {
			agent = new SysAgentImpl(agentLocalId, spec, 
					obtainPolicyEnforcer());
			localAgents.put(agentLocalId, agent);
		}

		return agent;
	}

	@Override
	public final void killAll() {
		for (SAPEREAgent agent : localAgents.values()) {
			agent.kill();
		}
	}
}
