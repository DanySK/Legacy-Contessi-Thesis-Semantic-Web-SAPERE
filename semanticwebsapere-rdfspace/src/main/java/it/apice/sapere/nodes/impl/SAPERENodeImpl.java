package it.apice.sapere.nodes.impl;

import it.apice.sapere.api.ExtSAPEREFactory;
import it.apice.sapere.api.SAPEREFactory;
import it.apice.sapere.api.impl.SAPEREFactoryImpl;
import it.apice.sapere.api.nodes.SAPERENode;
import it.apice.sapere.api.space.ExtLSAspace;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.space.impl.LSAspaceImpl;

import java.net.URI;

/**
 * <p>
 * Implementation of SAPERENode.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see SAPERENode
 */
public class SAPERENodeImpl implements SAPERENode {

	/** The node-id. */
	private final transient URI id;

	/** The (local) LSA-space. */
	private final transient ExtLSAspace space;

	/** The factory. */
	private final transient ExtSAPEREFactory factory;

	/**
	 * <p>
	 * Builds a new SAPERENodeImpl.
	 * </p>
	 * 
	 * @param nodeId
	 *            The id of the node to be created (should be unique)
	 */
	public SAPERENodeImpl(final URI nodeId) {
		if (nodeId == null) {
			throw new IllegalArgumentException("Invalid node-id");
		}

		id = nodeId;
		factory = new SAPEREFactoryImpl(this);
		space = new LSAspaceImpl(factory);
	}

	@Override
	public final URI getNodeId() {
		return id;
	}

	@Override
	public final LSAspace getLocalLSASpace() {
		return space;
	}

	@Override
	public final SAPEREFactory getModelFactory() {
		return factory;
	}

}
