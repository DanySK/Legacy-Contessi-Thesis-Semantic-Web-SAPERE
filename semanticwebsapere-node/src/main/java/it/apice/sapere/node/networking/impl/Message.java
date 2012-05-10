package it.apice.sapere.node.networking.impl;

/**
 * @author Michele Morgagni
 * @author Danilo Pianini
 * 
 */
public abstract class Message implements Comparable<Message> {

	/** Message's time-stamp. */
	private final long time;

	/**
	 * <p>
	 * Builds a new {@link Message}.
	 * </p>
	 */
	public Message() {
		time = System.currentTimeMillis();
	}

	/**
	 * <p>
	 * Clones this message.
	 * </p>
	 *
	 * @return Cloned message
	 */
	public abstract Message getCopy();

	/**
	 * <p>
	 * Retrieves the time-stamp of this message.
	 * </p>
	 *
	 * @return Message's time-stamp
	 */
	public final long getTimeStamp() {
		return time;
	}

	@Override
	public final int compareTo(final Message m) {
		return (int) (time - m.getTimeStamp());
	}
}
