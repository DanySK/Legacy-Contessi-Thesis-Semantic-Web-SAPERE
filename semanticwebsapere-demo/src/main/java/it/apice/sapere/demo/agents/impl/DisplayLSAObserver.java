package it.apice.sapere.demo.agents.impl;

import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.lsas.Property;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.values.LSAidValue;
import it.apice.sapere.api.lsas.values.PropertyValue;
import it.apice.sapere.api.space.observation.LSAEvent;
import it.apice.sapere.api.space.observation.LSAObserver;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p>
 * This class represents an observer of the Display LSA, which fires display
 * refresh on LSA update.
 * 
 * @author Paolo Contessi
 * 
 */
public class DisplayLSAObserver implements LSAObserver {

	/** Neighbor user property name. */
	private final transient PropertyName _propName;

	/** Synchronized queue nearest users LSA-ids. */
	private final transient BlockingQueue<List<LSAid>> evQueue = 
			new LinkedBlockingQueue<List<LSAid>>();

	/**
	 * <p>
	 * Builds a new {@link DisplayLSAObserver}.
	 * </p>
	 * 
	 * @param userPropName
	 *            (Nearest) User property name
	 */
	public DisplayLSAObserver(final PropertyName userPropName) {
		if (userPropName == null) {
			throw new IllegalArgumentException(
					"Invalid user property name provided");
		}

		_propName = userPropName;
	}

	@Override
	public void eventOccurred(final LSAEvent ev) {
		final List<LSAid> ids = new LinkedList<LSAid>();
		final Property prop = ev.getLSA().getSemanticDescription()
				.get(_propName);
		if (prop != null) {
			for (PropertyValue<?, ?> val : prop.values()) {
				if (val instanceof LSAidValue) {
					ids.add(((LSAidValue) val).getValue());
				}
			}
		}
		
		evQueue.offer(ids);
	}

	/**
	 * <p>
	 * Retrieves the updated list of LSAid of nearest users.
	 * </p>
	 * 
	 * @return List of nearest users
	 * @throws InterruptedException
	 *             Cannot retrieve it (because interrupted)
	 */
	public List<LSAid> getUsersLSAids() throws InterruptedException {
		return evQueue.take();
	}
}
