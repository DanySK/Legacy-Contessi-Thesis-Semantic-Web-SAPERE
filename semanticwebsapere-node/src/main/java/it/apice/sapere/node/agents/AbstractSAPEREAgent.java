package it.apice.sapere.node.agents;

import it.apice.sapere.node.internal.LoggerFactoryImpl;
import it.apice.sapere.node.internal.NodeServicesImpl;
import it.apice.sapere.node.networking.impl.Message;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.log4j.Logger;

/**
 * <p>
 * Class that represent an agent of the system.
 * </p>
 * 
 * <p>
 * After the integration with an RDF-based subsystem some modifications
 * occurred:
 * </p>
 * <ul>
 * <li>Substitution of Logger entity with commons-logging logger</li>
 * <li>Javadoc completed</li>
 * </ul>
 * 
 * @author Marco Santarelli
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractSAPEREAgent extends Thread 
		implements SAPEREAgent {

	/** Initial queue capacity. */
	private static final transient int QUEUE_CAPACITY = 100;

	/** Input queue. */
	private final PriorityBlockingQueue<Message> input;

	/** Agent id. */
	private final String id;

	/** Agent URI. */
	private final URI agentURI;

	/** Running flag. */
	private transient volatile boolean running = true;

	/**
	 * Creates a new Agent with the specified identifier.
	 * 
	 * @param agentId
	 *            identifier for this agent
	 */
	public AbstractSAPEREAgent(final String agentId) {
		super(agentId);

		if (agentId == null || agentId.equals("")) {
			throw new IllegalArgumentException("Invalid agent-id provided");
		}

		id = agentId;
		spy("intializing...");
		
		try {
			agentURI = new URI(NodeServicesImpl.getInstance().getLSAFactory()
					.getNodeID().replace("#", "/").concat("agents#")
					.concat(agentId));
		} catch (URISyntaxException ex) {
			throw new IllegalArgumentException("Cannot create "
					+ "an agent URI", ex);
		}

		input = new PriorityBlockingQueue<Message>(QUEUE_CAPACITY);
	}

	/**
	 * Sends a message to the agent.
	 * 
	 * @param note
	 *            the message to send
	 */
	public final void sendMessage(final Message note) {
		input.put(note);
	}

	/**
	 * <p>
	 * Sends a debug message to the Logger.
	 * </p>
	 * 
	 * @param s
	 *            the string to be sent
	 */
	protected final void spy(final String s) {
		final Logger logger = LoggerFactoryImpl.getInstance().getLogger(this);
		synchronized (logger) {
			logger.debug(id + "> " + s);
		}
	}

	/**
	 * <p>
	 * Sends a info message to the Logger.
	 * </p>
	 * 
	 * @param s
	 *            the string to be sent
	 */
	protected final void info(final String s) {
		final Logger logger = LoggerFactoryImpl.getInstance().getLogger(this);
		synchronized (logger) {
			logger.info(id + "> " + s);
		}
	}

	/**
	 * <p>
	 * Sends a warning message to the Logger.
	 * </p>
	 * 
	 * @param s
	 *            the string to be sent
	 * @param cause
	 *            Cause of the warning
	 */
	protected final void warn(final String s, final Throwable cause) {
		final Logger logger = LoggerFactoryImpl.getInstance().getLogger(this);
		synchronized (logger) {
			logger.warn(id + "> " + s, cause);
		}
	}

	/**
	 * <p>
	 * Sends a error message to the Logger.
	 * </p>
	 * 
	 * @param s
	 *            the string to be sent
	 * @param cause
	 *            Cause of the error
	 */
	protected final void error(final String s, final Throwable cause) {
		final Logger logger = LoggerFactoryImpl.getInstance().getLogger(this);
		synchronized (logger) {
			logger.error(id + "> " + s, cause);
		}
	}

	/**
	 * <p>
	 * Sends a fatal message to the Logger and kills the Agent.
	 * </p>
	 * 
	 * @param s
	 *            the string to be sent
	 * @param cause
	 *            Cause of the fatal
	 */
	protected final void fatal(final String s, final Throwable cause) {
		final Logger logger = LoggerFactoryImpl.getInstance().getLogger(this);
		synchronized (logger) {
			logger.fatal(id + "> " + s, cause);
		}

		kill();
	}

	@Override
	public final String getLocalAgentId() {
		return id;
	}

	@Override
	public final URI getAgentURI() {
		return agentURI;
	}

	/**
	 * <p>
	 * Retrieves a reference to the queue that handles incoming messages.
	 * </p>
	 * 
	 * @return Incoming messages queue
	 */
	public final PriorityBlockingQueue<Message> getInputQueue() {
		return input;
	}

	@Override
	public final boolean isRunning() {
		return running;
	}

	@Override
	public final void kill() {
		running = true;
		interrupt();
	}

	@Override
	public final void spawn() {
		start();
	}

	@Override
	public final void run() {
		super.run();
		spy("activated");
		try {
			execute();
		} catch (Exception ex) {
			error("Unhandled Exception occurred. Terminating..", ex);
		}
		spy("terminated");
	}

	/**
	 * <p>
	 * Contains Agent's business logic to be executed.
	 * </p>
	 */
	protected abstract void execute();
}
