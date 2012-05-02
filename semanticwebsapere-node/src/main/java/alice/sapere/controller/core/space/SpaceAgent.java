package alice.sapere.controller.core.space;

import alice.sapere.controller.core.InternalAgent;

/**
 * <p>
 * Class that represent an SAPEREAgent that manage the request of operations over the
 * space.
 * </p>
 * 
 * @author Marco Santarelli
 * @author Matteo Desanti
 * 
 */
public abstract class SpaceAgent extends InternalAgent {

	// protected ISpaceManager spaceManager;
	//
	/**
	 * <p>
	 * Creates a new SpaceAgent with the given identifier.
	 * </p>
	 * 
	 * @param idAgent
	 *            the identifier for this SAPEREAgent
	 */
	public SpaceAgent(final String idAgent) {
		super(idAgent);
		
		// TODO
//		try {
//			spaceManager = ReactionManager.getInstance();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	//
	// /**
	// * <p>
	// * Forwards an Inject operation to the manager.
	// * </p>
	// *
	// * @param lsa
	// * the LSA to inject
	// * @param requestingId
	// * the id of the process that forward the request
	// * @return true if the operation is queued, false otherwise
	// */
	// protected final LsaId doInject(final Lsa lsa, final String requestingId)
	// {
	// try {
	// LsaId id = spaceManager.queueOperation(new SpaceOperation(
	// SpaceOperationType.Inject, null, lsa, null, requestingId),
	// null);
	// return id;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// }
	//
	// /**
	// * <p>
	// * Forwards a Remove operation to the manager.
	// * </p>
	// *
	// * @param lsa
	// * the identifier of the LSA to remove
	// * @param requestingId
	// * the id of the process that forward the request
	// * @return true if the operation is queued, false otherwise
	// */
	// protected final boolean doRemove(final LsaId lsa, final String
	// requestingId) {
	// try {
	// spaceManager.queueOperation(new SpaceOperation(
	// SpaceOperationType.Remove, lsa, null, null, requestingId),
	// null);
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// }
	//
	// /**
	// * <p>
	// * Forwards an Update operation to the manager.
	// * </p>
	// *
	// * @param lsa
	// * the identifier of the LSA to update
	// * @param newCont
	// * the new content for the LSA
	// * @param requestingId
	// * the id of the process that forward the request
	// * @return true if the operation is queued, false otherwise
	// */
	// protected final boolean doUpdate(final LsaId lsa, final Property[] props,
	// final String requestingId) {
	// try {
	// spaceManager.queueOperation(new SpaceOperation(
	// SpaceOperationType.Update, lsa, null, props, requestingId),
	// null);
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// }
	//
	// /**
	// * <p>
	// * Forwards an Observe operation to the Notifier.
	// * </p>
	// *
	// * @param lsa
	// * the identifier of the LSA to update
	// * @param filter
	// * the filter for the observe (deprecated)
	// * @return true if the operation is forwarded, false otherwise
	// */
	// protected final boolean doObserveOne(final LsaId lsa, final ISubscriber
	// subscriber,
	// final IContentFilter filter) {
	// try {
	// Notifier.getInstance().sendMessage(
	// new SubscriptionRequest(lsa, SubscriptionType.One,
	// subscriber, filter));
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// }
	//
	// protected final boolean doObserveMany(final LsaId lsa, final ISubscriber
	// subscriber,
	// final IContentFilter filter) {
	// try {
	// Notifier.getInstance().sendMessage(
	// new SubscriptionRequest(lsa, SubscriptionType.Many,
	// subscriber, filter));
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// }
	//
	// protected final boolean doRead(final LsaId lsa, final ISubscriber
	// subscriber,
	// final IContentFilter filter) {
	// try {
	// Notifier.getInstance().sendMessage(
	// new SubscriptionRequest(lsa, SubscriptionType.Read,
	// subscriber, filter));
	// spaceManager.queueOperation(new SpaceOperation(
	// SpaceOperationType.Read, lsa, null, null, null), null);
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// }
	//
	// protected final boolean doDeregister(final LsaId lsa, final ISubscriber
	// subscriber,
	// final IContentFilter filter) {
	// try {
	// Notifier.getInstance().sendMessage(
	// new SubscriptionRequest(lsa, SubscriptionType.Deregister,
	// subscriber, filter));
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// }

	// /**
	// * Forwards an Inject/Observe operation to the manager and Notifier
	// *
	// * @param lsa the identifier of the LSA to update
	// * @param filter the filter for the observe (deprecated)
	// * @param requestingId the id of the process that forward the request
	// * @return true if the operation is forwarded, false otherwise
	// */
	// protected boolean doInjectObserve(Lsa lsa, IContentFilter filter, String
	// requestingId){
	// //non so ancora l'id dell'lsa...
	// SubscriptionRequest sub = new
	// SubscriptionRequest(null,SubscriptionType.One,this,filter);
	// try {
	// spaceManager.queueOperation(new SpaceOperation(SpaceOperationType.Inject,
	// null, lsa, null, requestingId),sub);
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// }
}
