package it.apice.sapere.node.networking.utils.impl;

import it.apice.sapere.node.internal.LoggerFactoryImpl;
import it.apice.sapere.node.networking.guestsmngt.impl.GuestMessage;
import it.apice.sapere.node.networking.guestsmngt.impl.GuestMessageType;
import it.apice.sapere.node.networking.impl.Message;
import it.apice.sapere.node.networking.obsnotif.impl.Notification;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * <p>
 * Class for utility static methods.
 * </p>
 * 
 * @author Michele Morgagni
 * 
 */
public final class CommunicationUtils {

	/**
	 * <p>
	 * Private constructor.
	 * </p>
	 */
	private CommunicationUtils() {

	}

	/**
	 * <p>
	 * Receives a message from a guest.
	 * </p>
	 * 
	 * @param socket
	 *            the socket where to receive the message
	 * @return the message received
	 */
	public static GuestMessage receiveMessage(final Socket socket) {

		try {
			InputStream in = socket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(in);
			GuestMessage msg = (GuestMessage) ois.readObject();
			return msg;

		} catch (Exception e) {
			System.out.println("Exception in communicationUtils: " + e);
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * <p>
	 * Sends a string message to the guest.
	 * </p>
	 * 
	 * @param s
	 *            the message to send
	 * @param socket
	 *            the socked used for communicate with guest
	 * @return true if success
	 */
	public static boolean sendString(final String s, final Socket socket) {

		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(s);
			return true;
		} catch (Exception e) {
			System.out.println("Exception in sendString: " + e);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * <p>
	 * Sends a message to the guest.
	 * </p>
	 * 
	 * @param mess
	 *            the message to send
	 * @param socket
	 *            the socket used for communicate with guest
	 * @return true if success
	 */
	public static boolean sendMessage(final Message mess, final Socket socket) {
		try {
			if (mess instanceof Notification) {
				Notification notif = (Notification) mess;
				GuestMessage message = new GuestMessage(
						GuestMessageType.NOTIFY, notif.getSubjectLSAid(),
						notif.getLSA(), 0, null, null);

				ObjectOutputStream oos = new ObjectOutputStream(
						socket.getOutputStream());
				oos.writeObject(message);

				return true;
			}

			return false;
		} catch (Exception e) {
			LoggerFactoryImpl.getInstance().getLogger(CommunicationUtils.class)
					.warn("Cannot send the message", e);
		}
		return false;
	}

}
