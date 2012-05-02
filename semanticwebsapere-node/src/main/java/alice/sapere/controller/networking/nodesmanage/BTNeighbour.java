package alice.sapere.controller.networking.nodesmanage;

/**
 * <p>
 * Class for the registration of a BT neighbour, using a single Bluetooth Device
 * (actually used).
 * </p>
 * 
 * @author Michele Morgagni
 * 
 */
public class BTNeighbour implements INeighbour {

	/** Reference to BT Manager. */
	private BluetoothManager manager;

	/** BT Neighbour's address. */
	private String btAddress;

	/**
	 * <p>
	 * Creates the BTNeighbour.
	 * </p>
	 * 
	 * @param aManager
	 *            the BluetoothManager of the node
	 * @param aBtAddress
	 *            the BluetoothAddress of the neighbour
	 */
	public BTNeighbour(final BluetoothManager aManager, 
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
