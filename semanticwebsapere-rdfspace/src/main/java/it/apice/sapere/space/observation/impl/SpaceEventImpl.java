package it.apice.sapere.space.observation.impl;

import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.observation.SpaceEvent;
import it.apice.sapere.api.space.observation.SpaceOperationType;

import java.util.Set;

/**
 * <p>
 * Implementation of SpaceEvent.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see SpaceEvent
 */
public class SpaceEventImpl implements SpaceEvent {

	/** Description of the event. */
	private final String message;

	/** LSA-ids. */
	private final LSAid[] ids;

	/** Operation type. */
	private final SpaceOperationType type;

	/**
	 * <p>
	 * Builds a new SpaceEventImpl.
	 * </p>
	 * 
	 * @param aMessage
	 *            Description of the event
	 * @param someIds
	 *            The LSA-ids of LSAs involved in the event
	 * @param aType
	 *            The type of operation that raised the event
	 */
	public SpaceEventImpl(final String aMessage, final LSAid[] someIds,
			final SpaceOperationType aType) {
		message = aMessage;
		ids = someIds;
		type = aType;
	}

	/**
	 * <p>
	 * Builds a new SpaceEventImpl.
	 * </p>
	 * 
	 * @param aMessage
	 *            Description of the event
	 * @param someIds
	 *            The LSA-ids of LSAs involved in the event
	 * @param aType
	 *            The type of operation that raised the event
	 */
	public SpaceEventImpl(final String aMessage, final Set<LSAid> someIds,
			final SpaceOperationType aType) {
		message = aMessage;
		ids = someIds.toArray(new LSAid[someIds.size()]);
		type = aType;
	}

	/**
	 * <p>
	 * Builds a new SpaceEventImpl.
	 * </p>
	 * 
	 * @param aMessage
	 *            Description of the event
	 * @param aType
	 *            The type of operation that raised the event
	 */
	public SpaceEventImpl(final String aMessage,
			final SpaceOperationType aType) {
		message = aMessage;
		ids = new LSAid[0];
		type = aType;
	}

	@Override
	public final String getMessage() {
		return message;
	}

	@Override
	public final LSAid[] getLSAid() {
		return ids;
	}

	@Override
	public final SpaceOperationType getOperationType() {
		return type;
	}

}
