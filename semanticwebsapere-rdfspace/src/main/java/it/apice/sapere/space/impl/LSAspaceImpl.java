package it.apice.sapere.space.impl;

import it.apice.sapere.api.ExtSAPEREFactory;
import it.apice.sapere.api.internal.JenaSAPEREConverter;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.nodes.SAPEREAgent;
import it.apice.sapere.api.nodes.SAPEREException;
import it.apice.sapere.api.space.ExtLSAspace;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.api.space.observation.LSAObserver;
import it.apice.sapere.api.space.observation.SpaceObserver;
import it.apice.sapere.api.space.observation.SpaceOperationType;
import it.apice.sapere.space.observation.impl.LSAEventImpl;
import it.apice.sapere.space.observation.impl.SpaceEventImpl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * <p>
 * Implementation of ExtLSAspace.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see ExtLSAspace
 */
public class LSAspaceImpl implements ExtLSAspace {

	/** SpaceObservers list. */
	private final transient List<SpaceObserver> listeners;

	/** LSAs observerations map. */
	private final transient Map<LSAid, List<LSAObserver>> observers;

	/** RDF Graph Store. */
	private final transient Model model;

	/** Jena <-> SAPERE model converter. */
	private final transient JenaSAPEREConverter converter;

	/**
	 * <p>
	 * Builds a new LSAspaceImpl.
	 * </p>
	 * 
	 * @param aFactory
	 *            Extended SAPERE model Factory
	 */
	public LSAspaceImpl(final ExtSAPEREFactory aFactory) {
		listeners = new LinkedList<SpaceObserver>();
		observers = new HashMap<LSAid, List<LSAObserver>>();
		model = ModelFactory.createDefaultModel();
		converter = new JenaSAPEREConverter(aFactory);
	}

	@Override
	public final LSAspace inject(final LSA lsa) throws SAPEREException {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid LSA");
		}

		if (read(lsa.getLSAId()) != null) {
			throw new SAPEREException("LSA already injected");
		}

		converter.serializeLSA(lsa, model);

		notifySpaceOperation(String.format("Injecting LSA: %s", lsa),
				lsa.getLSAId(), SpaceOperationType.AGENT_INJECT);
		notifyLSAObservers("LSA Injected", lsa, 
				SpaceOperationType.AGENT_INJECT);

