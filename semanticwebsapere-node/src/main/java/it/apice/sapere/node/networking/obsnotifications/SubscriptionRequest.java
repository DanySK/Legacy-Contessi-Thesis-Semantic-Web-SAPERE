package it.apice.sapere.node.networking.obsnotifications;

import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.node.networking.Message;

import java.io.Serializable;


/**
 * <p>
 * This class represents a request that a subscriber forwards in order to be
 * notified of LSA-space events involving the specified LSA.
 * </p>
 */
public class SubscriptionRequest extends Message implements Serializable {

	/** Serialization ID. */
	private static final long serialVersionUID = -5721538661270246401L;

	/** LSA-id to be monitored. */
	private LSAid subjectId;

	/** The subscriber of the notification. */
	private Subscriber subscriber;

	/** Type of the subscription. */
	private SubscriptionType type;

	/**
	 * <p>
	 * Builds a new {@link SubscriptionRequest}.
	 * </p>
	 * 
	 * @param id
	 *            The LSA-id to be monitored
	 * @param aType
	 *            The type of subscription
	 * @param aSubscriber
	 *            Who is subscribing
	 */
	public SubscriptionRequest(final LSAid id, final SubscriptionType aType,
			final Subscriber aSubscriber) {
		subjectId = id;
		type = aType;
		subscriber = aSubscriber;
	}

	/**
	 * <p>
	 * Retrieves the subscriber who forwarded the request.
	 * </p>
	 * 
	 * @return The subscriber
	 */
	public final Subscriber getSubscriber() {
		return subscriber;
	}

	/**
	 * <p>
	 * Retrieves the LSA-id to be monitored.
	 * </p>
	 * 
	 * @return The LSA-id to be monitored
	 */
	public final LSAid getLSAid() {
		return subjectId;
	}

	/**
	 * <p>
	 * Retrieves the type of the subscription.
	 * </p>
	 * 
	 * @return The subscription type
	 */
	public final SubscriptionType getType() {
		return type;
	}

	@Override
	public final SubscriptionRequest getCopy() {
		return new SubscriptionRequest(subjectId, type, subscriber);
	}

	@Override
	public final String toString() {
		return "Subscription[operation=" + type + " subjectId=" + subjectId
				+ " subscriber=" + subscriber + "]";
	}
}
