package it.apice.sapere.node.networking.guestsmngt;

import java.io.Serializable;

/**
 * <p>
 * Enumeration for the identification of messages between guests and
 * GuestsHandlerAgent.
 * </p>
 * 
 * @author Michele Morgagni
 * 
 */
public enum GuestMessageType implements Serializable {
	/** INJECT message type. */
	INJECT,

	/** Re inject message type. */
	RE_INJECT,

	/** UPDATE message type. */
	UPDATE,

	/** READ message type. */
	READ,

	/** Refresh message type. */
	REFRESH,

	/** Move Away message type. */
	MOVE_AWAY,

	/** One time observation message type. */
	OBSERVE_ONCE,

	/** Permanent observation message type. */
	OBSERVE,

	/** Cancel registration message type. */
	CANCEL_SUBSCRIPTION,

	/** Notify message type. */
	NOTIFY;

}
