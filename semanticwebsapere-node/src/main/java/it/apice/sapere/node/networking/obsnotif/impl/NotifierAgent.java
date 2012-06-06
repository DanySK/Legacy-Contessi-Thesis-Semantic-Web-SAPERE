package it.apice.sapere.node.networking.obsnotif.impl;

import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.node.NodeServices;
import it.apice.sapere.api.node.agents.networking.Message;
import it.apice.sapere.api.node.agents.networking.Subscriber;
import it.apice.sapere.api.space.observation.SpaceEvent;
import it.apice.sapere.api.space.observation.SpaceObserver;
import it.apice.sapere.api.space.observation.SpaceOperationType;
import it.apice.sapere.node.agents.AbstractSystemAgent;
import it.apice.sapere.node.agents.impl.AbstractSAPEREAgentImpl;
import it.apice.sapere.node.internal.NodeServicesImpl;
import it.apice.sapere.node.networking.guestsmngt.impl.GuestSubscriber;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * <p>
 * Internal agent that handles notifications.
 * </p>
 * 
 * <p>
 * Some modifications occurred during RDF-based subsystem intergration.
 * </p>
 * 
 * @author Unknown
 * @author Paolo Contessi
 */
@Deprecated
public final class NotifierAgent extends AbstractSystemAgent 
		implements SpaceObserver {

	/** Many Times subscribers. */
	private final Hashtable<String, ArrayList<Subscriber>> mtsubscribers;

	/** One Time subscribers. */
	private final Hashtable<String, ArrayList<Subscriber>> otsubscribers;

	/** Singleton instance. */
	private static final transient NotifierAgent INSTANCE = new NotifierAgent();

	/**
	 * <p>
	 * Retrieves the singleton reference of {@link NotifierAgent}.
	 * </p>
	 * 
	 * @return Global reference to {@link NotifierAgent}
	 */
	public static NotifierAgent getInstance() {
		return INSTANCE;
	}

	/**
	 * <p>
	 * Builds a new {@link NotifierAgent}.
	 * </p>
	 */
	private NotifierAgent() {
		super("notifier");

		mtsubscribers = new Hashtable<String, ArrayList<Subscriber>>();
		otsubscribers = new Hashtable<String, ArrayList<Subscriber>>();
	}

	@Override
	protected void behaviour(final NodeServices services) {
		services.getLSAspace().addSpaceObserver(this);

		while (isRunning()) {
			try {
				final Message note = getInputQueue().take();

				if (note instanceof SubscriptionRequest) {
					manageSubscription((SubscriptionRequest) note);
				} else if (note instanceof Notification) {
					manageNotification((Notification) note);
				}
			} catch (InterruptedException e) {
				spy("interrupted");
			}
		}
	}

	/**
	 * 
	 * @param sub
	 *            A {@link SubscriptionRequest}
	 */
	private void manageSubscription(final SubscriptionRequest sub) {
		spy("subscription message received.");
		switch (sub.getType()) {
		case ONE_TIME_SUBSCRIPTION:
			spy("Handling ONE-TIME subscription");
			handleOneTimeSubscription(sub);
			break;
		case PERMANENT_SUBSCRIPTION:
			spy("Handling subscription");
			handleSubscription(sub);
			break;
		case CANCEL_SUBSCRIPTION:
			spy("Handling CANCEL subscription");
			handleCancelSubscription(sub);
			break;
		default:
			break;

		}

	}

	/**
	 * @param sub
	 *            A {@link SubscriptionRequest}
	 */
	private void handleCancelSubscription(final SubscriptionRequest sub) {
		final ArrayList<Subscriber> res1 = mtsubscribers.get(sub.getLSAid()
				.toString());
		final ArrayList<Subscriber> res2 = otsubscribers.get(sub.getLSAid()
				.toString());
		if (res1 != null) {
			for (int i = 0; i < res1.size(); i++) {
				final Subscriber e = res1.get(i);
				if (e instanceof AbstractSAPEREAgentImpl
						&& sub.getSubscriber() 
						instanceof AbstractSAPEREAgentImpl) {
					res1.remove(sub.getSubscriber());
				} else {
					if (e instanceof GuestSubscriber
							&& sub.getSubscriber() instanceof GuestSubscriber
							&& ((GuestSubscriber) e).getDestination().equals(
									((GuestSubscriber) sub.getSubscriber())
											.getDestination())) {
						res1.remove(i);
					}
				}
			}
		}
		if (res2 != null) {
			for (int i = 0; i < res1.size(); i++) {
				final Subscriber e = res1.get(i);
				if (e instanceof AbstractSAPEREAgentImpl
						&& sub.getSubscriber() 
						instanceof AbstractSAPEREAgentImpl) {
					res2.remove(sub.getSubscriber());
				} else {
					if (e instanceof GuestSubscriber
							&& sub.getSubscriber() instanceof GuestSubscriber
							&& ((GuestSubscriber) e).getDestination().equals(
									((GuestSubscriber) sub.getSubscriber())
											.getDestination())) {
						res2.remove(i);
					}
				}
			}
		}

		spy(sub.getSubscriber() + " subscriptions cancelled.");
	}

	/**
	 * @param sub
	 *            A {@link SubscriptionRequest}
	 */
	private void handleSubscription(final SubscriptionRequest sub) {
		final ArrayList<Subscriber> res = mtsubscribers
				.get(sub.getLSAid().toString());
		if (res != null) {
			res.add(sub.getSubscriber());
		} else {
			final ArrayList<Subscriber> list = new ArrayList<Subscriber>();
			list.add(sub.getSubscriber());
			mtsubscribers.put(sub.getLSAid().toString(), list);
		}

		spy("Subscription registered");
	}

	/**
	 * @param sub
	 *            A {@link SubscriptionRequest}
	 */
	private void handleOneTimeSubscription(final SubscriptionRequest sub) {
		final ArrayList<Subscriber> res = otsubscribers
				.get(sub.getLSAid().toString());
		if (res != null) {
			res.add(sub.getSubscriber());
		} else {
			final ArrayList<Subscriber> list = new ArrayList<Subscriber>();
			list.add(sub.getSubscriber());
			otsubscribers.put(sub.getLSAid().toString(), list);
		}
		
		spy("ONE-TIME Subscription registered");
	}

	/**
	 * <p>
	 * Manages {@link Notification}s.
	 * </p>
	 * 
	 * @param notify
	 *            A {@link Notification} to be handled
	 */
	private void manageNotification(final Notification notify) {
		spy("notification received: " + notify);
		final LSAid lsaId = notify.getSubjectLSAid();
		final ArrayList<Subscriber> notificables = mtsubscribers
				.get(lsaId.toString());

		if (!(notificables == null || notificables.size() == 0)) {
			spy("found subscribers...");
			for (Subscriber n : notificables) {
				if (notify.getType() == SpaceOperationType.AGENT_UPDATE) {
					final Notification newNote = new Notification(notify);
					try {
						n.sendMessage(newNote);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						n.sendMessage(notify.getCopy());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		final ArrayList<Subscriber> otnotificables = otsubscribers.get(lsaId
				.toString());

		if (otnotificables == null || otnotificables.size() == 0) {
			return;
		}

		spy("found one-time subscribers...");
		for (Subscriber n : otnotificables) {
			if (notify.getType() == SpaceOperationType.AGENT_UPDATE) {
				final Notification newNote = new Notification(notify);
				try {
					n.sendMessage(newNote);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					n.sendMessage(notify.getCopy());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// One-Time Subscription removal
			otsubscribers.remove(lsaId.toString());
		}

		printSubscribersList();
	}

	/**
	 * <p>
	 * Prints in the log the list of subscribers.
	 * </p>
	 */
	private void printSubscribersList() {
		final StringBuilder builder = new StringBuilder();
		builder.append("NotifierAgent [subscribers=");
		builder.append(mtsubscribers);
		builder.append(", one-time-subscribers=");
		builder.append(otsubscribers);
		builder.append("]");

		spy(builder.toString());
	}

	@Override
	public void eventOccurred(final SpaceEvent ev) {
		if (ev.getOperationType() != SpaceOperationType.AGENT_READ
				&& ev.getOperationType() != SpaceOperationType.AGENT_ACTION) {
			spy("Requiring notification for event " + ev.toString());

			final String[] lsas = ev.getLSAContent(RDFFormat.RDF_XML);
			for (int i = 0; i < lsas.length; i++) {
				sendMessage(new Notification(ev.getOperationType(),
						NodeServicesImpl.getInstance().getLSACompiler()
								.parse(lsas[i], RDFFormat.RDF_XML)));
			}
		}
	}
}
