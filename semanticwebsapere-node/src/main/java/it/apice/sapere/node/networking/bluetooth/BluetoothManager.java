package it.apice.sapere.node.networking.bluetooth;

import it.apice.sapere.node.agents.InternalAgent;
import it.apice.sapere.node.networking.NetworkManager;
import it.apice.sapere.node.networking.NodeMessage;
import it.apice.sapere.node.networking.NodeMessageType;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 * <p>
 * This class represents an entity capable of handling Bluetooth local devices
 * (currently only one) and exploiting in order to enact a secondary means of
 * communication between nodes.
 * </p>
 * 
 * <p>
 * During RDF-based LSA-space integration phase, some refactoring and debug has
 * been done.
 * </p>
 * 
 * @author Michele Morgagni
 * @author Paolo Contessi
 * 
 */
public final class BluetoothManager extends InternalAgent {

	/* === BLUETOOTH PROTOCOL CONSTANTS (begin) === */

	/** Wildcard that means all the Bluetooth Addresses. */
	public static final transient String ALL_BT_ADDRESSES = "all";

	/** Bluetooth Server UUID. */
	private static final transient String SERVER_UUID = "11111"
			+ "111111111111111111111111123";

	/* ==== BLUETOOTH PROTOCOL CONSTANTS (end) ==== */

	/** Orientation Z-axis. */
	private static final transient float DUMMY_ORIENTATION_Z = 3.0F;

	/** Orientation Y-axis. */
	private static final transient float DUMMY_ORIENTATION_Y = 2.0F;

	/** Orientation X-axis. */
	private static final transient float DUMMY_ORIENTATION_X = 1.0F;

	/** GeoLoc Longitude. */
	private static final transient double DUMMY_LONGITUDE = 10.0;

	/** GeoLoc Latitude. */
	private static final transient double DUMMY_LATITUDE = 10.0;

	/** Event Queue. */
	private final transient List<MessageQueueData> eventQueue;

	/** Bluetooth Client. */
	private transient BTClient btClient;

	/** Bluetooth device. */
	private final transient LocalDevice localDevice;

	/** Network manager. */
	private final transient NetworkManager netManager;

	/** Personal Node Message. */
	private NodeMessage myNodeMessage;

	/** Map of all instances per {@link NetworkManager}. */
	private static final transient 
		Map<NetworkManager, BluetoothManager> INSTANCES = 
		new HashMap<NetworkManager, BluetoothManager>();

	/**
	 * <p>
	 * Returns the instance of the BluetoothManager (pattern Singleton).
	 * </p>
	 * 
	 * @param netManager
	 *            the networkManager
	 * @return the instance of the BluetoothManager
	 */
	public static BluetoothManager getInstance(
			final NetworkManager netManager) {
		BluetoothManager inst = INSTANCES.get(netManager);
		if (inst == null) {
			inst = new BluetoothManager(netManager);
			INSTANCES.put(netManager, inst);
		}

		return inst;
	}

	/**
	 * <p>
	 * Creates a BluetoothManager.
	 * </p>
	 * 
	 * @param aNetManager
	 *            a reference to the NetworkManager
	 */
	private BluetoothManager(final NetworkManager aNetManager) {
		super("btmanager");

		netManager = aNetManager;
		eventQueue = new LinkedList<MessageQueueData>();

		try {
			localDevice = LocalDevice.getLocalDevice();
		} catch (Exception ex) {
			fatal("Cannot retrieve local Bluetooth device", ex);
			throw new IllegalStateException("Cannot retrieve LocalDevice", ex);
		}

		myNodeMessage = new NodeMessage(NodeMessageType.NODE_INFO,
				localDevice.getBluetoothAddress(), null, DUMMY_LATITUDE,
				DUMMY_LONGITUDE, new float[] { DUMMY_ORIENTATION_X,
						DUMMY_ORIENTATION_Y, DUMMY_ORIENTATION_Z });

		// TODO this.start();
	}

	/**
	 * <p>
	 * Invoked in order to contact all neighbour nodes.
	 * </p>
	 * 
	 * @param message
	 *            the message to send
	 */
	public void contactAllOtherNodes(final NodeMessage message) {
		eventQueue.add(new MessageQueueData(ALL_BT_ADDRESSES, null));
		interrupt();
	}

	/**
	 * <p>
	 * Invoked in order to contact a specific neighbour node or all nodes if
	 * <code>BluetoothManager.ALL_BT_ADDRESSES</code> is provided.
	 * </p>
	 * 
	 * @param btAddress
	 *            the neighbour to contact
	 * @param message
	 *            the message to send
	 */
	public void contactOtherNodes(final String btAddress,
			final NodeMessage message) {
		if (btAddress == null) {
			throw new IllegalArgumentException("Cannot contact null address");
		}

		eventQueue.add(new MessageQueueData(btAddress, message));
		interrupt();
	}

