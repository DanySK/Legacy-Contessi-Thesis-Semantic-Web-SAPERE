package it.apice.sapere.node.networking;


import java.util.HashMap;


/**
 * <p>
 * The Table where the neighbourIds are mapped on the neighbours.
 * </p>
 * 
 * @author Michele Morgagni
 * 
 */
public final class NeighboursTable {

	/** Current index = Entry count. */
	private int currentId;

	/** Table's inner map. */
	private HashMap<String, Neighbour> map;

	/**
	 * <p>
	 * Creates the NeighboursTable.
	 * </p>
	 */
	public NeighboursTable() {
		currentId = 0;
		map = new HashMap<String, Neighbour>();
	}

	/**
	 * <p>
	 * Adds a neighbour to the table.
	 * </p>
	 * 
	 * @param neighbour
	 *            the neighbour to add
	 * @return the id assigned to the neighbour just inserted
	 */
	public synchronized String addNeighbour(final Neighbour neighbour) {
		String newId = "neighbour" + currentId++;
		map.put(newId, neighbour);
		return newId;

	}

	/**
	 * <p>
	 * Invoked for sending a message to a neighbour in the table.
	 * </p>
	 * 
	 * @param neighbourId
	 *            the id of the neighbour
	 * @param message
	 *            the message to send
	 * @return true if success
	 */
	public boolean sendMessage(final String neighbourId,
			final NodeMessage message) {
		Neighbour neighbour = map.get(neighbourId);
		if (neighbour == null) {
			return false;
		}
		neighbour.send(message);
		return true;

	}

}
