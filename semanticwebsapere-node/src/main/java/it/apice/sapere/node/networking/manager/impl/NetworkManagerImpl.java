package it.apice.sapere.node.networking.manager.impl;

import it.apice.api.node.logging.impl.LoggerFactoryImpl;
import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.node.NodeServices;
import it.apice.sapere.api.space.core.CompiledLSA;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.node.agents.AbstractSystemAgent;
import it.apice.sapere.node.networking.impl.NodeMessage;
import it.apice.sapere.node.networking.impl.NodeMessageType;
import it.apice.sapere.node.networking.manager.NetworkManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * This class realizes the {@link NetworkManager} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class NetworkManagerImpl extends AbstractSystemAgent implements
		NetworkManager {

	/** Prefix for Synthetic Properties. */
	private static final transient String SYNT_PROPS_PREFIX = "http://"
			+ "www.sapere-project.eu/ontologies/2012/0/sapere-model.owl#";

	/** LSA's location property URI. */
	private static final transient URI LOCATION_PROP = URI
			.create(SYNT_PROPS_PREFIX + "location");

	/** Local location property value URI. */
	private static final transient URI LOCAL_VAL = URI.create(SYNT_PROPS_PREFIX
			+ "local");

	/** Friendly Id property URI (as String). */
	private static final transient String ID_PROP_URI = SYNT_PROPS_PREFIX
			+ "name";

	/** Type property URI (as String). */
	private static final transient String TYPE_PROP_URI = SYNT_PROPS_PREFIX
			+ "type";

	/** Neighbour individual URI (as String). */
	private static final transient String NEIGHB_INDIV_URI = SYNT_PROPS_PREFIX
			+ "neighbour";

	/** Reference to LSA-space. */
	@SuppressWarnings("rawtypes")
	private transient LSAspaceCore space;

	/** Reference to LSACompiler. */
	@SuppressWarnings("rawtypes")
	private transient LSACompiler comp;

	/** Table of all reachable nodes, by friendly Ids. */
	private final transient Map<String, InetSocketAddress> neighbIds = 
			new HashMap<String, InetSocketAddress>();

	/** Table of all reachable nodes, by LSA-ids. */
	private final transient Map<URI, InetSocketAddress> neighLIds = 
			new HashMap<URI, InetSocketAddress>();

	/** Executor for network stuffs. */
	private final transient ExecutorService exec = Executors
			.newSingleThreadExecutor();

	/** Mutual exclusion lock. */
	private final transient Lock mutex = new ReentrantLock();

	/** Singleton instance. */
	private static final transient NetworkManagerImpl INSTANCE = 
			new NetworkManagerImpl();

	/** The port on which diffusion server will listen (default=20020). */
	private static transient int incomingPort = 20020;

	/**
	 * <p>
	 * Builds a new {@link NetworkManagerImpl}.
	 * </p>
	 */
	public NetworkManagerImpl() {
		super("network_manager");
	}

	@Override
	public void diffuse(final Object to, final NodeMessage msg) {
		final InetSocketAddress dest = retrieveSockAddr(to);
		if (dest == null) {
			throw new IllegalArgumentException("Unknown neighbor");
		}

		LoggerFactoryImpl
				.getInstance()
				.getLogger(NetworkManager.class)
				.spy(String.format(
						"Sending DIFFUSE message to %s (LSA-id: %s; dest: %s)",
						to, msg.getOperation().getLSAid(), dest));

		exec.execute(new Runnable() {

			@Override
			public void run() {
				try {
					doDiffuse(dest, msg);
				} catch (IOException e) {
					LoggerFactoryImpl.getInstance()
							.getLogger(NetworkManager.class)
							.warn("Cannot complete diffusion", e);
				}
			}
		});
	}

	/**
	 * <p>
	 * Retrieves the address (IP:Port) given a destination identifier (name, or
	 * LSA-id).
	 * </p>
	 * <p>
	 * This method tries to deduce what kind of identifier has been provided. If
	 * null is returned then the id is not valid!
	 * </p>
	 * 
	 * @param to
	 *            Identifier (name == id or LSA-id of the neighbour LSA)
	 * @return The destination address (IP:Port)
	 */
	private InetSocketAddress retrieveSockAddr(final Object to) {
		mutex.lock();
		try {
			if (to instanceof LSAid) {
				return neighLIds.get(((LSAid) to).getId());
			} else if (to instanceof URI) {
				return neighbIds.get((URI) to);
			} else if (to instanceof String) {
				return neighbIds.get(to);
			}

			return neighbIds.get(URI.create(to.toString()));
		} finally {
			mutex.unlock();
		}
	}

	/**
	 * <p>
	 * Handles diffusion.
	 * </p>
	 * 
	 * @param dest
	 *            Destination
	 * @param msg
	 *            Message
	 * @throws IOException
	 *             Cannot open a connection through the neighbor
	 */
	private void doDiffuse(final InetSocketAddress dest, final NodeMessage msg)
			throws IOException {
		final Socket sock = new Socket();
		ObjectOutputStream out = null;
		try {
			sock.connect(dest);
			out = new ObjectOutputStream(sock.getOutputStream());
			out.writeObject(msg);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}

			sock.close();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean register(final String id, final InetSocketAddress addr)
			throws SAPEREException {
		mutex.lock();
		try {
			if (neighbIds.put(id, addr) == null) {
				final CompiledLSA neighbLSA = comp.create();
				neighbLSA.assertProperty(URI.create(TYPE_PROP_URI),
						URI.create(NEIGHB_INDIV_URI));
				neighbLSA.assertProperty(URI.create(ID_PROP_URI), id);

				space.beginWrite();
				try {
					space.inject(neighbLSA);
				} finally {
					space.done();
				}

				neighLIds.put(neighbLSA.getLSAid().getId(), addr);
				spy(String.format("Registered: <%s, %s, %s>", id, addr,
						neighbLSA.getLSAid()));
				return true;
			}

			return false;
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public void loadTable(final File config) {
		neighbIds.clear();

		final Properties props = new Properties();
		if (!config.exists() || !config.canRead() || !config.isFile()) {
			throw new IllegalArgumentException("Invalid file specified");
		}

		InputStream in = null;
		try {
			try {
				in = new FileInputStream(config);
				props.loadFromXML(in);

				for (Entry<Object, Object> entry : props.entrySet()) {
					register(entry.getKey().toString(),
							parseAddressAndPort(entry.getValue().toString()));
				}
			} finally {
				if (in != null) {
					in.close();
				}
			}
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}

	/**
	 * <p>
	 * Parses the String in order to extract IP and Port of the destination to
	 * be registered.
	 * </p>
	 * 
	 * @param string
	 *            The string to be parsed
	 * @return The Socket Address of the destination
	 */
	private InetSocketAddress parseAddressAndPort(final String string) {
		try {
			final String[] parts = string.replace(" ", "").split(":");
			return new InetSocketAddress(InetAddress.getByName(parts[0]),
					Integer.parseInt(parts[1]));
		} catch (Exception ex) {
			throw new IllegalArgumentException(
					"Cannot extract destination address (IP:Port). "
							+ "Invalid string format");
		}
	}

	@Override
	protected void behaviour(final NodeServices services) {
		space = services.getLSAspace();
		comp = services.getLSACompiler();

		ServerSocket sock = null;
		try {
			sock = new ServerSocket(incomingPort);

			Socket conn = null;
			ObjectInputStream in = null;
			while (isRunning()) {
				try {
					conn = sock.accept();
					in = new ObjectInputStream(conn.getInputStream());

					final Object obj = in.readObject();
					if (obj instanceof NodeMessage) {
						final NodeMessage msg = (NodeMessage) obj;

						if (msg.getType().equals(NodeMessageType.DIFFUSE)) {
							handleDiffusion(services, msg);
						} else {
							LoggerFactoryImpl
									.getInstance()
									.getLogger(NetworkManagerImpl.class)
									.warn(String.format("Message DROPPED: "
											+ "unsupported type (%s)",
											msg.getType()), null);
						}
					}
				} finally {
					if (conn != null) {
						conn.close();
					}

					if (in != null) {
						in.close();
					}
				}
			}
		} catch (IOException ex) {
			LoggerFactoryImpl.getInstance().getLogger(NetworkManagerImpl.class)
					.error("Cannot open an incoming connection", ex);
		} catch (ClassNotFoundException ex) {
			LoggerFactoryImpl.getInstance().getLogger(NetworkManagerImpl.class)
					.error("Cannot deserialize message", ex);
		} finally {
			if (sock != null) {
				try {
					sock.close();
				} catch (IOException e) {
					LoggerFactoryImpl.getInstance()
							.getLogger(NetworkManagerImpl.class)
							.error("Cannot close server socket", e);
				}
			}
		}
	}

	/**
	 * <p>
	 * Inject the diffused (received) LSA, once extracted, in the LSA-space.
	 * </p>
	 * 
	 * @param services
	 *            Node services reference
	 * @param message
	 *            The diffused message
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void handleDiffusion(final NodeServices services,
			final NodeMessage message) {
		final LSAspaceCore lsaSpace = services.getLSAspace();
		final LSACompiler lsaComp = services.getLSACompiler();

		lsaSpace.beginWrite();
		try {
			final CompiledLSA<?> lsa = lsaComp.parse(message.getOperation()
					.getLSA(), RDFFormat.RDF_XML);
			lsa.clearProperty(LOCATION_PROP);
			lsa.assertProperty(LOCATION_PROP, LOCAL_VAL);

			lsaSpace.inject(lsa);
			spy("Received diffusion " + message.getOperation().getLSAid());
		} catch (SAPEREException e) {
			error("Cannot receive diffusion", e);
		} finally {
			lsaSpace.done();
		}
	}

	@Override
	public int countNeighbours() {
		return neighbIds.size();
	}

	/**
	 * <p>
	 * Sets the port on which the Diffusion server will listen for incoming
	 * connections.
	 * </p>
	 * <p>
	 * This method should be used before running the agent otherwise it is
	 * useless.
	 * </p>
	 * 
	 * @param port
	 *            The port on which listening
	 */
	public static void setIncomingPort(final int port) {
		if (port <= 1024) {
			throw new IllegalArgumentException(
					"Invalid port: it should be greater than 1024");
		}

		incomingPort = port;
	}

	/**
	 * <p>
	 * Retrieves the the port used for incoming connections.
	 * </p>
	 * 
	 * @return The port for relocation service
	 */
	public static int getIncomingPort() {
		return incomingPort;
	}

	/**
	 * <p>
	 * Singleton method.
	 * </p>
	 * 
	 * @return The singleton instance
	 */
	public static NetworkManagerImpl getInstance() {
		return INSTANCE;
	}
}
