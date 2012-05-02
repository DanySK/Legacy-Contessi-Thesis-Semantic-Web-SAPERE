package alice.sapere.controller.core;

import alice.sapere.model.communication.Message;

/**
 * <p>
 * Interface for a generic subscriber.
 * </p>
 * 
 */
public interface ISubscriber {

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
