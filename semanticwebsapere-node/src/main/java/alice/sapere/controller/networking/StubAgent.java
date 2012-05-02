package alice.sapere.controller.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.commons.logging.LogFactory;

import alice.sapere.controller.core.InternalAgent;
import alice.sapere.model.communication.Message;

/**
 * <p>
 * This class models an agent, executed by the {@link SAPEREMiddleware}, that
 * opens a communication channel in order to send a message.
 * </p>
 * 
 */
public class StubAgent implements Runnable {

	/** The agent that run the operation. */
	private InternalAgent service;

	/** The operation to be executed. */
	private SapereOperation op;

	/** The TCP port to be used. */
	private int port;

	/**
	 * <p>
	 * Builds a new {@link StubAgent}.
	 * </p>
	 * 
	 * @param anOp
	 *            The operation to be executed
	 * @param aService
	 *            The agent that run the operation
	 * @param aPort
	 *            The TCP port to be used
	 */
	public StubAgent(final SapereOperation anOp, final InternalAgent aService,
			final int aPort) {
		service = aService;
		op = anOp;
		port = aPort;
	}

	@Override
	public final void run() {
		Socket sock = null;
		try {
			sock = new Socket("localhost", port);
			final ObjectOutputStream oos = new ObjectOutputStream(
					sock.getOutputStream());
			oos.writeObject(op);
			final ObjectInputStream ois = new ObjectInputStream(
					sock.getInputStream());
			final Message note = (Message) ois.readObject();
			service.sendMessage(note);
		} catch (Exception e) {
			LogFactory.getLog(StubAgent.class).warn(
					"Problems executing a SAPEREOperation", e);
		} finally {
			if (sock != null) {
				try {
					sock.close();
				} catch (IOException e) {
					LogFactory.getLog(StubAgent.class).warn(
							"Cannot close socket used for SAPEREOperation", e);
				}
			}
		}
	}
}
