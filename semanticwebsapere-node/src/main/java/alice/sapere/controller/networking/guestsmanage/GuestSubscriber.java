package alice.sapere.controller.networking.guestsmanage;

import alice.sapere.controller.core.ISubscriber;
import alice.sapere.model.communication.Message;

/**
 * <p>
 * Subscriber of type guest.
 * </p>
 * 
 * @author Michele Morgagni
 * 
 */
public class GuestSubscriber implements ISubscriber {

	/** Guest ID. */
	private final String destination;

	/**
	 * <p>
	 * Creates a GuestSubscriber.
	 * </p>
	 * 
	 * @param aDestination
	 *            the guest id
	 */
	public GuestSubscriber(final String aDestination) {
		destination = aDestination;

	}

	/**
	 * <p>
	 * Retrieves the destination.
	 * </p>.
	 * 
	 * @return the destination
	 */
	public final String getDestination() {
		return destination;
	}

	/**
	 * <p>
	 * Sends the message using GuestsAgent.
	 * </p>
	 * 
	 * @param note
	 *            the message to send (notification)
	 */
	public final void sendMessage(final Message note) {
		GuestsAgent.getInstance().sendMessageToGuest(note, destination);
	}
}
