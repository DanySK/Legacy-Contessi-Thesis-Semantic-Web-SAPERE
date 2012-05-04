package it.apice.sapere.node.networking;

import java.io.Serializable;


/**
 * <p>
 * Messages exchanged between nodes.
 * </p>
 * 
 * @author Michele Morgagni
 * 
 */
public class NodeMessage implements Serializable {

	/** Serialization ID. */
	private static final long serialVersionUID = -1495387059182878463L;

	/** Message type. */
	private final NodeMessageType type;

	/** Operation invoked. */
	private final SpaceOperation operation;

	/** Geo-localization: latitude. */
	private final double latitude;

	/** Geo-localization: longitude. */
	private final double longitude;

	/** Geo-localization: orientation. */
	private final float[] orientation;

	/** Who sent the message. */
	private final String sender;

	/**
	 * <p>
	 * Creates the message to send.
	 * </p>
	 * 
	 * @param aType
	 *            the message type
	 * @param aSender
	 *            the sender id
	 * @param anOp
	 *            the operation invoked
	 * @param lat
	 *            latitude of the node
	 * @param lng
	 *            longitude of the node
	 * @param anOrientation
	 *            orientation of the node
	 */
	public NodeMessage(final NodeMessageType aType, final String aSender,
			final SpaceOperation anOp, final double lat, final double lng,
			final float[] anOrientation) {
		type = aType;
		sender = aSender;
		operation = anOp;
		latitude = lat;
		longitude = lng;
		orientation = anOrientation;
	}

	/**
	 * @return the type
	 */
	public final NodeMessageType getType() {
		return type;
	}

	/**
	 * @return the operation
	 */
	public final SpaceOperation getOperation() {
		return operation;
	}

	/**
	 * @return the latitude
	 */
	public final double getLatitude() {
		return latitude;
	}

	/**
	 * @return the longitude
	 */
	public final double getLongitude() {
		return longitude;
	}

	/**
	 * @return the orientation
	 */
	public final float[] getOrientation() {
		return orientation;
	}

	/**
	 * @return the sender
	 */
	public final String getSender() {
		return sender;
	}

}
