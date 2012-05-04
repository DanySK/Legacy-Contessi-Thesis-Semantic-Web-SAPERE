package it.apice.sapere.node.networking.guestsmngt;

import java.io.Serializable;

/**
 * <p>
 * Enumeration for the identification of messages between guests and
 * GuestsAgent.
 * </p>
 * 
 * @author Michele Morgagni
 * 
 */
public enum GuestMessageType implements Serializable {
	/**  INJECT message type. */
	INJECT,

	/**  Re inject message type. */
	RE_INJECT,

	/**  Move Away message type. */
	MOVE_AWAY,

	/**  UPDATE message type. */
	UPDATE,

	/**  Refresh message type. */
	REFRESH,

	/**  OO message type. */
	OBSERVE_ONCE,

	/**  OM message type. */
	OBSERVE,

	/**  READ message type. */
	READ,

	/**  Cancel registration message type. */
	CANCEL_SUBSCRIPTION,

	/**  Notify message type. */
	NOTIFY;

}
