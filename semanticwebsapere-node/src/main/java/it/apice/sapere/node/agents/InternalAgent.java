package it.apice.sapere.node.agents;

import it.apice.sapere.node.networking.Message;
import it.apice.sapere.node.networking.obsnotifications.Subscriber;

import java.util.concurrent.PriorityBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * <p>
 * Class that represent an internal agent of the system.
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
public abstract class InternalAgent extends Thread implements SAPEREAgent,
		Subscriber {

	/** Initial queue capacity. */
	private static final transient int QUEUE_CAPACITY = 100;

	/** Input queue. */
	private final PriorityBlockingQueue<Message> input;

	/** Agent id. */
	private final String id;

	/** Running flag. */
	private transient volatile boolean running = true;

	/**
	 * Creates a new Agent with the specified identifier.
	 * 
	 * @param agentId
	 *            identifier for this agent
	 */
	public InternalAgent(final String agentId) {
		if (agentId == null || agentId.equals("")) {
			throw new IllegalArgumentException("Invalid agent-id provided");
		}

		id = agentId;
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
		final Log logger = LogFactory.getLog(InternalAgent.class);
		synchronized (logger) {
			logger.debug("[Agent::" + id + "] " + s);
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
		final Log logger = LogFactory.getLog(InternalAgent.class);
		synchronized (logger) {
			logger.info("[Agent::" + id + "] " + s);
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
		final Log logger = LogFactory.getLog(InternalAgent.class);
		synchronized (logger) {
			logger.warn("[Agent::" + id + "] " + s, cause);
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
		final Log logger = LogFactory.getLog(InternalAgent.class);
		synchronized (logger) {
			logger.error("[Agent::" + id + "] " + s, cause);
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
		final Log logger = LogFactory.getLog(InternalAgent.class);
		synchronized (logger) {
			logger.fatal("[Agent::" + id + "] " + s, cause);
		}

		kill();
	}

	/**
	 * <p>
	 * Getter for the agent identifier.
	 * </p>
	 * 
	 * @return the agent identifier
	 */
	public final String getAgentId() {
		return id;
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
