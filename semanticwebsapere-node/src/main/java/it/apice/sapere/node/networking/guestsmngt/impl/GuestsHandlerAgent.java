package it.apice.sapere.node.networking.guestsmngt.impl;

import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.node.NodeServices;
import it.apice.sapere.api.node.agents.networking.Message;
import it.apice.sapere.api.node.agents.networking.Subscriber;
import it.apice.sapere.api.space.core.CompiledLSA;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.observation.SpaceOperationType;
import it.apice.sapere.node.agents.AbstractSystemAgent;
import it.apice.sapere.node.agents.impl.AbstractSAPEREAgentImpl;
import it.apice.sapere.node.internal.LoggerFactoryImpl;
import it.apice.sapere.node.networking.obsnotif.impl.Notification;
import it.apice.sapere.node.networking.obsnotif.impl.NotifierAgent;
import it.apice.sapere.node.networking.obsnotif.impl.SubscriptionRequest;
import it.apice.sapere.node.networking.obsnotif.impl.SubscriptionType;
import it.apice.sapere.node.networking.utils.impl.CommunicationUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is the main part for the management of guests connected to the
 * node. It receives messages from clients and sends back response, it sends
 * notifications too.
 * 
 * @author Michele Morgagni
 * 
 */
@Deprecated
public final class GuestsHandlerAgent extends AbstractSAPEREAgentImpl {

	/** The PORT of the server. */
	private static final transient int PORT = 4444;

	/** Server socket. */
	private final transient ServerSocket server;

	/** Localization map. */
	private final transient HashMap<String, String> locMap;

	/** MAC-IP map. */
	private final transient HashMap<String, String> ipMap;

	/** Singleton instance. */
	private static transient GuestsHandlerAgent instance;

	/**
	 * <p>
	 * Retrieves the instance of the GuestsHandlerAgent (pattern Singleton).
	 * </p>
	 * 
	 * @return the instance of the GuestsHandlerAgent
	 */
	public static GuestsHandlerAgent getInstance() {
		if (instance == null) {
			instance = new GuestsHandlerAgent();
		}

		return instance;
	}

	/**
	 * <p>
	 * Creates a new {@link GuestsHandlerAgent}.
	 * </p>
	 */
	private GuestsHandlerAgent() {
		super("guests_handler");

		locMap = new HashMap<String, String>();
		ipMap = new HashMap<String, String>();

		try {
			server = new ServerSocket(PORT);
			spy("TCP/IP address: " + server.getLocalSocketAddress());
		} catch (Exception ex) {
			error("Cannot connect GuestsHandlerAgent to network", ex);
			throw new IllegalStateException("Cannot connect to network", ex);
		}

	}

