package alice.sapere.model.communication;

import java.io.Serializable;

import alice.sapere.controller.core.ISubscriber;

public class SubscriptionRequest extends Message implements Serializable {

	private static final long serialVersionUID = -5721538661270246401L;
	private LsaId subjectId;
	private ISubscriber subscriber;
	private IContentFilter contentFilter;
	private SubscriptionType type;

	public SubscriptionRequest(final LsaId id, final SubscriptionType type,
			final ISubscriber subscriber, final IContentFilter filter) {
		this.subjectId = id;
		this.type = type;
		this.subscriber = subscriber;
		this.contentFilter = filter;
	}

	public final ISubscriber getSubscriber() {
		return subscriber;
	}

	public final LsaId getLsaSubjectId() {
		return subjectId;
	}

	public final SubscriptionType getType() {
		return type;
	}

	@Override
	public final SubscriptionRequest getCopy() {
		return new SubscriptionRequest(subjectId, type, subscriber,
				contentFilter);
	}

	public final IContentFilter getContentFilter() {
		return contentFilter;
	}

	@Override
	public final String toString() {
		return "Subscription[operation=" + type + " subjectId=" + subjectId
				+ " subscriber=" + subscriber + "]";
	}
}
