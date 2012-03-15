package it.apice.sapere.api.nodes;

import it.apice.sapere.api.SAPEREFactory;
import it.apice.sapere.api.space.LSAspace;

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
	 * Retrieves a reference to the (local) LSA-space.
	 * </p>
	 * 
	 * @return Reference to LSA-space
	 */
	LSAspace getLocalLSASpace();

	/**
	 * <p>
	 * Retrieves a reference to the model factory.
	 * </p>
	 * 
	 * @return Reference to a SAPEREFactory
	 */
	SAPEREFactory getModelFactory();
}
