package alice.sapere.controller.networking.guestsmanage;

import it.apice.sapere.api.lsas.Property;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.apache.commons.logging.LogFactory;

import alice.sapere.controller.core.InternalAgent;
import alice.sapere.controller.core.space.SpaceAgent;
import alice.sapere.model.communication.GuestMessage;
import alice.sapere.model.communication.Message;

/**
 * This class is the main part for the management of guests connected to the
 * node. It receives messages from clients and sends back response, it sends
 * notifies too.
 * 
 * @author Michele Morgagni
 * 
 */
public final class GuestsAgent extends InternalAgent {

	/** The PORT of the server. */
	private static final transient int PORT = 4444;

	/** Server socket. */
	private ServerSocket server;

	/** Localization map. */
	private HashMap<String, String> locMap;

	/** MAC-IP map. */
	private HashMap<String, String> ipMap;

	/** Singleton instance. */
	private static transient GuestsAgent instance;

	/**
	 * <p>
	 * Retrieves the instance of the GuestsAgent (pattern Singleton).
	 * </p>
	 * 
	 * @return the instance of the GuestsAgent
	 */
	public static GuestsAgent getInstance() {
		if (instance == null) {
			instance = new GuestsAgent("guests_agent");
		}

		return instance;
	}

	/**
	 * <p>
	 * Creates a new {@link GuestsAgent}.
	 * </p>
	 * 
	 * @param myId
	 *            the identifier of the agent
	 */
	private GuestsAgent(final String myId) {
		super(myId);

		locMap = new HashMap<String, String>();
		ipMap = new HashMap<String, String>();

		try {
			server = new ServerSocket(PORT);
			spy("TCP/IP address: " + server.getInetAddress() + ":" + PORT);
			// TODO start();
		} catch (Exception ex) {
			LogFactory.getLog(GuestsAgent.class).error(
					"Cannot connect GuestsAgent to network", ex);
		}

	}

	@Override
	protected void execute() {
		while (isRunning()) {
			try {
				Socket socket = server.accept();
				spy("Connection accepted from: " + socket.getInetAddress());
				new RequestHandler(locMap, ipMap, socket).start();

			} catch (Exception ex) {
				LogFactory.getLog(GuestsAgent.class).warn(
						"Exception on client accept: " + ex);
			}

		}
	}

	/**
	 * <p>
	 * Invoked for sending a message to the destination guest.
	 * </p>
	 * 
	 * @param note
	 *            the message to send
	 * @param destination
	 *            the identifier of the destination guest
	 */
	public void sendMessageToGuest(final Message note, final String destination) {

		String destIp = ipMap.get(destination);
		spy("Sending a notification to ip: " + destIp);
		new SendMessageThread(note, destIp).start();

	}

}

/**
 * <p>
 * This class represent the Thread managing a single message received.
 * </p>
 * 
 * @author Michele Morgagni
 * 
 */
class RequestHandler extends SpaceAgent {

	private HashMap<String, String> locMap;
	private HashMap<String, String> ipMap;
	private Socket socket;

	private static int istances = 0;

	/**
	 * Creates a new requestHandler and starts it
	 * 
	 * @param aLocMap
	 *            a reference to the map where to put guests LsaId and their
	 *            location LsaId
	 * @param anIpMap
	 *            a reference to the map where to put guests MAC address and
	 *            their current IP address
	 * @param aSocket
	 *            the socket used for the communication
	 */
	public RequestHandler(final HashMap<String, String> aLocMap,
			final HashMap<String, String> anIpMap, final Socket aSocket) {
		super("request_handler" + istances++);
		locMap = aLocMap;
		ipMap = anIpMap;
		socket = aSocket;
	}

