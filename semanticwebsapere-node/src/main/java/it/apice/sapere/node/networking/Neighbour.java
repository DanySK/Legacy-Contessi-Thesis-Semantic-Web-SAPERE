package it.apice.sapere.node.networking;


/**
 * <p>
 * Interface implemented by all neighbours (actually only BT neighbour).
 * </p>
 * 
 * @author Michele Morgagni
 * 
 */
public interface Neighbour {

	/**
	 * <p>
	 * Retrieves the identifier of the neighbour (the BT Address).
	 * </p>
	 * 
	 * @return BT neighbour id
	 */
	String getId();

	/**
	 * <p>
	 * Invoked for sending a message to the neighbour.
	 * </p>
	 * 
	 * @param message
	 *            The message to be sent
	 */
	void send(NodeMessage message);

}