		return this;
	}

	@Override
	public final LSA read(final LSAid lsaId) throws SAPEREException {
		if (lsaId == null) {
			throw new IllegalArgumentException("Invalid LSA-id");
		}

		final Resource rLsa = model.getResource(lsaId.getId().toString());
		if (rLsa != null) {
			try {
				final LSA lsa = converter.parseLSA(rLsa, model);

				notifySpaceOperation(
						String.format("Reading LSA: id=%s", lsaId), lsaId,
						SpaceOperationType.AGENT_READ);

				return lsa;
			} catch (Exception e) {
				throw new SAPEREException(String.format(
						"Cannot read LSA: id=%s", lsaId), e);
			}
		}

		throw new SAPEREException(String.format("Unknown LSA: id=%s", lsaId));
	}

	@Override
	public final LSAspace remove(final LSA lsa, final SAPEREAgent requestor)
			throws SAPEREException {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid LSA");
		}

		checkRemovePermission(lsa, requestor);
		return doRemove(lsa, requestor);
	}

	/**
	 * <p>
	 * Checks REMOVE permission.
	 * </p>
	 * 
	 * @param lsa
	 *            The involved LSA
	 * @param requestor
	 *            The requestor
	 * @throws SAPEREException
	 *             Operation not allowed
	 */
	private void checkRemovePermission(final LSA lsa,
			final SAPEREAgent requestor) throws SAPEREException {
		if (requestor == null) {
			throw new IllegalArgumentException("Invalid requestor");
		}

		if (!lsa.isOwnedBy(requestor)) {
			throw new SAPEREException(String.format(
					"Operation not allowed (op=remove; requestor=%s; lsa=%s)",
					requestor, lsa));
		}
	}

	@Override
	public final LSAspace update(final LSA lsa, final SAPEREAgent requestor)
			throws SAPEREException {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid LSA");
		}

		checkUpdatePermission(lsa, requestor);
		return doUpdate(lsa, requestor);
	}

	/**
	 * <p>
	 * Checks UPDATE permission.
	 * </p>
	 * 
	 * @param lsa
	 *            The involved LSA
	 * @param requestor
	 *            The requestor
	 * @throws SAPEREException
	 *             Operation not allowed
	 */
	private void checkUpdatePermission(final LSA lsa,
			final SAPEREAgent requestor) throws SAPEREException {
		if (requestor == null) {
			throw new IllegalArgumentException("Invalid requestor");
		}

		if (!lsa.isOwnedBy(requestor)) {
			throw new SAPEREException(String.format(
					"Operation not allowed (op=update; requestor=%s; lsa=%s)",
					requestor, lsa));
		}
	}

	@Override
	public final LSAspace observe(final LSAid lsaId,
			final SAPEREAgent requestor, final LSAObserver obs)
			throws SAPEREException {
		if (lsaId == null) {
			throw new IllegalArgumentException("Invalid LSA-id");
		}

		if (obs == null) {
			throw new IllegalArgumentException("Invalid observer");
		}

		checkObservePermission(lsaId, requestor);
		return doObserve(lsaId, requestor, obs);
	}

	/**
	 * <p>
	 * Checks OBSERVE permission.
	 * </p>
	 * 
	 * @param lsaId
	 *            The involved LSA's LSAid
	 * @param requestor
	 *            The requestor
	 * @throws SAPEREException
	 *             Operation not allowed
	 */
	private void checkObservePermission(final LSAid lsaId,
			final SAPEREAgent requestor) throws SAPEREException {
		if (requestor == null) {
			throw new IllegalArgumentException("Invalid requestor");
		}

		final LSA lsa = read(lsaId);
		if (lsa != null && !lsa.isOwnedBy(requestor)) {
			throw new SAPEREException(String.format("Operation not allowed "
					+ "(op=observe; requestor=%s; lsa-id=%s)", requestor, 
					lsaId));
		}
	}

	@Override
	public final LSAspace ignore(final LSAid lsaId,
			final SAPEREAgent requestor, final LSAObserver obs) {
		if (lsaId == null) {
			throw new IllegalArgumentException("Invalid LSA-id");
		}

		if (obs == null) {
			throw new IllegalArgumentException("Invalid observer");
		}

		try {
			checkIgnorePermission(lsaId, requestor);
			return doIgnore(lsaId, requestor, obs);
		} catch (SAPEREException e) {
			return this;
		}
	}

	/**
	 * <p>
	 * Checks IGNORE permission.
	 * </p>
	 * 
	 * @param lsaId
	 *            The involved LSA's LSAid
	 * @param requestor
	 *            The requestor
	 * @throws SAPEREException
	 *             Operation not allowed
	 */
	private void checkIgnorePermission(final LSAid lsaId,
			final SAPEREAgent requestor) throws SAPEREException {
		if (requestor == null) {
			throw new IllegalArgumentException("Invalid requestor");
		}

		final LSA lsa = read(lsaId);
		if (lsa != null && !lsa.isOwnedBy(requestor)) {
			throw new SAPEREException(String.format("Operation not allowed "
					+ "(op=ignore; requestor=%s; lsa-id=%s)", requestor, 
					lsaId));
		}
	}

	@Override
	public final LSAspace remove(final LSA lsa) throws SAPEREException {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid LSA");
		}

		return doRemove(lsa, null);
	}

	@Override
	public final LSAspace update(final LSA lsa) throws SAPEREException {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid LSA");
		}

		return doUpdate(lsa, null);
	}

	@Override
	public final LSAspace observe(final LSAid lsaId, final LSAObserver obs)
			throws SAPEREException {
		if (lsaId == null) {
			throw new IllegalArgumentException("Invalid LSA-id");
		}

		if (obs == null) {
			throw new IllegalArgumentException("Invalid observer");
		}

		return doObserve(lsaId, null, obs);
	}

	@Override
	public final LSAspace ignore(final LSAid lsaId, final LSAObserver obs) {
		if (lsaId == null) {
			throw new IllegalArgumentException("Invalid LSA-id");
		}

		if (obs == null) {
			throw new IllegalArgumentException("Invalid observer");
		}

		return doIgnore(lsaId, null, obs);
	}

	@Override
	public final void addSpaceObserver(final SpaceObserver obs) {
		listeners.add(obs);

		notifySpaceOperation("Space observation REGISTERED",
				SpaceOperationType.SYSTEM_ACTION);
	}

	@Override
	public final void removeSpaceObserver(final SpaceObserver obs) {
		listeners.remove(obs);

		notifySpaceOperation("Space observation CANCELLED",
				SpaceOperationType.SYSTEM_ACTION);
	}

	/**
	 * <p>
	 * Executes REMOVE operation.
	 * </p>
	 * 
	 * @param lsa
	 *            The involved LSA
	 * @param requestor
	 *            The operation requestor
	 * @return Reference to this space
	 * @throws SAPEREException
	 *             Something went wrong
	 */
	private LSAspace doRemove(final LSA lsa, final SAPEREAgent requestor)
			throws SAPEREException {
		model.getResource(lsa.getLSAId().getId().toString()).removeProperties();

		if (requestor != null) {
			notifySpaceOperation(
					String.format("Removing LSA: %s (requestor: %s)",
							lsa.getLSAId(), requestor), lsa.getLSAId(),
					SpaceOperationType.AGENT_REMOVE);
		} else {
			notifySpaceOperation(
					String.format("Removing LSA: %s (requestor: SYSTEM)",
							lsa.getLSAId()), lsa.getLSAId(),
					SpaceOperationType.SYSTEM_ACTION);
		}
		notifyLSAObservers("LSA Removed", lsa, SpaceOperationType.AGENT_REMOVE);

		return this;
	}

	/**
	 * <p>
	 * Executes UPDATE operation.
	 * </p>
	 * 
	 * @param lsa
	 *            The involved LSA
	 * @param requestor
	 *            The operation requestor
	 * @return Reference to this space
	 * @throws SAPEREException
	 *             Something went wrong
	 */
	private LSAspace doUpdate(final LSA lsa, final SAPEREAgent requestor)
			throws SAPEREException {
		// Removes LSA from the space, then re-injects it..
		model.getResource(lsa.getLSAId().getId().toString()).removeProperties();
		converter.serializeLSA(lsa, model);

		if (requestor != null) {
			notifySpaceOperation(String.format(
					"Updating LSA: id=%s, content=%s (requestor: %s)",
					lsa.getLSAId(), lsa.getSemanticDescription(), requestor),
					lsa.getLSAId(), SpaceOperationType.AGENT_UPDATE);
		} else {
			notifySpaceOperation(String.format(
					"Updating LSA: id=%s, content=%s (requestor: SYSTEM)",
					lsa.getLSAId(), lsa.getSemanticDescription()),
					lsa.getLSAId(), SpaceOperationType.SYSTEM_ACTION);
		}
		notifyLSAObservers("LSA Updated", lsa, SpaceOperationType.AGENT_UPDATE);

		return this;
	}

	/**
	 * <p>
	 * Executes OBSERVE operation.
	 * </p>
	 * 
	 * @param lsaId
	 *            The LSA-id of involved LSA
	 * @param requestor
	 *            The operation requestor
	 * @param obs
	 *            The observer involved
	 * @return Reference to this space
	 */
	private LSAspace doObserve(final LSAid lsaId, final SAPEREAgent requestor,
			final LSAObserver obs) {
		if (!observers.containsKey(lsaId)) {
			observers.put(lsaId, new LinkedList<LSAObserver>());
		}

		observers.get(lsaId).add(obs);

		if (requestor != null) {
			notifySpaceOperation(String.format(
					"Start observing LSA: id=%s (requestor: %s)", lsaId,
					requestor), lsaId, SpaceOperationType.AGENT_ACTION);
		} else {
			notifySpaceOperation(String.format("Start observing LSA: id=%s "
					+ "(requestor: SYSTEM)", lsaId), lsaId,
					SpaceOperationType.SYSTEM_ACTION);
		}

		return this;
	}

	/**
	 * <p>
	 * Executes IGNORE operation.
	 * </p>
	 * 
	 * @param lsaId
	 *            The LSA-id of involved LSA
	 * @param requestor
	 *            The operation requestor
	 * @param obs
	 *            The observer involved
	 * @return Reference to this space
	 */
	private LSAspace doIgnore(final LSAid lsaId, final SAPEREAgent requestor,
			final LSAObserver obs) {
		final List<LSAObserver> obss = observers.get(lsaId);
		if (obss != null) {
			// Removes it if is registered, otherwise nothing changes...
			obss.remove(obs);

			if (requestor != null) {
				notifySpaceOperation(String.format(
						"Stop observing LSA: id=%s (requestor: %s)", lsaId,
						requestor), lsaId, SpaceOperationType.AGENT_ACTION);
			} else {
				notifySpaceOperation(String.format("Stop observing LSA: id=%s "
						+ "(requestor: SYSTEM)", lsaId), lsaId,
						SpaceOperationType.SYSTEM_ACTION);
			}
		}

		return this;
	}

	/**
	 * <p>
	 * Notifies Space observers of an internal event.
	 * </p>
	 * 
	 * @param msg
	 *            Description of the event
	 * @param type
	 *            Event type
	 */
	private void notifySpaceOperation(final String msg,
			final SpaceOperationType type) {
		for (SpaceObserver obs : listeners) {
			obs.eventOccurred(new SpaceEventImpl(msg, type));
		}
	}

	/**
	 * <p>
	 * Notifies Space observers of an internal event.
	 * </p>
	 * 
	 * @param msg
	 *            Description of the event
	 * @param id
	 *            LSA-id of the involved LSA
	 * @param type
	 *            Event type
	 */
	private void notifySpaceOperation(final String msg, final LSAid id,
			final SpaceOperationType type) {
		for (SpaceObserver obs : listeners) {
			obs.eventOccurred(new SpaceEventImpl(msg, new LSAid[] { id }, 
					type));
		}
	}

	/**
	 * <p>
	 * Notifies observing agents (only the interested ones) that something
	 * happened to an LSA.
	 * </p>
	 * 
	 * @param msg
	 *            Description of the event
	 * @param lsa
	 *            Actual LSA status
	 * @param type
	 *            Event type
	 */
	private void notifyLSAObservers(final String msg, final LSA lsa,
			final SpaceOperationType type) {
		final List<LSAObserver> obss = observers.get(lsa.getLSAId());
		if (obss != null) {
			for (LSAObserver obs : obss) {
				obs.eventOccurred(new LSAEventImpl(msg, lsa, type));
			}
		}
	}
}
