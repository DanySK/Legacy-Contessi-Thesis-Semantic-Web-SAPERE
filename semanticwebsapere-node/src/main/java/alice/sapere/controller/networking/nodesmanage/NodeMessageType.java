package alice.sapere.controller.networking.nodesmanage;

import java.io.Serializable;

/**
 * <p>
 * Enumeration for the identification of messages between nodes.
 * </p>
 * 
 * @author Michele Morgagni
 * 
 */
public enum NodeMessageType implements Serializable {

	/** Diffusion message. */
	Diffuse,

	/** Information about the node. */
	NodeInfo;
}
