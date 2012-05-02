package alice.sapere.model.communication;

import java.io.Serializable;

/**
 * Messages exchanged between guests and GuestsAgent
 * 
 * @author Michele Morgagni
 * 
 */
public class GuestMessage implements Serializable {

	public GuestMessageType type;
	public LsaId id;
	public Content content;
	public int signal;
	public float[] orientation;
	public String macAddress;

	/**
	 * Creates a new GuestMessage
	 * 
	 * @param aType
	 *            the type of the message
	 * @param anId
	 *            the identifier of the LSA
	 * @param aContent
	 *            the content of the LSA
	 * @param aSignal
	 *            the power of the wi-fi signal (for a distance extimation)
	 * @param anOrientation
	 *            the values of the orientation sensor
	 * @param aMacAddress
	 *            the macAddress of the mobile device
	 */
	public GuestMessage(final GuestMessageType aType, final LsaId anId,
			final Content aContent, final int aSignal, final float[] anOrientation,
			final String aMacAddress) {
		this.type = aType;
		id = anId;
		content = aContent;
		this.signal = aSignal;
		this.orientation = anOrientation;
		this.macAddress = aMacAddress;

	}

}
