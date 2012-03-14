package it.apice.sapere.api.nodes;

import java.net.URI;

/**
 * <p>
 * This interface models a generic SAPERE node, part of an ecosystem.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface SAPERENode {

	/**
	 * <p>
	 * Retrieves the identifier of the node as an URI.
	 * </p>
	 * 
	 * @return The URI which identifies this Node
	 */
	URI getNodeId();

	/**
	 * <p>
	 * To be completed!
	 * </p>
	 * 
	 * @return Reference to LSA-space
	 */
	Object getLocalLSASpace();
}