	@Override
	protected void execute() {
		try {
			GuestMessage message = CommunicationUtils.receiveMessage(socket);

			if (message != null) {

				String ipAddress = socket.getInetAddress().getHostAddress();
				ipMap.put(message.macAddress, ipAddress);

				switch (message.type) {
				case Inject:
					// inject:
					spy("INJECT :" + message.content.toString());

					// creation of the localization LSA
					Property[] properties = new Property[5];
					properties[0] = new Property("type", new SetPropertyValue(
							"localization"));
					properties[1] = new Property("signal",
							new SetPropertyValue("" + (1.0 * message.signal)));
					properties[2] = new Property("orientation1",
							new SetPropertyValue("" + message.orientation[0]));
					properties[3] = new Property("orientation2",
							new SetPropertyValue("" + message.orientation[1]));
					properties[4] = new Property("orientation3",
							new SetPropertyValue("" + message.orientation[2]));
					Content contentLoc = new JavaLsaContent(properties);

					// injecting localization LSA
					LsaId idLoc = doInject(new Lsa(null, contentLoc),
							message.macAddress);

					// binding localization LSA to user LSA
					Property prop = new Property("position",
							new SetPropertyValue(idLoc.toString()));
					message.content.addProperty(prop);

					// injecting user LSA
					LsaId id = doInject(new Lsa(null, message.content),
							message.macAddress);

					// association lsa-lsaLoc
					locMap.put(id.toString(), idLoc.toString());

					// sending back LsaId
					if (!CommunicationUtils.sendString(id.toString(), socket)) {
						spy("Error on Lsaid sending");
					}
					break;
				case Reinject:
					// reinject:
					spy("REINJECT ");

					Property[] properties2 = new Property[5];
					properties2[0] = new Property("type", new SetPropertyValue(
							"localization"));
					properties2[1] = new Property("signal",
							new SetPropertyValue("" + (1.0 * message.signal)));
					properties2[2] = new Property("orientation1",
							new SetPropertyValue("" + message.orientation[0]));
					properties2[3] = new Property("orientation2",
							new SetPropertyValue("" + message.orientation[1]));
					properties2[4] = new Property("orientation3",
							new SetPropertyValue("" + message.orientation[2]));
					Content contentLoc2 = new JavaLsaContent(properties2);

					LsaId idLoc2 = doInject(new Lsa(null, contentLoc2),
							message.macAddress);

					Property prop2 = new Property("position",
							new SetPropertyValue(idLoc2.toString()));
					message.content.addProperty(prop2);

					doInject(new Lsa(message.id, message.content),
							message.macAddress);

					locMap.put(message.id.toString(), idLoc2.toString());

					if (!CommunicationUtils.sendString("ack", socket)) {
						spy("Error on ack sending");
					}
					break;
				case MoveAway:
					// move:
					spy("MOVE Id: " + message.id);

					doRemove(message.id, message.macAddress);
					// SpaceOperation op3=new
					// SpaceOperation(SpaceOperationType.Remove,message.id,null,null);
					// ReactionManager.getInstance().queueOperation(op3, null);
					// SapereOperation op3 = new
					// SapereOperation(SapereOperationType.REMOVE, message.id,
					// null, null, null);
					// SAPEREMiddleware.executeOperation(op3, this, PORT);
					String idLocalization = locMap.get(message.id.toString());

					doRemove(new LsaId(idLocalization), message.macAddress);
					// SpaceOperation op3a=new
					// SpaceOperation(SpaceOperationType.Remove,idLocalization,null,null);
					// ReactionManager.getInstance().queueOperation(op3a, null);
					// SapereOperation op3a = new
					// SapereOperation(SapereOperationType.REMOVE,
					// idLocalization, null, null, null);
					// SAPEREMiddleware.executeOperation(op3a, this, PORT);

					locMap.remove(message.id.toString());

					if (!CommunicationUtils.sendString("ack", socket)) {
						System.out.println("Errore invio string!");
					}
					break;
				case Update:
					// update:
					spy("ricevuta UPDATE Id: " + message.id);

					doUpdate(message.id, message.content.getProperties(),
							message.macAddress);

					if (!CommunicationUtils.sendString("ack", socket)) {
						spy("Error on ack sending");
					}
					break;
				case Refresh:
					// refresh:
					String idLocalization5 = locMap.get(message.id.toString());

					Property[] properties5 = new Property[5];
					properties5[0] = new Property("type", new SetPropertyValue(
							"localization"));
					properties5[1] = new Property("signal",
							new SetPropertyValue("" + (1.0 * message.signal)));
					properties5[2] = new Property("orientation1",
							new SetPropertyValue("" + message.orientation[0]));
					properties5[3] = new Property("orientation2",
							new SetPropertyValue("" + message.orientation[1]));
					properties5[4] = new Property("orientation3",
							new SetPropertyValue("" + message.orientation[2]));
					Content contentLoc5 = new JavaLsaContent(properties5);

					spy("REFRESH: " + contentLoc5.toString());
					doUpdate(new LsaId(idLocalization5),
							contentLoc5.getProperties(), message.macAddress);

					if (!CommunicationUtils.sendString("ack", socket)) {
						spy("Error on ack sending");
					}
					break;
				case Read:
					// read:
					spy("READ Id: " + message.id + " da " + message.macAddress);
					doRead(message.id, new GuestSubscriber(message.macAddress),
							null);
					if (!CommunicationUtils.sendString("ack", socket)) {
						spy("Error on ack sending");
					}
					break;
				case ObserveOne:
					// observeOne:
					spy("OBSERVE ONE Id: " + message.id + " da "
							+ message.macAddress);
					doObserveOne(message.id, new GuestSubscriber(
							message.macAddress), null);
					if (!CommunicationUtils.sendString("ack", socket)) {
						spy("Error on ack sending");
					}
					break;
				case ObserveMany:
					// observeMany:
					spy("OBSERVE MANY Id: " + message.id + " da "
							+ message.macAddress);
					doObserveMany(message.id, new GuestSubscriber(
							message.macAddress), null);
					if (!CommunicationUtils.sendString("ack", socket)) {
						spy("Error on ack sending");
					}
					break;
				case Deregister:
					// deregister:
					spy("DEREGISTER Id: " + message.id + " da "
							+ message.macAddress);
					doDeregister(message.id, new GuestSubscriber(
							message.macAddress), null);
					if (!CommunicationUtils.sendString("ack", socket)) {
						spy("Error on ack sending");
					}
					break;
				default:
					spy("Error: code not valid");
					if (!CommunicationUtils.sendString(
							"error: wrong message code", socket)) {
						spy("Error on error message sending");
					}

				}
			}

			socket.close();

		} catch (Exception ex) {
			System.out.println("Exception on RequestHandler: " + ex);
			ex.printStackTrace();
		}
	}
}

/**
 * This class is used for sending notifications to guests
 * 
 * @author Michele Morgagni
 * 
 */
class SendMessageThread extends Thread {

	// the PORT where the guest is listening for notifications
	private static int port = 5555;

	private Message message;
	private String destination;

	/**
	 * Creates a new SendMessageThread and starts it
	 * 
	 * @param aMessage
	 *            the message to send
	 * @param aDestination
	 *            the ip address of the destination guest
	 */
	public SendMessageThread(final Message aMessage, final String aDestination) {
		this.message = aMessage;
		this.destination = aDestination;
	}

	public void run() {
		try {
			Socket socket = new Socket(destination, port);
			CommunicationUtils.sendMessage(message, socket);
			socket.close();

		} catch (Exception ex) {
			// System.out.println("Exception on SendMessageThread: "+ex);
			// ex.printStackTrace();
		}

	}

}
