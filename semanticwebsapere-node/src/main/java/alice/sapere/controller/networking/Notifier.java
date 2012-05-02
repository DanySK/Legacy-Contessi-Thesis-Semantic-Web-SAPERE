package alice.sapere.controller.networking;

import it.apice.sapere.api.space.observation.SpaceOperationType;

import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.commons.logging.LogFactory;

import alice.sapere.controller.core.ISubscriber;
import alice.sapere.controller.core.InternalAgent;
import alice.sapere.controller.networking.guestsmanage.GuestSubscriber;
import alice.sapere.model.communication.Message;
import alice.sapere.model.communication.Notification;
import alice.sapere.model.communication.SubscriptionRequest;

/**
 * <p>
 * Internal agent that handles notifications.
 * </p>
 */
public final class Notifier extends InternalAgent {

	/** Many T subscribers. */
	private final Hashtable<String, ArrayList<ISubscriber>> mtsubscribers;

	/** One T subscribers. */
	private final Hashtable<String, ArrayList<ISubscriber>> otsubscribers;

	/** Read subscribers. */
	private final Hashtable<String, ArrayList<ISubscriber>> rdsubscribers;

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

		mtsubscribers = new Hashtable<String, ArrayList<ISubscriber>>();
		otsubscribers = new Hashtable<String, ArrayList<ISubscriber>>();
		rdsubscribers = new Hashtable<String, ArrayList<ISubscriber>>();

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
				LogFactory.getLog(Notifier.class).warn("No note to handle.", e);
			}
		}
	}

	private void manageSubscription(final SubscriptionRequest sub) {
		spy("subscription message received.");
		switch (sub.getType()) {
		case One:
			ArrayList<ISubscriber> res = otsubscribers.get(sub
					.getLsaSubjectId().toString());
			if (res != null) {
				res.add(sub.getSubscriber());
			} else {
				ArrayList<ISubscriber> list = new ArrayList<ISubscriber>();
				list.add(sub.getSubscriber());
				otsubscribers.put(sub.getLsaSubjectId().toString(), list);
			}
			printSubscribersList();
			break;
		case Many:
			ArrayList<ISubscriber> res = mtsubscribers.get(sub
					.getLsaSubjectId().toString());
			if (res != null) {
				res.add(sub.getSubscriber());
			} else {
				ArrayList<ISubscriber> list = new ArrayList<ISubscriber>();
				list.add(sub.getSubscriber());
				mtsubscribers.put(sub.getLsaSubjectId().toString(), list);
			}
			printSubscribersList();
			break;
		case Read:
			ArrayList<ISubscriber> res = rdsubscribers.get(sub
					.getLsaSubjectId().toString());
			if (res != null) {
				res.add(sub.getSubscriber());
			} else {
				ArrayList<ISubscriber> list = new ArrayList<ISubscriber>();
				list.add(sub.getSubscriber());
				rdsubscribers.put(sub.getLsaSubjectId().toString(), list);
			}
			printSubscribersList();
			break;
		case Deregister:
			/*
			 * try{ for(ISubscriber e:
			 * mtsubscribers.get(sub.getLsaSubjectId())){
			 * System.out.println(""+e); } for(ISubscriber e:
			 * otsubscribers.get(sub.getLsaSubjectId())){
			 * System.out.println(""+e); } }catch(Exception e){}
			 */
			ArrayList<ISubscriber> res1 = mtsubscribers.get(sub
					.getLsaSubjectId().toString());
			ArrayList<ISubscriber> res2 = otsubscribers.get(sub
					.getLsaSubjectId().toString());
			if (res1 != null) {
				for (int i = 0; i < res1.size(); i++) {
					ISubscriber e = res1.get(i);
					if (e instanceof InternalAgent
							&& sub.getSubscriber() instanceof InternalAgent) {
						res1.remove(sub.getSubscriber());
					} else {
						if (e instanceof GuestSubscriber
								&& sub.getSubscriber() instanceof GuestSubscriber
								&& ((GuestSubscriber) e).getDestination()
										.equals(((GuestSubscriber) sub
												.getSubscriber())
												.getDestination())) {
							res1.remove(i);
						}
					}
				}

				// boolean b1=res1.remove(sub.getSubscriber());
				// System.out.println("b1= "+b1);
			}
			if (res2 != null) {
				for (int i = 0; i < res1.size(); i++) {
					ISubscriber e = res1.get(i);
					if (e instanceof InternalAgent
							&& sub.getSubscriber() instanceof InternalAgent) {
						res2.remove(sub.getSubscriber());
					} else {
						if (e instanceof GuestSubscriber
								&& sub.getSubscriber() instanceof GuestSubscriber
								&& ((GuestSubscriber) e).getDestination()
										.equals(((GuestSubscriber) sub
												.getSubscriber())
												.getDestination())) {
							res2.remove(i);
						}
					}
				}

				// boolean b1=res1.remove(sub.getSubscriber());
				// System.out.println("b1= "+b1);
			}
			spy(sub.getSubscriber() + " deregistered from lists.");
			/*
			 * try{ for(ISubscriber e:
			 * mtsubscribers.get(sub.getLsaSubjectId())){
			 * System.out.println(""+e); } for(ISubscriber e:
			 * otsubscribers.get(sub.getLsaSubjectId())){
			 * System.out.println(""+e); } }catch(Exception e){}
			 */
			printSubscribersList();
			break;
		default:
			break;

		}

	}

	private void manageNotification(final Notification notify) {

		if (notify.getType() == SpaceOperationType.Read) {
			ArrayList<ISubscriber> rdnotificables = rdsubscribers.get(notify
					.getLsaSubjectId().toString());

			if (rdnotificables.size() > 0) {
				Notification newNote = new Notification(notify.getType(),
						notify.getLsaSubjectId(), notify.getNewContent());

				try {
					rdnotificables.get(0).sendMessage(newNote);
				} catch (Exception e) {
					return;
				}
				rdnotificables.remove(0);
			}
			return;
		}
		spy("notification received: " + notify);
		LsaId lsaId = notify.getLsaSubjectId();
		ArrayList<ISubscriber> notificables = mtsubscribers.get(lsaId
				.toString());

		if (!(notificables == null || notificables.size() == 0)) {

			spy("found subscribers...");
			for (ISubscriber n : notificables) {
				if (notify.getType() == SpaceOperationType.Update) {
					// IContentFilter filter = n.getContentFilter();
					Content cont = notify.getNewContent();
					// if (filter!=null) cont =
					// filter.filters(notify.getNewContent());
					Notification newNote = new Notification(notify.getType(),
							notify.getLsaSubjectId(), cont);
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
				// subscribers.remove(lsaId+"");
			}
		}
		ArrayList<ISubscriber> otnotificables = otsubscribers.get(lsaId
				.toString());

		if (otnotificables == null || otnotificables.size() == 0) {
			return;
		}

		spy("found one time subscribers...");
		for (ISubscriber n : otnotificables) {
			if (notify.getType() == SpaceOperationType.Update) {
				// IContentFilter filter = n.getContentFilter();
				Content cont = notify.getNewContent();
				// if (filter!=null) cont =
				// filter.filters(notify.getNewContent());
				Notification newNote = new Notification(notify.getType(),
						notify.getLsaSubjectId(), cont);
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
				for (ISubscriber ia : mtsubscribers.get(id)) {
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
				for (ISubscriber ia : otsubscribers.get(id)) {
					msg += " " + ia + " ";
				}
				msg += "]" + "\n";
			}
			msg += "-----------";
		}

		spy(msg);
	}

}
