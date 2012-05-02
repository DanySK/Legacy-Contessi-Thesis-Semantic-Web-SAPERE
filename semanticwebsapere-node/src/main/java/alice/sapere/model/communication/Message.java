package alice.sapere.model.communication;

/**
 * @author Michele Morgagni
 * @author Danilo Pianini
 * 
 */
public abstract class Message implements Comparable<Message> {

	private final long time;

	public Message() {
		time = System.currentTimeMillis();
	}

	public abstract Message getCopy();

	public long getTimeStamp() {
		return time;
	}

	public int compareTo(Message m) {
		return (int) (time - m.getTimeStamp());
	}
}
