package it.apice.sapere.node.networking.bluetooth;

import it.apice.sapere.node.networking.NetworkManager;
import it.apice.sapere.node.networking.NodeMessage;
import it.apice.sapere.node.networking.NodeMessageType;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * This class models a Bluetooth Client.
 * </p>
 * 
 * <p>
 * Some modifications has been made during RDF-based LSA-space integration.
 * </p>
 * 
 * @author Paolo Contessi
 */
public class BTClient {

	/* === BLUETOOTH PROTOCOL CONSTANTS (begin) === */

	/** 0x0100 value. */
	private static final transient int VAL_0X0100 = 0x0100;

	/** 0x0003 value. */
	private static final int VAL_0X0003 = 0x0003;

	/** 0x0200 value. */
	private static final int VAL_0X0200 = 0x0200;

	/* ==== BLUETOOTH PROTOCOL CONSTANTS (end) ==== */

	/** Foo Object for Lock mechanism. */
	private final transient Object inquiryCompletedEvent = new Object();

	/** Inquiry Lock Condition. */
	private transient boolean isInqCompleted;

	/** Foo Object for Lock mechanism. */
	private final transient Object serviceSearchCompletedEvent = new Object();

	/** ServiceSearch Lock Condition. */
	private transient boolean isSearchCompleted;

	/** Agent responsible for Devices Discovery. */
	private DiscoveryAgent discoveryAgent;

	/** List of Remote Devices. */
	private List<RemoteDevice> foundedDevices;

	/** List of Services and relative info. */
	private List<ServiceRecord> services;

	/** Reference to MyNodeMessage. */
	private NodeMessage myNodeMessage;

	/** Reference to Bluetooth Manager. */
	private BluetoothManager btManager;

	/** Reference to Network Manager. */
	private NetworkManager netManager;

	/** Entity which listens to new devices discovery. . */
	private DiscoveryListener listener = new DiscoveryListener() {

		public void deviceDiscovered(final RemoteDevice btDevice,
				final DeviceClass cod) {
			foundedDevices.add(btDevice);
		}

		public void inquiryCompleted(final int discType) {

			synchronized (inquiryCompletedEvent) {
				isInqCompleted = true;
				inquiryCompletedEvent.notifyAll();
			}

		}

		public void serviceSearchCompleted(final int transID, 
				final int respCode) {

			synchronized (serviceSearchCompletedEvent) {
				isSearchCompleted = true;
				serviceSearchCompletedEvent.notifyAll();
			}
		}

		public void servicesDiscovered(final int transID,
				final ServiceRecord[] servRecord) {
			for (int i = 0; i < servRecord.length; i++) {

				DataElement serviceName = servRecord[i]
						.getAttributeValue(VAL_0X0100);
				if (serviceName != null
						&& ("" + serviceName).contains("ServerBT")) {
					services.add(servRecord[i]);
				}
			}
		}
	};

	/**
	 * <p>
	 * Builds a new {@link BTClient}.
	 * </p>
	 * 
	 * @param nodeMessage
	 *            Its {@link NodeMessage}
	 */
	public BTClient(final NodeMessage nodeMessage) {

		foundedDevices = new ArrayList<RemoteDevice>();
		services = new ArrayList<ServiceRecord>();
		myNodeMessage = nodeMessage;
		netManager = NetworkManager.getInstance();
		btManager = BluetoothManager.getInstance(netManager);

		try {

			discoveryAgent = LocalDevice.getLocalDevice().getDiscoveryAgent();
			contactAllDevices();

		} catch (Exception ex) {
			LogFactory
					.getLog(BTClient.class)
					.warn("Cannot retrieve DiscoveryAgent. "
							+ "This client will not discover any remote device",
							ex);
		}
	}

