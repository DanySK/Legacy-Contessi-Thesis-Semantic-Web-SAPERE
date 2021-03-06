package it.apice.sapere.api.node.agents.networking;



/**
 * <p>
 * Interface for a generic subscriber.
 * </p>
 * 
 */
public interface Subscriber {

	/**
	 * <p>
	 * Sends a message to the subscriber.
	 * </p>
	 * 
	 * @param note
	 *            The message
	 */
	void sendMessage(Message note);
}
