package alice.sapere.controller.networking;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import alice.sapere.controller.core.InternalAgent;

/**
 * <p>
 * Class that represents the SAPEREmiddleware.
 * </p>
 * 
 */
public class SAPEREMiddleware {

	/** Reference to effective executor. */
	private final transient Executor executor = Executors.newCachedThreadPool();

	/** Singleton instance. */
	private static final transient SAPEREMiddleware INSTANCE = 
			new SAPEREMiddleware();

	/**
	 * <p>
	 * Enqueues a communication operation to be executed.
	 * </p>
	 * 
	 * @param op
	 *            The operation to be executed
	 * @param service
	 *            The agent that execute it
	 * @param port
	 *            The port used to execute the operation
	 */
	public final void executeOperation(final SapereOperation op,
			final InternalAgent service, final int port) {
		executor.execute(new StubAgent(op, service, port));
	}

	/**
	 * <p>
	 * Retrieves the singleton instance of {@link SAPEREMiddleware}.
	 * </p>
	 * 
	 * @return Global reference to {@link SAPEREMiddleware}
	 */
	public static SAPEREMiddleware getInstance() {
		return INSTANCE;
	}
}
