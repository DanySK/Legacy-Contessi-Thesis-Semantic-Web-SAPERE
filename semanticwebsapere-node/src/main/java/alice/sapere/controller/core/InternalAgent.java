package alice.sapere.controller.core;

import java.util.concurrent.PriorityBlockingQueue;

import org.apache.commons.logging.LogFactory;

import alice.sapere.model.communication.Message;

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
		ISubscriber {

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
		input = new PriorityBlockingQueue<Message>(QUEUE_CAPACITY);
		id = agentId;
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
		LogFactory.getLog(InternalAgent.class)
				.debug("[Agent::" + id + "] " + s);
	}
	
	/**
	 * <p>
	 * Sends an info message to the Logger.
	 * </p>
	 * 
	 * @param s
	 *            the string to be sent
	 */
	protected final void log(final String s) {
		LogFactory.getLog(InternalAgent.class)
				.info("[Agent::" + id + "] " + s);
	}
	
	/**
	 * <p>
	 * Sends a warning message to the Logger.
	 * </p>
	 * 
	 * @param s
	 *            the string to be sent
	 */
	protected final void warn(final String s) {
		LogFactory.getLog(InternalAgent.class)
				.warn("[Agent::" + id + "] " + s);
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
		execute();
		spy("terminated");
	}

	/**
	 * <p>
	 * Contains Agent's business logic to be executed.
	 * </p>
	 */
	protected abstract void execute();
}
