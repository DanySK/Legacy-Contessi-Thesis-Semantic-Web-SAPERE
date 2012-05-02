package alice.sapere.controller.networking.nodesmanage;

import it.apice.sapere.api.lsas.Property;
import it.apice.sapere.api.space.observation.SpaceOperationType;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import alice.sapere.controller.core.space.SpaceAgent;

/**
 * <p>
 * The manager for receiving messages throw Bluetooth from other nodes, using
 * single BT device (actually used).
 * </p>
 * 
 * @author Michele Morgagni
 * 
 */
public final class BluetoothManager extends SpaceAgent {

	private ArrayList<MessageQueueData> eventQueue;

	private BTClient btClient;
	private LocalDevice localDevice;
	NetworkManager netManager;

	private NodeMessage myNodeMessage;

	/** Singleton instance. */
	private static transient BluetoothManager instance;

	/**
	 * <p>
	 * Returns the instance of the BluetoothManager (pattern Singleton).
	 * </p>
	 * 
	 * @param netManager
	 *            the networkManager
	 * @return the instance of the BluetoothManager
	 */
	public static BluetoothManager getInstance(final NetworkManager netManager) {
		if (instance == null) {
			instance = new BluetoothManager(netManager);
		}

		return instance;
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

		eventQueue = new ArrayList<MessageQueueData>();

		try {
			localDevice = LocalDevice.getLocalDevice();
		} catch (Exception ex) {
			System.out.println("Impossible retrieving local device");
			return;
		}

		myNodeMessage = new NodeMessage(NodeMessageType.NodeInfo,
				localDevice.getBluetoothAddress(), null, 10.0, 10.0,
				new float[] { (float) 1.0, (float) 2.0, (float) 3.0 });

		this.start();
	}

	@Override
	protected void execute() {
		btClient = new BTClient(myNodeMessage);
		routine();
	}

	/**
	 * <p>
	 * Invoked for contacting one or all neighbour nodes.
	 * </p>
	 * 
	 * @param btAddress
	 *            the neighbour to contact, null for contacting all neighbours
	 * @param message
	 *            the message to send
	 */
	public void contactOtherNodes(final String btAddress,
			final NodeMessage message) {

		if (btAddress == null) {
			eventQueue.add(new MessageQueueData("all", null));
		} else {
			eventQueue.add(new MessageQueueData(btAddress, message));
		}

		this.interrupt();
	}

	/**
	 * <p>
	 * The routine of the agent.
	 * </p>
	 */
	public void routine() {
		try {

			String address = localDevice.getBluetoothAddress();
			String name = localDevice.getFriendlyName();
			System.out.println("Bluetooth device: " + name
					+ "\nBluetooth address: " + address);

			String serverUUID = "11111111111111111111111111111123";

			StreamConnectionNotifier service;

			while (true) {

				service = (StreamConnectionNotifier) Connector
						.open("btspp://localhost:" + serverUUID
								+ ";name=ServerBT");

				spy("Waiting for a connection...");

				try {
					StreamConnection connection = (StreamConnection) service
							.acceptAndOpen();
					spy("Connection accepted.");

					InputStream in = connection.openInputStream();
					OutputStream out = connection.openOutputStream();

					ObjectInputStream ois = new ObjectInputStream(in);
					NodeMessage message = (NodeMessage) ois.readObject();

					switch (message.type) {
					case Diffuse:
						// managing diffuse
						doInject(message.operation.getLsa(), "system");
						spy("received diffusion " + message.operation.getLsa());
						break;
					case NodeInfo:
						// managing nodeinfo
						String newId = netManager
								.registerNeighbour(new BTNeighbour(this,
										message.sender));

						Property[] properties = new Property[7];
						properties[0] = new Property("type",
								new SetPropertyValue("#neighbour"));
						properties[1] = new Property("where",
								new SetPropertyValue(newId));
						properties[2] = new Property("latitude",
								new SetPropertyValue("" + message.latitude));
						properties[3] = new Property("longitude",
								new SetPropertyValue("" + message.longitude));
						properties[4] = new Property("orientation1",
								new SetPropertyValue("" + message.longitude));
						properties[5] = new Property("orientation2",
								new SetPropertyValue("" + message.longitude));
						properties[6] = new Property("orientation3",
								new SetPropertyValue("" + message.longitude));
						Content contentLoc = new JavaLsaContent(properties);

						SpaceOperation op2 = new SpaceOperation(
								SpaceOperationType.Inject, null, new Lsa(null,
										contentLoc), null, "system");
						ReactionManager.getInstance().queueOperation(op2, null);

						ObjectOutputStream oos = new ObjectOutputStream(out);
						oos.writeObject(myNodeMessage);

						break;
					default:
						spy("Error on NodeMessage type");
					}

					in.close();
					out.close();
					connection.close();
					service.close();

				} catch (Exception ex) {
					service.close();
					parseEventList();
				}

			}

		} catch (Exception ex) {
			System.out.println("Exception in BTManager: " + ex);
			ex.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Parsing events.
	 * </p>
	 */
	public void parseEventList() {
		for (int i = 0; i < eventQueue.size(); i++) {
			if (eventQueue.get(i).equals("all")) {
				btClient.contactAllDevices();
			} else {
				btClient.contactDevice(eventQueue.get(i).btAddress,
						eventQueue.get(i).message);
			}
		}
		eventQueue = new ArrayList<MessageQueueData>();
	}

	/**
	 * <p>
	 * Class for events storage.
	 * </p>
	 * 
	 * @author Michele Morgagni
	 * 
	 */
	private class MessageQueueData {

		/** BT address. */
		public String btAddress;

		/** The message. */
		public NodeMessage message;

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
	}

}
