package it.apice.sapere.node.networking.manager;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.node.networking.impl.NodeMessage;

import java.io.File;
import java.net.InetSocketAddress;

/**
 * <p>
 * Basic interface that models a service capable of handling relocation of LSAs
 * from an LSA-space to another.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface NetworkManager {

	/**
	 * <p>
	 * Sends a message to destination, in order to diffuse an LSA.
	 * </p>
	 * 
	 * @param to
	 *            Id of the node to which deliver the message (friendly name,
	 *            neighbour LSA-id)
	 * @param msg
	 *            The message
	 */
	void diffuse(Object to, NodeMessage msg);

	/**
	 * <p>
	 * Registers a new destination.
	 * </p>
	 * 
	 * @param id
	 *            Identifier of the destination
	 * @param addr
	 *            Address (IP:Port) of the destination
	 * @return False if this registration has overridden a previous one, True
	 *         otherwise
	 * @throws SAPEREException
	 *             Cannot inject information
	 */
	boolean register(String id, InetSocketAddress addr) throws SAPEREException;

	/**
	 * <p>
	 * Loads all destinations from a configuration (properties) file.
	 * </p>
	 * 
	 * @param config
	 *            Configuration file
	 */
	void loadTable(File config);

	/**
	 * <p>
	 * Retrieves the number of registered neighbours.
	 * </p>
	 * 
	 * @return Number of neighbours
	 */
	int countNeighbours();
}
