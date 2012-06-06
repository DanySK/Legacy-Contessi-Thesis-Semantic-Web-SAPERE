package it.apice.sapere.node.networking.guestsmngt.impl;

import it.apice.sapere.api.node.agents.networking.Message;
import it.apice.sapere.api.node.agents.networking.Subscriber;

/**
 * <p>
 * Subscriber of type guest.
 * </p>
 * 
 * @author Michele Morgagni
 * 
 */
public class GuestSubscriber implements Subscriber {

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
	 * Sends the message using GuestsHandlerAgent.
	 * </p>
	 * 
	 * @param note
	 *            the message to send (notification)
	 */
	@SuppressWarnings("deprecation")
	public final void sendMessage(final Message note) {
		GuestsHandlerAgent.getInstance().sendMessageToGuest(note, destination);
	}
}