	@Override
	protected void execute() {
		btClient = new BTClient(myNodeMessage);

		try {
			String address = localDevice.getBluetoothAddress();
			String name = localDevice.getFriendlyName();
			System.out.println("Bluetooth device: " + name
					+ "\nBluetooth address: " + address);

			StreamConnectionNotifier service = null;

			while (isRunning()) {

				service = (StreamConnectionNotifier) Connector
						.open("btspp://localhost:" + SERVER_UUID
								+ ";name=ServerBT");

				spy("Waiting for a connection...");

				InputStream in = null;
				OutputStream out = null;
				StreamConnection connection = null;

				try {
					connection = (StreamConnection) service.acceptAndOpen();
					spy("Connection accepted.");

					in = connection.openInputStream();
					out = connection.openOutputStream();

					ObjectInputStream ois = new ObjectInputStream(in);
					NodeMessage message = (NodeMessage) ois.readObject();

					switch (message.getType()) {
					case DIFFUSE:
						handleDiffuse(message);
						break;
					case NODE_INFO:
						handleNodeInfo(out, message);
						break;
					default:
						spy("Error on NodeMessage type: unsupported type");
					}
				} catch (Exception ex) {
					service.close();
					parseEventList();
				} finally {
					if (in != null) {
						in.close();
					}

					if (out != null) {
						out.close();
					}

					if (connection != null) {
						connection.close();
					}

					if (service != null) {
						service.close();
					}
				}

			}

		} catch (Exception ex) {
			fatal("Exception in BTManager", ex);
		}
	}

	/**
	 * <p>
	 * Handles a NODE_INFO message.
	 * </p>
	 * 
	 * @param out
	 *            Output Stream
	 * @param message
	 *            The NODE_INFO message to be handled
	 * @throws IOException
	 *             Error while serializing myNodeMessage
	 */
	private void handleNodeInfo(final OutputStream out,
			final NodeMessage message) throws IOException {
		String newId = netManager.registerNeighbour(new BTNeighbour(this,
				message.getSender()));

		// Property[] properties = new Property[7];
		// properties[0] = new Property("type", new SetPropertyValue(
		// "#neighbour"));
		// properties[1] = new Property("where", new SetPropertyValue(newId));
		// properties[2] = new Property("latitude", new SetPropertyValue(""
		// + message.getLatitude()));
		// properties[3] = new Property("longitude", new SetPropertyValue(""
		// + message.getLongitude()));
		// properties[4] = new Property("orientation1", new SetPropertyValue(""
		// + message.getOrientation()[0]));
		// properties[5] = new Property("orientation2", new SetPropertyValue(""
		// + message.getOrientation()[1]));
		// properties[6] = new Property("orientation3", new SetPropertyValue(""
		// + message.Orientation()[2]));
		// Content contentLoc = new JavaLsaContent(properties);
		//
		// SpaceOperation op2 = new SpaceOperation(SpaceOperationType.Inject,
		// null, new Lsa(null, contentLoc), null, "system");
		// ReactionManager.getInstance().queueOperation(op2, null);

		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(myNodeMessage);
	}

	/**
	 * <p>
	 * Handles a DIFFUSE Message.
	 * </p>
	 * 
	 * @param message
	 *            The DIFFUSE Message
	 */
	private void handleDiffuse(final NodeMessage message) {
		// doInject(message.getOperation().getLsa(), "system");
		spy("received diffusion " + message.getOperation().getLSAid());
	}

	/**
	 * <p>
	 * Parsing events.
	 * </p>
	 */
	public void parseEventList() {
		final Iterator<MessageQueueData> iter = eventQueue.iterator();

		boolean shouldClear = false;
		while (iter.hasNext()) {
			final MessageQueueData data = iter.next();
			if (data.getBTAddress().equals(ALL_BT_ADDRESSES)) {
				btClient.contactAllDevices();
			} else {
				btClient.contactDevice(data.getBTAddress(), data.getMessage());
			}

			try {
				iter.remove();
			} catch (Exception ex) {
				shouldClear = true;
			}
		}

		if (shouldClear) {
			eventQueue.clear();
		}

		// @author Michele Morgagni
		// for (int i = 0; i < eventQueue.size(); i++) {
		// if (eventQueue.get(i).equals("all")) {
		// btClient.contactAllDevices();
		// } else {
		// btClient.contactDevice(eventQueue.get(i).btAddress,
		// eventQueue.get(i).message);
		// }
		// }
		//
		// eventQueue = new ArrayList<MessageQueueData>();
	}

	/**
	 * <p>
	 * Class for events storage.
	 * </p>
	 * 
	 * @author Michele Morgagni
	 * 
	 */
	private static class MessageQueueData {

		/** BT address. */
		private final String btAddress;

		/** The message. */
		private final NodeMessage message;

		/**
		 * <p>
		 * Builds a new {@link MessageQueueData}.
		 * </p>
		 * 
		 * @param aBtAddress
		 *            BT address
		 * @param aMessage
		 *            Message to be sent
		 */
		public MessageQueueData(final String aBtAddress,
				final NodeMessage aMessage) {
			btAddress = aBtAddress;
			message = aMessage;
		}

		/**
		 * @return The Bluetooth Address
		 */
		public String getBTAddress() {
			return btAddress;
		}

		/**
		 * @return the Message
		 */
		public NodeMessage getMessage() {
			return message;
		}
	}

}
