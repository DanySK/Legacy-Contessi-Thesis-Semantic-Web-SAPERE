package it.apice.sapere.node.networking.bluetooth.impl;

import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.space.core.CompiledLSA;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.node.agents.AbstractSystemAgent;
import it.apice.sapere.node.agents.NodeServices;
import it.apice.sapere.node.internal.NodeServicesImpl;
import it.apice.sapere.node.networking.impl.NetworkManager;
import it.apice.sapere.node.networking.impl.NodeMessage;
import it.apice.sapere.node.networking.impl.NodeMessageType;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URI;
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
public final class BluetoothManagerAgent extends AbstractSystemAgent {

	/** Prefix for Synthetic Properties. */
	private static final transient String SYNT_PROPS_PREFIX = "http://"
			+ "www.sapere-project.eu/ontologies/2012/0/sapere-model.owl#";

	/** Type property URI (as String). */
	private static final transient String TYPE_PROP_URI = SYNT_PROPS_PREFIX
			+ "type";

	/** Neighbour individual URI (as String). */
	private static final transient String NEIGHB_INDIV_URI = SYNT_PROPS_PREFIX
			+ "neighbour";

	/** Where property URI (as String). */
	private static final transient String WHERE_PROP_URI = SYNT_PROPS_PREFIX
			+ "where";

	/** Longitude property URI (as String). */
	private static final transient String LNG_PROP_URI = SYNT_PROPS_PREFIX
			+ "longitude";

	/** Latitude property URI (as String). */
	private static final transient String LAT_PROP_URI = SYNT_PROPS_PREFIX
			+ "latitude";

	/** Orientation 1 property URI (as String). */
	private static final transient String OR1_PROP_URI = SYNT_PROPS_PREFIX
			+ "orientation1";

	/** Orientation 2 property URI (as String). */
	private static final transient String OR2_PROP_URI = SYNT_PROPS_PREFIX
			+ "orientation2";

	/** Orientation 3 property URI (as String). */
	private static final transient String OR3_PROP_URI = SYNT_PROPS_PREFIX
			+ "orientation3";

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
		Map<NetworkManager, BluetoothManagerAgent> INSTANCES = 
			new HashMap<NetworkManager, BluetoothManagerAgent>();

	/**
	 * <p>
	 * Returns the instance of the BluetoothManagerAgent (pattern Singleton).
	 * </p>
	 * 
	 * @param netManager
	 *            the networkManager
	 * @return the instance of the BluetoothManagerAgent
	 */
	public static BluetoothManagerAgent getInstance(
			final NetworkManager netManager) {
		BluetoothManagerAgent inst = INSTANCES.get(netManager);
		if (inst == null) {
			inst = new BluetoothManagerAgent(netManager);
			INSTANCES.put(netManager, inst);
		}

		return inst;
	}

	/**
	 * <p>
	 * Creates a BluetoothManagerAgent.
	 * </p>
	 * 
	 * @param aNetManager
	 *            a reference to the NetworkManager
	 */
	private BluetoothManagerAgent(final NetworkManager aNetManager) {
		super("btmanager");

		netManager = aNetManager;
		eventQueue = new LinkedList<MessageQueueData>();

		try {
			localDevice = LocalDevice.getLocalDevice();
			
			myNodeMessage = new NodeMessage(NodeMessageType.NODE_INFO,
					localDevice.getBluetoothAddress(), null, DUMMY_LATITUDE,
					DUMMY_LONGITUDE, new float[] { DUMMY_ORIENTATION_X,
							DUMMY_ORIENTATION_Y, DUMMY_ORIENTATION_Z });
		} catch (Exception ex) {
			fatal("Cannot retrieve local Bluetooth device", ex);
			throw new IllegalStateException("Cannot retrieve LocalDevice", ex);
		}
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
	 * <code>BluetoothManagerAgent.ALL_BT_ADDRESSES</code> is provided.
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
	protected void behaviour(final NodeServices services) {
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
						handleDiffuse(message, services);
						break;
					case NODE_INFO:
						handleNodeInfo(out, message, services);
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
	 * @param services
	 *            Reference to all node services
	 * @throws IOException
	 *             Error while serializing myNodeMessage
	 */
	private void handleNodeInfo(final OutputStream out,
			final NodeMessage message, final NodeServices services)
			throws IOException {
		neighbourRegistration(message, services);

		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(myNodeMessage);
	}

	/**
	 * <p>
	 * Registers neighbour's information in NeighboursTable and LSA-space.
	 * </p>
	 * 
	 * @param message
	 *            The NODE_INFO message to be handled
	 */
	void neighbourRegistration(final NodeMessage message) {
		neighbourRegistration(message, NodeServicesImpl.getInstance());
	}
			
	/**
	 * <p>
	 * Registers neighbour's information in NeighboursTable and LSA-space.
	 * </p>
	 * 
	 * @param message
	 *            The NODE_INFO message to be handled
	 * @param services
	 *            Reference to all node services
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void neighbourRegistration(final NodeMessage message,
			final NodeServices services) {
		String newId = netManager.registerNeighbour(new BTNeighbour(this,
				message.getSender()));

		final LSAspaceCore space = services.getLSAspace();
		final LSACompiler lsaComp = services.getLSACompiler();
		final CompiledLSA nInfoLSA = lsaComp.create();

		nInfoLSA.assertProperty(URI.create(TYPE_PROP_URI),
				URI.create(NEIGHB_INDIV_URI));
		nInfoLSA.assertProperty(URI.create(WHERE_PROP_URI), newId);
		nInfoLSA.assertProperty(URI.create(LNG_PROP_URI),
				"" + message.getLongitude());
		nInfoLSA.assertProperty(URI.create(LAT_PROP_URI),
				"" + message.getLatitude());
		nInfoLSA.assertProperty(URI.create(OR1_PROP_URI),
				"" + message.getOrientation()[0]);
		nInfoLSA.assertProperty(URI.create(OR2_PROP_URI),
				"" + message.getOrientation()[1]);
		nInfoLSA.assertProperty(URI.create(OR3_PROP_URI),
				"" + message.getOrientation()[2]);

		space.beginWrite();
		try {
			space.inject(nInfoLSA);
		} catch (SAPEREException ex) {
			error("Cannot inject Node Info", ex);
		} finally {
			space.done();
		}
	}

	/**
	 * <p>
	 * Handles a DIFFUSE Message.
	 * </p>
	 * 
	 * @param message
	 *            The DIFFUSE Message
	 * @param services
	 *            Reference to all node services
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void handleDiffuse(final NodeMessage message,
			final NodeServices services) {
		final LSAspaceCore space = services.getLSAspace();
		final LSACompiler lsaComp = services.getLSACompiler();

		space.beginWrite();
		try {
			space.inject(lsaComp.parse(message.getOperation().getLSA(),
					RDFFormat.RDF_XML));
			spy("Received diffusion " + message.getOperation().getLSAid());
		} catch (SAPEREException e) {
			error("Cannot receive diffusion", e);
		} finally {
			space.done();
		}
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
