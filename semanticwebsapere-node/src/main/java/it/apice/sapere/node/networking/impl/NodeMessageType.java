package it.apice.sapere.node.networking.impl;

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
	DIFFUSE,

	/** Information about the node. */
	NODE_INFO;
}
