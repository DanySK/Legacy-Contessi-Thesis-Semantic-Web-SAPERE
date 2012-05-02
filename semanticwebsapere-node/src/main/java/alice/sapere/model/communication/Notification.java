package alice.sapere.model.communication;

import it.apice.sapere.api.space.observation.SpaceOperationType;

import java.io.Serializable;

public class Notification extends Message implements Serializable {

	private static final long serialVersionUID = -6417756713784371365L;
	private SpaceOperationType type;
	private LsaId subject;
	private Content content;

	public Notification(final SpaceOperationType type, final LsaId sub,
			final Content mod) {
		this.type = type;
		this.subject = sub;
		this.content = mod;
	}

	public final SpaceOperationType getType() {
		return type;
	}

	public final Notification getCopy() {
		if (content != null) {
			return new Notification(type, subject, content.getCopy());
		}
		return new Notification(type, subject, null);
	}

	public final Content getNewContent() {
		return content;
	}

	public final LsaId getLsaSubjectId() {
		return subject;
	}

	@Override
	public final String toString() {
		if (content != null) {
			return "Notify[type=" + type + " subject=" + subject
					+ " newContent=" + content + "]";
		} else {
			return "Notify[type=" + type + " subject=" + subject + "]";
		}
	}
}
