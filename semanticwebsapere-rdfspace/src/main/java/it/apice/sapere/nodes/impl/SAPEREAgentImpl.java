package it.apice.sapere.nodes.impl;

import it.apice.sapere.api.nodes.SAPEREAgent;
import it.apice.sapere.api.nodes.SAPEREAgentBehaviour;
import it.apice.sapere.api.nodes.SAPEREAgentTerminateException;
import it.apice.sapere.api.nodes.SAPERENode;

import java.net.URI;

/**
 * <p>
 * Implementation of SAPEREAgent.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see SAPEREAgent
 */
public class SAPEREAgentImpl implements SAPEREAgent, Runnable {

	/** The identifier of the agent. */
	private final transient URI id;

	/** Node where the agent has been created. */
	private final transient SAPERENode node;

	/** Agent behaviour specification. */
	private final transient SAPEREAgentBehaviour behaviour;

	/** Registers if agent has been spawn. */
	private volatile boolean running;

	/**
	 * <p>
	 * Builds a new SAPEREAgentImpl.
	 * </p>
	 * 
	 * @param aUniqueId
	 *            Identifier of the agent
	 * @param thisNode
	 *            The actual node, where the agent has been created
	 * @param behav
	 *            Agent behaviour specification
	 */
	public SAPEREAgentImpl(final URI aUniqueId, final SAPERENode thisNode,
			final SAPEREAgentBehaviour behav) {
		if (aUniqueId == null) {
			throw new IllegalArgumentException("Invalid id");
		}

		if (thisNode == null) {
			throw new IllegalArgumentException("Invalid node");
		}

		if (behav == null) {
			throw new IllegalArgumentException("Invalid agent behaviour");
		}

		id = aUniqueId;
		node = thisNode;
		behaviour = behav;
	}

	@Override
	public final URI getId() {
		return id;
	}

	@Override
	public final SAPERENode getNode() {
		return node;
	}

	@Override
	public final void run() {
		running = true;
		try {
			behaviour.behave(node.getLocalLSASpace(), node);
		} catch (SAPEREAgentTerminateException ex) {
			System.err.println(ex.getMessage());
		}

		running = false;
	}

	@Override
	public final void spawn() {
		if (running) {
			throw new IllegalStateException("Agent (" + id + ") already spawn");
		}

		new Thread(this).start();
	}

	@Override
	public final boolean isRunning() {
		return running;
	}

}
