package alice.sapere.controller.networking.nodesmanage;

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
	public NodeMessageType type;

	/** Operation invoked. */
	public SpaceOperation operation;

	/** Geo-localization: latitude. */
	public double latitude;

	/** Geo-localization: longitude. */
	public double longitude;

	/** Geo-localization: orientation. */
	public float[] orientation;

	/** Who sent the message. */
	public String sender;

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
			final SpaceOperation anOp, final double lat,
			final double lng, final float[] anOrientation) {
		type = aType;
		sender = aSender;
		operation = anOp;
		latitude = lat;
		longitude = lng;
		orientation = anOrientation;
	}

}
