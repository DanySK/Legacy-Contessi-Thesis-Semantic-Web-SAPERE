package it.apice.sapere.space.observation.impl;

import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.core.CompiledLSA;
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

	/** Compiled LSAs. */
	private final CompiledLSA<?>[] lsas;

	/** Operation type. */
	private final SpaceOperationType type;

	/** Support ids vector. */
	private LSAid[] ids;

	/**
	 * <p>
	 * Builds a new SpaceEventImpl.
	 * </p>
	 * 
	 * @param aMessage
	 *            Description of the event
	 * @param someLsas
	 *            The (compiled) LSAs involved in the event
	 * @param aType
	 *            The type of operation that raised the event
	 */
	public SpaceEventImpl(final String aMessage,
			final CompiledLSA<?>[] someLsas, final SpaceOperationType aType) {
		message = aMessage;
		lsas = someLsas;
		type = aType;
	}

	/**
	 * <p>
	 * Builds a new SpaceEventImpl.
	 * </p>
	 * 
	 * @param aMessage
	 *            Description of the event
	 * @param someLsas
	 *            The (compiled) LSAs involved in the event
	 * @param aType
	 *            The type of operation that raised the event
	 */
	public SpaceEventImpl(final String aMessage,
			final Set<CompiledLSA<?>> someLsas, 
			final SpaceOperationType aType) {
		message = aMessage;
		lsas = someLsas.toArray(new CompiledLSA[someLsas.size()]);
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
		lsas = new CompiledLSA[0];
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
	 *            The LSA-ids of the LSAs involved in the event
	 * @param aType
	 *            The type of operation that raised the event
	 */
	public SpaceEventImpl(final String aMessage, final LSAid[] someIds,
			final SpaceOperationType aType) {
		message = aMessage;
		lsas = null;
		ids = someIds;
		type = aType;
	}

	@Override
	public final String getMessage() {
		return message;
	}

	@Override
	public final LSAid[] getLSAid() {
		if (ids == null) {
			ids = new LSAid[lsas.length];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = lsas[i].getLSAid();
			}
		}

		return ids;
	}

	@Override
	public final SpaceOperationType getOperationType() {
		return type;
	}

	@Override
	public final String[] getLSAContent(final RDFFormat format) {
		if (lsas != null) {
			final String[] res = new String[lsas.length];
			for (int i = 0; i < res.length; i++) {
				res[i] = lsas[i].toString(format);
			}

			return res;
		}
	
		return new String[0];
	}

}
