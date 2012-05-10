package it.apice.sapere.node.networking.guestsmngt.impl;

import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.core.CompiledLSA;

import java.io.Serializable;

/**
 * <p>
 * Messages exchanged between guests and GuestsHandlerAgent.
 * </p>
 * <p>
 * In order to provide a better integration with RDF-based part the LSA
 * representation has been modified: now the message delivers a serialized
 * version of the LSA as RDF/XML.
 * </p>
 * 
 * @author Michele Morgagni
 * @author Paolo Contessi
 * 
 */
public class GuestMessage implements Serializable {

	/** Serialization ID. */
	private static final long serialVersionUID = -9120707265217485532L;

	/** Message type. */
	private final GuestMessageType type;

	/** LSA-id of wrapped LSA. */
	private final LSAid id;

	/** LSA to be sent, serialized as RDF/XML. */
	private final String serLsa;

	/** Signal power field. */
	private final int signal;

	/** Orientation field. */
	private final float[] orientation;

	/** MAC Address. */
	private final String macAddress;

	/**
	 * <p>
	 * Creates a new GuestMessage.
	 * </p>
	 * 
	 * @param aType
	 *            The type of the message
	 * @param lsa
	 *            The LSA to be sent
	 * @param aSignal
	 *            The power of the Wi-Fi signal (for a distance estimation)
	 * @param anOrientation
	 *            The values of the orientation sensor
	 * @param aMacAddress
	 *            The macAddress of the mobile device
	 */
	public GuestMessage(final GuestMessageType aType, final CompiledLSA<?> lsa,
			final int aSignal, final float[] anOrientation,
			final String aMacAddress) {
		this(aType, lsa.getLSAid(), lsa.toString(RDFFormat.RDF_XML), aSignal,
				anOrientation, aMacAddress);
	}

	/**
	 * <p>
	 * Creates a new GuestMessage.
	 * </p>
	 * 
	 * @param aType
	 *            The type of the message
	 * @param lsaId
	 *            The LSA-id of the LSA to be sent
	 * @param serializedLSA
	 *            The RDF/XML String representing the LSA
	 * @param aSignal
	 *            The power of the Wi-Fi signal (for a distance estimation)
	 * @param anOrientation
	 *            The values of the orientation sensor
	 * @param aMacAddress
	 *            The macAddress of the mobile device
	 */
	public GuestMessage(final GuestMessageType aType, final LSAid lsaId,
			final String serializedLSA, final int aSignal,
			final float[] anOrientation, final String aMacAddress) {
		type = aType;
		id = lsaId;
		serLsa = serializedLSA;
		signal = aSignal;
		orientation = anOrientation;
		macAddress = aMacAddress;

	}

	/**
	 * <p>
	 * Retrieves the type of the message.
	 * </p>
	 * 
	 * @return The type
	 */
	public final GuestMessageType getType() {
		return type;
	}

	/**
	 * <p>
	 * Retrieves the LSA-id of the delivered LSA.
	 * </p>
	 * 
	 * @return The id
	 */
	public final LSAid getId() {
		return id;
	}

	/**
	 * <p>
	 * Retrieves a String in RDF/XML that represents the LSA.
	 * </p>
	 * 
	 * @return The LSA, written in RDF/XML
	 */
	public final String getLSA() {
		return serLsa;
	}

	/**
	 * <p>
	 * Retrieves the estimated Wi-Fi signal power.
	 * </p>
	 * 
	 * @return Wi-Fi signal power estimation
	 */
	public final int getSignal() {
		return signal;
	}

	/**
	 * <p>
	 * Retrieves the orientation of the device.
	 * </p>
	 * 
	 * @return The orientation
	 */
	public final float[] getOrientation() {
		return orientation;
	}

	/**
	 * <p>
	 * Retrieves the MAC address of the device.
	 * </p>
	 * 
	 * @return The MAC address
	 */
	public final String getMacAddress() {
		return macAddress;
	}

}
