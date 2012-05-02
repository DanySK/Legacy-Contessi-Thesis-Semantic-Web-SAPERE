package alice.sapere.controller.networking.nodesmanage;

import it.apice.sapere.api.lsas.Property;
import it.apice.sapere.api.space.observation.SpaceOperationType;

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

public class BTClient {

	private Object inquiryCompletedEvent = new Object(),
			serviceSearchCompletedEvent = new Object();

	private DiscoveryAgent discoveryAgent;

	private List<RemoteDevice> foundedDevices;

	private ArrayList<ServiceRecord> services;

	private NodeMessage myNodeMessage;
	private BluetoothManager btManager;
	private NetworkManager netManager;

	private DiscoveryListener listener = new DiscoveryListener() {

		public void deviceDiscovered(final RemoteDevice btDevice,
				final DeviceClass cod) {
			foundedDevices.add(btDevice);
		}

		public void inquiryCompleted(final int discType) {

			synchronized (inquiryCompletedEvent) {
				inquiryCompletedEvent.notifyAll();
			}

		}

		public void serviceSearchCompleted(final int transID, final int respCode) {

			synchronized (serviceSearchCompletedEvent) {
				serviceSearchCompletedEvent.notifyAll();
			}
		}

		public void servicesDiscovered(final int transID,
				final ServiceRecord[] servRecord) {
			for (int i = 0; i < servRecord.length; i++) {

				DataElement serviceName = servRecord[i]
						.getAttributeValue(0x0100);
				if (serviceName != null
						&& ("" + serviceName).contains("ServerBT")) {
					services.add(servRecord[i]);
				}
			}
		}
	};

	public BTClient(final NodeMessage nodeMessage) {

		foundedDevices = new ArrayList<RemoteDevice>();
		services = new ArrayList<ServiceRecord>();
		myNodeMessage = nodeMessage;
		netManager = NetworkManager.getInstance();
		btManager = BluetoothManager.getInstance(netManager);

		try {

			discoveryAgent = LocalDevice.getLocalDevice().getDiscoveryAgent();
			this.contactAllDevices();

		} catch (Exception ex) {
			System.out.println("Impossible retrieve discovery agent: " + ex);
		}
	}

	public final void contactService(final ServiceRecord service,
			final NodeMessage message) {

		String url = service.getConnectionURL(
				ServiceRecord.NOAUTHENTICATE_NOENCRYPT, true);
		StreamConnection connection = null;
		try {
			connection = (StreamConnection) Connector.open(url);
			InputStream is = connection.openInputStream();
			OutputStream os = connection.openOutputStream();

			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(message);

			if (message.type == NodeMessageType.NodeInfo) {
				ObjectInputStream ois = new ObjectInputStream(is);
				NodeMessage messageRec = (NodeMessage) ois.readObject();

				String newId = netManager.registerNeighbour(new BTNeighbour(
						btManager, messageRec.sender));

				Property[] properties = new Property[7];
				properties[0] = new Property("type", new SetPropertyValue(
						"#neighbour"));
				properties[1] = new Property("where", new SetPropertyValue(
						newId));
				properties[2] = new Property("latitude", new SetPropertyValue(
						"" + messageRec.latitude));
				properties[3] = new Property("longitude", new SetPropertyValue(
						"" + messageRec.longitude));
				properties[4] = new Property("orientation1",
						new SetPropertyValue("" + messageRec.longitude));
				properties[5] = new Property("orientation2",
						new SetPropertyValue("" + messageRec.longitude));
				properties[6] = new Property("orientation3",
						new SetPropertyValue("" + messageRec.longitude));
				Content contentLoc = new JavaLsaContent(properties);

				SpaceOperation op2 = new SpaceOperation(
						SpaceOperationType.Inject, null, new Lsa(null,
								contentLoc), null, "system");
				ReactionManager.getInstance().queueOperation(op2, null);

			}

			is.close();
			os.close();
			connection.close();
		} catch (Exception ex) {
			System.out.println("Exception in connectToService: " + ex);
			ex.printStackTrace();
		}

	}

	public final void findServices() {
		try {
			foundedDevices = new ArrayList<RemoteDevice>();
			services = new ArrayList<ServiceRecord>();
			synchronized (inquiryCompletedEvent) {

				boolean started = discoveryAgent.startInquiry(
						DiscoveryAgent.GIAC, listener);
				if (started) {
					inquiryCompletedEvent.wait();

					for (int i = 0; i < foundedDevices.size(); i++) {
						synchronized (serviceSearchCompletedEvent) {
							discoveryAgent.searchServices(new int[] { 0x0100,
									0x0200 }, new UUID[] { new UUID(0x0003) },
									foundedDevices.get(i), listener);
							serviceSearchCompletedEvent.wait();
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

	public final void contactAllDevices() {

		findServices();
		for (int i = 0; i < services.size(); i++) {
			contactService(services.get(i), myNodeMessage);
		}
	}

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