	/**
	 * @param service
	 *            A {@link ServiceRecord}
	 * @param message
	 *            A {@link NodeMessage}
	 */
	public final void contactService(final ServiceRecord service,
			final NodeMessage message) {

		String url = service.getConnectionURL(
				ServiceRecord.NOAUTHENTICATE_NOENCRYPT, true);
		StreamConnection connection = null;
		InputStream is = null;
		OutputStream os = null;

		try {
			connection = (StreamConnection) Connector.open(url);
			is = connection.openInputStream();
			os = connection.openOutputStream();

			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(message);

			if (message.getType() == NodeMessageType.NODE_INFO) {
				ObjectInputStream ois = new ObjectInputStream(is);
				try {
					NodeMessage messageRec = (NodeMessage) ois.readObject();

					String newId = netManager
							.registerNeighbour(new BTNeighbour(btManager,
									messageRec.getSender()));
					assert newId != null;

					// Property[] properties = new Property[7];
					// properties[0] = new Property("type", new
					// SetPropertyValue(
					// "#neighbour"));
					// properties[1] = new Property("where", new
					// SetPropertyValue(
					// newId));
					// properties[2] = new Property("latitude",
					// new SetPropertyValue("" + messageRec.getLatitude()));
					// properties[3] = new Property(
					// "longitude",
					// new SetPropertyValue("" + messageRec.getLongitude()));
					// properties[4] = new Property(
					// "orientation1",
					// new SetPropertyValue("" +
					// messageRec.getOrientation()[0]));
					// properties[5] = new Property(
					// "orientation2",
					// new SetPropertyValue("" + messageRec.Orientation()[1]));
					// properties[6] = new Property(
					// "orientation3",
					// new SetPropertyValue("" + messageRec.Orientation()[2]));
					// Content contentLoc = new JavaLsaContent(properties);
					//
					// SpaceOperation op2 = new SpaceOperation(
					// SpaceOperationType.Inject, null, new Lsa(null,
					// contentLoc), null, "system");
					// ReactionManager.getInstance().queueOperation(op2, null);
				} finally {
					if (ois != null) {
						ois.close();
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("Exception in connectToService: " + ex);
			ex.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception ex) {
					LogFactory.getLog(BTClient.class).warn(
							"Cannot close input stream", ex);
				}
			}

			if (os != null) {
				try {
					os.close();
				} catch (Exception ex) {
					LogFactory.getLog(BTClient.class).warn(
							"Cannot close output stream", ex);
				}
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (Exception ex) {
					LogFactory.getLog(BTClient.class).warn(
							"Cannot close connection", ex);
				}
			}
		}

	}

	/**
	 * <p>
	 * Tries to discover new services.
	 * </p>
	 */
	public final void findServices() {
		try {
			foundedDevices = new ArrayList<RemoteDevice>();
			services = new ArrayList<ServiceRecord>();
			synchronized (inquiryCompletedEvent) {

				boolean started = discoveryAgent.startInquiry(
						DiscoveryAgent.GIAC, listener);
				if (started) {
					while (!isInqCompleted) {
						inquiryCompletedEvent.wait();
					}

					isInqCompleted = false;

					for (int i = 0; i < foundedDevices.size(); i++) {
						synchronized (serviceSearchCompletedEvent) {
							discoveryAgent.searchServices(new int[] {
									VAL_0X0100, VAL_0X0200 },
									new UUID[] { new UUID(VAL_0X0003) },
									foundedDevices.get(i), listener);

							while (!isSearchCompleted) {
								serviceSearchCompletedEvent.wait();
							}
							isSearchCompleted = false;
						}
					}
				} else {
					System.out.println("Failed to launch discovery");
				}
			}
		} catch (Exception ex) {
			System.out.println("Exception in findServices: " + ex);
			ex.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Sends <code>myNodeMessage</code> to all known devices.
	 * </p>
	 */
	public final void contactAllDevices() {

		findServices();
		for (int i = 0; i < services.size(); i++) {
			contactService(services.get(i), myNodeMessage);
		}
	}

	/**
	 * <p>
	 * Sends a {@link NodeMessage} to the specified device.
	 * </p>
	 * 
	 * @param btAddress
	 *            A bluetooth address
	 * @param message
	 *            A {@link NodeMessage}
	 */
	public final void contactDevice(final String btAddress,
			final NodeMessage message) {
		boolean founded = false;
		for (int i = 0; i < services.size(); i++) {
			if (services.get(i).getHostDevice().getBluetoothAddress()
					.equals(btAddress)) {
				contactService(services.get(i), message);
				founded = true;
			}
		}
		if (!founded) {
			findServices();
			for (int i = 0; i < services.size(); i++) {
				if (services.get(i).getHostDevice().getBluetoothAddress()
						.equals(btAddress)) {
					contactService(services.get(i), message);
				}
			}
		}
	}

}
