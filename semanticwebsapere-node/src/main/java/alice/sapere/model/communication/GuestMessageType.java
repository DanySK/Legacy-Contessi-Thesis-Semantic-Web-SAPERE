package alice.sapere.model.communication;

import java.io.Serializable;

/**
 * Enum for the identification of messages between guests and GuestsAgent
 * 
 * @author Michele Morgagni
 * 
 */
public enum GuestMessageType implements Serializable {

	Inject, Reinject, MoveAway, Update, Refresh, ObserveOne, ObserveMany, Read, Deregister, Notify

}
