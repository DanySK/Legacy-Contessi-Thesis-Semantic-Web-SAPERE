package alice.sapere.controller.networking.nodesmanage;

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
	private HashMap<String, INeighbour> map;

	/** Singleton instance. */
	private static NeighboursTable instance;

	/**
	 * <p>
	 * Returns the instance of the NeighboursTable (pattern Singleton).
	 * </p>
	 * 
	 * @return the instance of the NeighboursTable
	 */
	public static NeighboursTable getInstance() {
		if (instance == null) {
			instance = new NeighboursTable();
		}

		return instance;
	}

	/**
	 * <p>
	 * Creates the NeighboursTable.
	 * </p>
	 */
	private NeighboursTable() {
		currentId = 0;
		map = new HashMap<String, INeighbour>();
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
	public synchronized String addNeighbour(final INeighbour neighbour) {
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
		INeighbour neighbour = map.get(neighbourId);
		if (neighbour == null) {
			return false;
		}
		neighbour.send(message);
		return true;

	}

}
