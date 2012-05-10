package it.apice.sapere.node.networking.impl;

import it.apice.sapere.node.internal.LoggerFactoryImpl;

/**
 * The class represent the entity which manage the communcations between nodes.
 * Now it uses Bluetooth, but it can manage multiple protocols
 * 
 * @author Michele Morgagni
 * 
 */
public final class NetworkManager {

	/** Table of all neighbours. */
	private final transient NeighboursTable table;

	/** Singleton instance. */
	private static NetworkManager instance;

	/**
	 * <p>
	 * Returns the instance of the NetworkManager (pattern Singleton).
	 * </p>
	 * 
	 * @return the instance of the NetworkManager
	 */
	public static NetworkManager getInstance() {
		if (instance == null) {
			instance = new NetworkManager();
		}

		return instance;
	}

	/**
	 * <p>
	 * Creates a NetworkManager.
	 * </p>
	 */
	private NetworkManager() {
		table = new NeighboursTable();
	}

	/**
	 * <p>
	 * Makes a diffusion to another node.
	 * </p>
	 * 
	 * @param neighbourId
	 *            the neighbour identifier where to do the diffuse
	 * @param message
	 *            the diffusion message
	 */
	public void doDiffuse(final String neighbourId, final NodeMessage message) {
		LoggerFactoryImpl
				.getInstance()
				.getLogger(NetworkManager.class)
				.debug(String.format(
						"Sending DIFFUSE message to %s (LSA-id: %s)",
						neighbourId, message.getOperation().getLSAid()));
		table.sendMessage(neighbourId, message);
	}

	/**
	 * <p>
	 * Invoked for register a neighbour in the NeighbourTable.
	 * </p>
	 * 
	 * @param neighbour
	 *            the neighbour to be registered
	 * @return the id assigned to the neighbour
	 */
	public String registerNeighbour(final Neighbour neighbour) {
		LoggerFactoryImpl.getInstance().getLogger(NetworkManager.class)
				.info("Registering neighbour: " + neighbour.getId());
		return table.addNeighbour(neighbour);
	}

}