	@Override
	protected void execute() {
		while (isRunning()) {
			try {
				final Socket socket = server.accept();
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
		final String destIp = ipMap.get(destination);
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
class RequestHandler extends AbstractSystemAgent {

	/** Prefix for Synthetic Properties. */
	private static final transient String SYNT_PROPS_PREFIX = "http://"
			+ "www.sapere-project.eu/ontologies/2012/0/sapere-model.owl#";

	/** Type property URI (as String). */
	private static final transient String TYPE_PROP_URI = SYNT_PROPS_PREFIX
			+ "type";

	/** Signal property URI (as String). */
	private static final transient String SIGNAL_PROP_URI = SYNT_PROPS_PREFIX
			+ "signal";

	/** Location individual URI (as String). */
	private static final transient String LOCAL_INDIV_URI = SYNT_PROPS_PREFIX
			+ "location";

	/** Orientation 1 property URI (as String). */
	private static final transient String OR1_PROP_URI = SYNT_PROPS_PREFIX
			+ "orientation1";

	/** Orientation 2 property URI (as String). */
	private static final transient String OR2_PROP_URI = SYNT_PROPS_PREFIX
			+ "orientation2";

	/** Orientation 3 property URI (as String). */
	private static final transient String OR3_PROP_URI = SYNT_PROPS_PREFIX
			+ "orientation3";

	/** Position property URI (as String). */
	private static final transient String POS_PROP_URI = SYNT_PROPS_PREFIX
			+ "position";

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
	protected final void behaviour(final NodeServices services) {
		try {
			final GuestMessage message = CommunicationUtils
					.receiveMessage(socket);

			if (message != null) {
				final String ipAddress = socket.getInetAddress()
						.getHostAddress();
				ipMap.put(message.getMacAddress(), ipAddress);

				switch (message.getType()) {
				case INJECT:
					handleInject(message, services);
					break;
				case RE_INJECT:
					handleReInject(message, services);
					break;
				case MOVE_AWAY:
					handleMove(message, services);
					break;
				case UPDATE:
					handleUpdate(message, services);
					break;
				case REFRESH:
					handleRefresh(message, services);
					break;
				case READ:
					handleRead(message, services);
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
			LoggerFactoryImpl.getInstance().getLogger(RequestHandler.class)
			.warn("Exception on RequestHandler ", ex);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				LoggerFactoryImpl.getInstance().getLogger(RequestHandler.class)
						.warn("Cannot close the socket", e);
			}
		}
	}

	/**
	 * @param message
	 *            The message to be handled
	 */
	@SuppressWarnings("deprecation")
	private void handleCancelSubscription(final GuestMessage message) {
		spy("CANCEL REGISTRATION Id: " + message.getId() + " from "
				+ message.getMacAddress());

		// TODO This type of access should be deprecated
		NotifierAgent.getInstance().sendMessage(
				new SubscriptionRequest(message.getId(),
						SubscriptionType.CANCEL_SUBSCRIPTION,
						new GuestSubscriber(message.getMacAddress())));
		if (!CommunicationUtils.sendString("ack", socket)) {
			spy("Error on ack sending");
		}
	}

	/**
	 * @param message
	 *            The message to be handled
	 */
	@SuppressWarnings("deprecation")
	private void handleSubscription(final GuestMessage message) {
		spy("OBSERVE PERMANENT_SUBSCRIPTION Id: " + message.getId() + " from "
				+ message.getMacAddress());

		// TODO This type of access should be deprecated
		NotifierAgent.getInstance().sendMessage(
				new SubscriptionRequest(message.getId(),
						SubscriptionType.PERMANENT_SUBSCRIPTION,
						new GuestSubscriber(message.getMacAddress())));

		if (!CommunicationUtils.sendString("ack", socket)) {
			spy("Error on ack sending");
		}
	}

	/**
	 * @param message
	 *            The message to be handled
	 */
	@SuppressWarnings("deprecation")
	private void handleOnceSubscription(final GuestMessage message) {
		spy("OBSERVE ONE_TIME_SUBSCRIPTION Id: " + message.getId() + " from "
				+ message.getMacAddress());

		// TODO This type of access should be deprecated
		NotifierAgent.getInstance().sendMessage(
				new SubscriptionRequest(message.getId(),
						SubscriptionType.ONE_TIME_SUBSCRIPTION,
						new GuestSubscriber(message.getMacAddress())));

		if (!CommunicationUtils.sendString("ack", socket)) {
			spy("Error on ack sending");
		}
	}

	/**
	 * @param message
	 *            The message to be handled
	 * @param services
	 *            References to node's services
	 */
	@SuppressWarnings({ "rawtypes" })
	private void handleRead(final GuestMessage message,
			final NodeServices services) {
		final LSAspaceCore space = services.getLSAspace();
		spy("READ Id: " + message.getId() + " , requestor: "
				+ message.getMacAddress());

		final Subscriber requestor = new GuestSubscriber(
				message.getMacAddress());

		space.beginRead();
		try {
			final CompiledLSA<?> lsa = space.read(message.getId());
			requestor.sendMessage(new Notification(
					SpaceOperationType.AGENT_READ, lsa));

			if (!CommunicationUtils.sendString("ack", socket)) {
				spy("Error on ack sending");
			}
		} catch (SAPEREException e) {
			error("Cannot read the LSA", e);
		} finally {
			space.done();
		}
	}

	/**
	 * @param message
	 *            The message to be handled
	 * @param services
	 *            References to node's services
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void handleRefresh(final GuestMessage message,
			final NodeServices services) {
		spy("REFRESH: localization of " + message.getMacAddress());

		final LSAspaceCore space = services.getLSAspace();
		final CompiledLSA lsa = createGuestInfoLSA(message, services);

		space.beginWrite();
		try {
			services.getLSAspace().update(lsa);

			if (!CommunicationUtils.sendString("ack", socket)) {
				spy("Error on ack sending");
			}
		} catch (SAPEREException e) {
			error("Cannot refresh guest's information", e);
		} finally {
			space.done();
		}

	}

	/**
	 * @param message
	 *            The message to be handled
	 * @param services
	 *            References to node's services
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void handleUpdate(final GuestMessage message,
			final NodeServices services) {
		spy("UPDATE Id: " + message.getId());
		final LSAspaceCore space = services.getLSAspace();
		final CompiledLSA lsa = services.getLSACompiler().parse(
				message.getLSA(), RDFFormat.RDF_XML);

		space.beginWrite();
		try {
			services.getLSAspace().update(lsa);

			if (!CommunicationUtils.sendString("ack", socket)) {
				spy("Error on ack sending");
			}
		} catch (SAPEREException e) {
			error("Cannot update LSA", e);
		} finally {
			space.done();
		}
	}

	/**
	 * @param message
	 *            The message to be handled
	 * @param services
	 *            References to node's services
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void handleMove(final GuestMessage message,
			final NodeServices services) {
		final LSAspaceCore space = services.getLSAspace();

		spy("MOVE Id: " + message.getId());

		space.beginWrite();
		try {
			final CompiledLSA lsa1 = space.read(message.getId());
			space.remove(lsa1);

			final String idLoc = locMap.get(message.getId().toString());
			final CompiledLSA lsa2 = space.read(services
					.getLSAFactory().createLSAid(URI.create(idLoc)));
			space.remove(lsa2);
		} catch (SAPEREException e) {
			error("Cannot move guest", e);
		} finally {
			space.done();
		}

		locMap.remove(message.getId().toString());

		if (!CommunicationUtils.sendString("ack", socket)) {
			warn("Cannot send acknowledge", null);
		}
	}

	/**
	 * @param message
	 *            The message to be handled
	 * @param services
	 *            References to node's services
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void handleReInject(final GuestMessage message,
			final NodeServices services) {
		spy("RE-INJECT ");

		final CompiledLSA locLsa = createGuestInfoLSA(message, services);
		final LSAid idLoc = locLsa.getLSAid();

		final LSAspaceCore space = services.getLSAspace();

		space.beginWrite();
		try {
			// Removing what there was before (avoiding duplicate LSA-id)
			try {
				final CompiledLSA readLsa = space.read(locLsa
						.getLSAid());
				space.remove(readLsa);
			} catch (SAPEREException e) {
				spy("Re-injecting something that is not in the space");
			}

			space.inject(locLsa);

			final CompiledLSA recvLsa = services.getLSACompiler().parse(
					message.getLSA(), RDFFormat.RDF_XML);
			recvLsa.assertProperty(URI.create(POS_PROP_URI), idLoc.getId());
			space.inject(recvLsa);

			final LSAid recvId = recvLsa.getLSAid();
			locMap.put(recvId.toString(), idLoc.toString());
		} catch (SAPEREException e) {
			error("Cannot inject the LSA again", e);
		} finally {
			space.done();
		}

		if (!CommunicationUtils.sendString("ack", socket)) {
			spy("Error on ack sending");
		}
	}

	/**
	 * @param message
	 *            The message to be handled
	 * @param services
	 *            References to node's services
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void handleInject(final GuestMessage message,
			final NodeServices services) {
		spy("INJECT :" + message.getLSA());

		final CompiledLSA locLsa = createGuestInfoLSA(message, services);
		final LSAid idLoc = locLsa.getLSAid();

		final LSAspaceCore space = services.getLSAspace();

		space.beginWrite();
		try {
			space.inject(locLsa);

			final CompiledLSA recvLsa = services.getLSACompiler().parse(
					message.getLSA(), RDFFormat.RDF_XML);
			recvLsa.assertProperty(URI.create(POS_PROP_URI), idLoc.getId());
			space.inject(recvLsa);

			final LSAid recvId = recvLsa.getLSAid();
			locMap.put(recvId.toString(), idLoc.toString());

			if (!CommunicationUtils.sendString(recvId.toString(), socket)) {
				spy("Error on Lsaid sending");
			}
		} catch (SAPEREException e) {
			error("Cannot inject LSA or relative localization info", e);
		} finally {
			space.done();
		}
	}

	/**
	 * <p>
	 * Creates a GuestInfo LSA from information contained in the provided
	 * message.
	 * </p>
	 * 
	 * @param message
	 *            Message from the guest
	 * @param services
	 *            Reference to node's services
	 * @return The GuestInfo LSA
	 */
	@SuppressWarnings({ "rawtypes" })
	private CompiledLSA createGuestInfoLSA(final GuestMessage message,
			final NodeServices services) {
		final PrivilegedLSAFactory fact = services.getLSAFactory();

		LSAid idLoc = fact.createLSAid(URI.create(locMap.get(message.getId()
				.toString())));
		if (idLoc == null) {
			idLoc = fact.createLSAid();
		}

		final CompiledLSA lsa = services.getLSACompiler().compile(
				fact.createLSA());

		lsa.assertProperty(URI.create(TYPE_PROP_URI),
				URI.create(LOCAL_INDIV_URI));
		lsa.assertProperty(URI.create(SIGNAL_PROP_URI),
				"" + (double) message.getSignal());
		lsa.assertProperty(URI.create(OR1_PROP_URI),
				"" + message.getOrientation()[0]);
		lsa.assertProperty(URI.create(OR2_PROP_URI),
				"" + message.getOrientation()[1]);
		lsa.assertProperty(URI.create(OR3_PROP_URI),
				"" + message.getOrientation()[2]);
		return lsa;
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
		message = aMessage;
		destination = aDestination;
	}

	@Override
	public void run() {
		Socket socket = null;
		try {
			socket = new Socket(destination, SERVER_PORT);
			CommunicationUtils.sendMessage(message, socket);
		} catch (Exception ex) {
			LoggerFactoryImpl.getInstance().getLogger(SendMessageThread.class)
					.warn("Error while sending the message", ex);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					LoggerFactoryImpl.getInstance()
							.getLogger(SendMessageThread.class)
							.error("Cannot close sending socket", e);
				}
			}
		}

	}

}
