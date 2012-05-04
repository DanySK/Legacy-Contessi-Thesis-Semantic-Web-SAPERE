package it.apice.sapere.node.networking.guestsmngt;

import it.apice.sapere.node.agents.InternalAgent;
import it.apice.sapere.node.networking.CommunicationUtils;
import it.apice.sapere.node.networking.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.LogFactory;

/**
 * This class is the main part for the management of guests connected to the
 * node. It receives messages from clients and sends back response, it sends
 * notifications too.
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
			error("Cannot connect GuestsAgent to network", ex);
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
				warn("Exception on client accept", ex);
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
	public void sendMessageToGuest(final Message note, 
			final String destination) {
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
class RequestHandler extends InternalAgent {

	/** GeoLoc Map. */
	private HashMap<String, String> locMap;

	/** Network Map. */
	private HashMap<String, String> ipMap;

	/** The communication socket. */
	private Socket socket;

	/** Instances counter (for agent naming). */
	private static AtomicInteger instances = new AtomicInteger(0);

	/**
	 * <p>
	 * Creates a new requestHandler and starts it.
	 * </p>
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
		super("request_handler" + instances.getAndIncrement());

		if (aLocMap == null) {
			throw new IllegalArgumentException("Invalid loc map provided");
		}

		if (anIpMap == null) {
			throw new IllegalArgumentException("Invalid ip map provided");
		}

		if (aSocket == null) {
			throw new IllegalArgumentException("Invalid socket provided");
		}

		locMap = aLocMap;
		ipMap = anIpMap;
		socket = aSocket;
	}

	@Override
	protected final void execute() {
		try {
			GuestMessage message = CommunicationUtils.receiveMessage(socket);

			if (message != null) {
				String ipAddress = socket.getInetAddress().getHostAddress();
				ipMap.put(message.getMacAddress(), ipAddress);

				switch (message.getType()) {
				case INJECT:
					handleInject(message);
					break;
				case RE_INJECT:
					handleReInject(message);
					break;
				case MOVE_AWAY:
					handleMove(message);
					break;
				case UPDATE:
					handleUpdate(message);
					break;
				case REFRESH:
					handleRefresh(message);
					break;
				case READ:
					handleRead(message);
					break;
				case OBSERVE_ONCE:
					handleOnceSubscription(message);
					break;
				case OBSERVE:
					handleSubscription(message);
					break;
				case CANCEL_SUBSCRIPTION:
					handleCancelSubscription(message);
					break;
				default:
					spy("Error: invalid message type");
					if (!CommunicationUtils.sendString(
							"error: wrong message code", socket)) {
						spy("Cannot send error message");
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("Exception on RequestHandler: " + ex);
			ex.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				LogFactory.getLog(RequestHandler.class).warn(
						"Cannot close the socket", e);
			}
		}
	}

	/**
	 * @param message
	 */
	private void handleCancelSubscription(final GuestMessage message) {
		spy("CANCEL REGISTRATION Id: " + message.getId() + " from "
				+ message.getMacAddress());
//		doDeregister(message.getId(),
//				new GuestSubscriber(message.getMacAddress()), null);
		if (!CommunicationUtils.sendString("ack", socket)) {
			spy("Error on ack sending");
		}
	}

	/**
	 * @param message
	 */
	private void handleSubscription(final GuestMessage message) {
		spy("OBSERVE PERMANENT_SUBSCRIPTION Id: " + message.getId() + " from "
				+ message.getMacAddress());
//		doObserveMany(message.getId(),
//				new GuestSubscriber(message.getMacAddress()), null);
		if (!CommunicationUtils.sendString("ack", socket)) {
			spy("Error on ack sending");
		}
	}

	/**
	 * @param message
	 */
	private void handleOnceSubscription(final GuestMessage message) {
		spy("OBSERVE ONE_TIME_SUBSCRIPTION Id: " + message.getId() + " from "
				+ message.getMacAddress());
//		doObserveOne(message.getId(),
//				new GuestSubscriber(message.getMacAddress()), null);
		if (!CommunicationUtils.sendString("ack", socket)) {
			spy("Error on ack sending");
		}
	}

	/**
	 * @param message
	 */
	private void handleRead(final GuestMessage message) {
		spy("READ Id: " + message.getId() + " from " + message.getMacAddress());
//		doRead(message.getId(), new GuestSubscriber(message.getMacAddress()),
//				null);
		if (!CommunicationUtils.sendString("ack", socket)) {
			spy("Error on ack sending");
		}
	}

	/**
	 * @param message
	 */
	private void handleRefresh(final GuestMessage message) {
//		String idLocalization5 = locMap.get(message.getId().toString());

//		Property[] properties5 = new Property[5];
//		properties5[0] = new Property("type", new SetPropertyValue(
//				"localization"));
//		properties5[1] = new Property("signal", new SetPropertyValue(""
//				+ (1.0 * message.getSignal())));
//		properties5[2] = new Property("orientation1", new SetPropertyValue(""
//				+ message.getOrientation()[0]));
//		properties5[3] = new Property("orientation2", new SetPropertyValue(""
//				+ message.getOrientation()[1]));
//		properties5[4] = new Property("orientation3", new SetPropertyValue(""
//				+ message.getOrientation()[2]));
//		Content contentLoc5 = new JavaLsaContent(properties5);

//		spy("REFRESH: " + contentLoc5.toString());
//		doUpdate(new LsaId(idLocalization5), contentLoc5.getProperties(),
//				message.getMacAddress());

		if (!CommunicationUtils.sendString("ack", socket)) {
			spy("Error on ack sending");
		}
	}

	/**
	 * @param message
	 */
	private void handleUpdate(final GuestMessage message) {
		spy("UPDATE Id: " + message.getId());

//		doUpdate(message.getId(), message.content.getProperties(),
//				message.getMacAddress());

		if (!CommunicationUtils.sendString("ack", socket)) {
			spy("Error on ack sending");
		}
	}

	/**
	 * @param message
	 */
	private void handleMove(final GuestMessage message) {
		spy("MOVE Id: " + message.getId());

//		doRemove(message.getId(), message.getMacAddress());
//		String idLocalization = locMap.get(message.getId().toString());
//
//		doRemove(new LsaId(idLocalization), message.getMacAddress());

		locMap.remove(message.getId().toString());

		if (!CommunicationUtils.sendString("ack", socket)) {
			System.out.println("Errore invio string!");
		}
	}

	/**
	 * @param message
	 */
	private void handleReInject(final GuestMessage message) {
		spy("REINJECT ");

//		Property[] properties2 = new Property[5];
//		properties2[0] = new Property("type", new SetPropertyValue(
//				"localization"));
//		properties2[1] = new Property("signal", new SetPropertyValue(""
//				+ (1.0 * message.getSignal())));
//		properties2[2] = new Property("orientation1", new SetPropertyValue(""
//				+ message.getOrientation()[0]));
//		properties2[3] = new Property("orientation2", new SetPropertyValue(""
//				+ message.getOrientation()[1]));
//		properties2[4] = new Property("orientation3", new SetPropertyValue(""
//				+ message.getOrientation()[2]));
//		Content contentLoc2 = new JavaLsaContent(properties2);
//
//		LsaId idLoc2 = doInject(new Lsa(null, contentLoc2),
//				message.getMacAddress());
//
//		Property prop2 = new Property("position", new SetPropertyValue(
//				idLoc2.toString()));
//		message.content.addProperty(prop2);
//
//		doInject(new Lsa(message.getId(), message.content),
//				message.getMacAddress());

//		locMap.put(message.getId().toString(), idLoc2.toString());

		if (!CommunicationUtils.sendString("ack", socket)) {
			spy("Error on ack sending");
		}
	}

	/**
	 * @param message
	 */
	private void handleInject(final GuestMessage message) {
		spy("INJECT :" + message.getLSA());

		// creation of the localization LSA
//		Property[] properties = new Property[5];
//		properties[0] = new Property("type", new SetPropertyValue(
//				"localization"));
//		properties[1] = new Property("signal", new SetPropertyValue(""
//				+ (1.0 * message.getSignal())));
//		properties[2] = new Property("orientation1", new SetPropertyValue(""
//				+ message.getOrientation()[0]));
//		properties[3] = new Property("orientation2", new SetPropertyValue(""
//				+ message.getOrientation()[1]));
//		properties[4] = new Property("orientation3", new SetPropertyValue(""
//				+ message.getOrientation()[2]));
//		Content contentLoc = new JavaLsaContent(properties);

		// injecting localization LSA
//		LsaId idLoc = doInject(new Lsa(null, contentLoc),
//				message.getMacAddress());

		// binding localization LSA to user LSA
//		Property prop = new Property("position", new SetPropertyValue(
//				idLoc.toString()));
//		message.content.addProperty(prop);

		// injecting user LSA
//		LsaId id = doInject(new Lsa(null, message.content),
//				message.getMacAddress());

		// association lsa-lsaLoc
//		locMap.put(id.toString(), idLoc.toString());

		// sending back LsaId
//		if (!CommunicationUtils.sendString(id.toString(), socket)) {
//			spy("Error on Lsaid sending");
//		}
	}
}

/**
 * <p>
 * This class is used for sending notifications to guests.
 * </p>
 * 
 * @author Michele Morgagni
 * 
 */
class SendMessageThread extends Thread {

	/** The port on which the server is listening. */
	private static final transient int SERVER_PORT = 5555;

	/** The message to be sent. */
	private Message message;

	/** The destination. */
	private String destination;

	/**
	 * <p>
	 * Creates a new SendMessageThread and starts it.
	 * </p>
	 * 
	 * @param aMessage
	 *            the message to send
	 * @param aDestination
	 *            the ip address of the destination guest
	 */
	public SendMessageThread(final Message aMessage, 
			final String aDestination) {
		this.message = aMessage;
		this.destination = aDestination;
	}

	@Override
	public void run() {
		try {
			Socket socket = new Socket(destination, SERVER_PORT);
			CommunicationUtils.sendMessage(message, socket);
			socket.close();

		} catch (Exception ex) {
			LogFactory.getLog(SendMessageThread.class).warn(
					"Error while sending the message", ex);
		}

	}

}
