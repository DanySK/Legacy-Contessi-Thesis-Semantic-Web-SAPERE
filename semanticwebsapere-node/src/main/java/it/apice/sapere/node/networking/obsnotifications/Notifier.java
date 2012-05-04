package it.apice.sapere.node.networking.obsnotifications;

import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.observation.SpaceOperationType;
import it.apice.sapere.node.agents.InternalAgent;
import it.apice.sapere.node.networking.Message;
import it.apice.sapere.node.networking.Subscriber;
import it.apice.sapere.node.networking.guestsmngt.GuestSubscriber;

import java.util.ArrayList;
import java.util.Hashtable;


/**
 * <p>
 * Internal agent that handles notifications.
 * </p>
 */
public final class Notifier extends InternalAgent {

	/** Many Times subscribers. */
	private final Hashtable<String, ArrayList<Subscriber>> mtsubscribers;

	/** One Time subscribers. */
	private final Hashtable<String, ArrayList<Subscriber>> otsubscribers;

	/** Read subscribers. */
	private final Hashtable<String, ArrayList<Subscriber>> rdsubscribers;

	/** Singleton instance. */
	private static final transient Notifier INSTANCE = new Notifier();

	/**
	 * <p>
	 * Retrieves the singleton reference of {@link Notifier}.
	 * </p>
	 * 
	 * @return Global reference to {@link Notifier}
	 */
	public static Notifier getInstance() {
		return INSTANCE;
	}

	/**
	 * <p>
	 * Builds a new {@link Notifier}.
	 * </p>
	 */
	private Notifier() {
		super("notifier");

		mtsubscribers = new Hashtable<String, ArrayList<Subscriber>>();
		otsubscribers = new Hashtable<String, ArrayList<Subscriber>>();
		rdsubscribers = new Hashtable<String, ArrayList<Subscriber>>();

		// TODO start();
	}

	@Override
	protected void execute() {
		while (isRunning()) {
			try {
				final Message note = getInputQueue().take();

				if (note instanceof SubscriptionRequest) {
					manageSubscription((SubscriptionRequest) note);
				} else if (note instanceof Notification) {
					manageNotification((Notification) note);
				}
			} catch (InterruptedException e) {
				spy("Interrupted");
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
			handleOneTimeSubscription(sub);
			break;
		case PERMANENT_SUBSCRIPTION:
			handleSubscription(sub);
			break;
		case READ:
			handleReadSubscription(sub);
			break;
		case CANCEL_SUBSCRIPTION:
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
		ArrayList<Subscriber> res1 = mtsubscribers.get(sub.getLSAid()
				.toString());
		ArrayList<Subscriber> res2 = otsubscribers.get(sub.getLSAid()
				.toString());
		if (res1 != null) {
			for (int i = 0; i < res1.size(); i++) {
				Subscriber e = res1.get(i);
				if (e instanceof InternalAgent
						&& sub.getSubscriber() instanceof InternalAgent) {
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
				Subscriber e = res1.get(i);
				if (e instanceof InternalAgent
						&& sub.getSubscriber() instanceof InternalAgent) {
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

		spy(sub.getSubscriber() + " subscriptions cancelled from lists.");
	}

	/**
	 * @param sub
	 *            A {@link SubscriptionRequest}
	 */
	private void handleReadSubscription(final SubscriptionRequest sub) {
		ArrayList<Subscriber> res = rdsubscribers.get(sub.getLSAid()
				.toString());
		if (res != null) {
			res.add(sub.getSubscriber());
		} else {
			ArrayList<Subscriber> list = new ArrayList<Subscriber>();
			list.add(sub.getSubscriber());
			rdsubscribers.put(sub.getLSAid().toString(), list);
		}
	}

	/**
	 * @param sub
	 *            A {@link SubscriptionRequest}
	 */
	private void handleSubscription(final SubscriptionRequest sub) {
		ArrayList<Subscriber> res = mtsubscribers.get(sub.getLSAid()
				.toString());
		if (res != null) {
			res.add(sub.getSubscriber());
		} else {
			ArrayList<Subscriber> list = new ArrayList<Subscriber>();
			list.add(sub.getSubscriber());
			mtsubscribers.put(sub.getLSAid().toString(), list);
		}
	}

	/**
	 * @param sub
	 *            A {@link SubscriptionRequest}
	 */
	private void handleOneTimeSubscription(final SubscriptionRequest sub) {
		ArrayList<Subscriber> res = otsubscribers.get(sub.getLSAid()
				.toString());
		if (res != null) {
			res.add(sub.getSubscriber());
		} else {
			ArrayList<Subscriber> list = new ArrayList<Subscriber>();
			list.add(sub.getSubscriber());
			otsubscribers.put(sub.getLSAid().toString(), list);
		}
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
		if (notify.getType() == SpaceOperationType.AGENT_READ) {
			ArrayList<Subscriber> rdnotificables = rdsubscribers.get(notify
					.getSubjectLSAid().toString());

			if (rdnotificables.size() > 0) {
				Notification newNote = new Notification(notify);

				try {
					rdnotificables.get(0).sendMessage(newNote);
				} catch (Exception e) {
					spy("Ignored Exception raised: " + e.getMessage());
					return;
				}
				rdnotificables.remove(0);
			}
			return;
		}
		spy("notification received: " + notify);
		LSAid lsaId = notify.getSubjectLSAid();
		ArrayList<Subscriber> notificables = mtsubscribers.get(lsaId
				.toString());

		if (!(notificables == null || notificables.size() == 0)) {
			spy("found subscribers...");
			for (Subscriber n : notificables) {
				if (notify.getType() == SpaceOperationType.AGENT_UPDATE) {
					Notification newNote = new Notification(notify);
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

		ArrayList<Subscriber> otnotificables = otsubscribers.get(lsaId
				.toString());

		if (otnotificables == null || otnotificables.size() == 0) {
			return;
		}

		spy("found one time subscribers...");
		for (Subscriber n : otnotificables) {
			if (notify.getType() == SpaceOperationType.AGENT_UPDATE) {
				Notification newNote = new Notification(notify);
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
			// L'iscrizione viene rimossa
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
		String msg = "subscribers list: ";

		if (mtsubscribers.keySet().isEmpty()) {
			msg += "empty";
		} else {
			msg += " \n-----------\n";
			for (String id : mtsubscribers.keySet()) {
				msg += id + " : [";
				for (Subscriber ia : mtsubscribers.get(id)) {
					msg += " " + ia + " ";
				}
				msg += "]" + "\n";
			}
			msg += "-----------";
		}
		msg += "\n";
		msg += "one time subscribers list: ";

		if (otsubscribers.keySet().isEmpty()) {
			msg += "empty";
		} else {
			msg += " \n-----------\n";
			for (String id : otsubscribers.keySet()) {
				msg += id + " : [";
				for (Subscriber ia : otsubscribers.get(id)) {
					msg += " " + ia + " ";
				}
				msg += "]" + "\n";
			}
			msg += "-----------";
		}

		spy(msg);
	}

}
