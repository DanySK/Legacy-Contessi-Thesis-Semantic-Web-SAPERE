package it.apice.sapere.node.networking.bluetooth.impl;

import it.apice.sapere.node.networking.impl.Neighbour;
import it.apice.sapere.node.networking.impl.NodeMessage;

/**
 * <p>
 * Class for the registration of a BT neighbour, using a single Bluetooth Device
 * (actually used).
 * </p>
 * 
 * @author Michele Morgagni
 * 
 */
@Deprecated
public class BTNeighbour implements Neighbour {

	/** Reference to BT Manager. */
	private BluetoothManagerAgent manager;

	/** BT Neighbour's address. */
	private String btAddress;

	/**
	 * <p>
	 * Creates the BTNeighbour.
	 * </p>
	 * 
	 * @param aManager
	 *            the BluetoothManagerAgent of the node
	 * @param aBtAddress
	 *            the BluetoothAddress of the neighbour
	 */
	public BTNeighbour(final BluetoothManagerAgent aManager, 
			final String aBtAddress) {
		manager = aManager;
		btAddress = aBtAddress;

	}

	@Override
	public final String getId() {
		return btAddress;
	}

	@Override
	public final void send(final NodeMessage message) {
		manager.contactOtherNodes(btAddress, message);
	}

}
