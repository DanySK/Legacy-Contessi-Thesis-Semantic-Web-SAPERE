package it.apice.sapere.space.observation.impl;

import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.space.observation.LSAEvent;
import it.apice.sapere.api.space.observation.SpaceOperationType;

/**
 * <p>
 * Implementation of LSAEvent.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see LSAEvent
 */
public class LSAEventImpl implements LSAEvent {

	/** Description of the event. */
	private final String message;

	/** Actual LSA status. */
	private final LSA lsa;

	/** Operation type. */
	private final SpaceOperationType type;

	/**
	 * <p>
	 * Builds a new LSAEventImpl.
	 * </p>
	 * 
	 * @param aMessage
	 *            Description of the event
	 * @param theLsa
	 *            The observed LSA
	 * @param aType
	 *            The type of operation that raised the event
	 */
	public LSAEventImpl(final String aMessage, final LSA theLsa,
			final SpaceOperationType aType) {
		message = aMessage;
		lsa = theLsa;
		type = aType;
	}

	@Override
	public final String getMessage() {
		return message;
	}

	@Override
	public final SpaceOperationType getOperationType() {
		return type;
	}

	@Override
	public final LSA getLSA() {
		return lsa;
	}

}
