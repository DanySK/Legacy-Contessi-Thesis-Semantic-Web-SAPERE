package it.apice.sapere.space.tdb.impl;

import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.api.space.observation.LSAObserver;
import it.apice.sapere.api.space.observation.SpaceObserver;
import it.apice.sapere.api.space.observation.SpaceOperationType;
import it.apice.sapere.lsas.visitors.impl.InjectorVisitorImpl;
import it.apice.sapere.lsas.visitors.impl.RemoverVisitorImpl;
import it.apice.sapere.space.tdb.internal.Jena2SAPEREConverter;
import it.apice.sapere.space.tdb.observation.impl.LSAEventImpl;
import it.apice.sapere.space.tdb.observation.impl.SpaceEventImpl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.tdb.TDBFactory;

/**
 * <p>
 * Implementation of LSAspace which supports ACID transactions and high
 * performance storage.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see LSAspace
 */
public class ACIDLSAspaceImpl implements LSAspace {

	/** The sapere:LSA type. */
	private static final transient String LSA_TYPE = "http://"
			+ "www.sapere-project.eu/ontologies/2012/0/sapere-model.owl#LSA";

	/** The rdf:type. */
	private static final transient String RDF_TYPE = "http://"
			+ "www.w3.org/1999/02/22-rdf-syntax-ns#type";

	/** SpaceObservers list. */
	private final transient List<SpaceObserver> listeners;

	/** LSAs observations map. */
	private final transient Map<LSAid, List<LSAObserver>> observers;

	/** Transactional DB. */
	private final transient Dataset tdb;

	/**
	 * Flag which registers if any transaction has been started since the
	 * beginning.
	 */
	private transient boolean everTransacted;

	/** RDF Graph Store. */
	private final transient Model model;

	/** rdf:type property. */
	private final transient Property rdfTypeProp;

	/** Reference to internal converter. */
	private final transient Jena2SAPEREConverter converter;

	/**
	 * <p>
	 * Builds a new LSAspaceImpl.
	 * </p>
	 * 
	 * @param aFactory
	 *            A PrivilegedLSAFactory
	 */
	public ACIDLSAspaceImpl(final PrivilegedLSAFactory aFactory) {
		// Observation initialization
		listeners = new LinkedList<SpaceObserver>();
		observers = new HashMap<LSAid, List<LSAObserver>>();

		// RDFModel initialization
		tdb = TDBFactory.createDataset("dbs/lsa-space");
		assert tdb.supportsTransactions();

		model = tdb.getDefaultModel();

		// Other stuff initialization
		rdfTypeProp = model.createProperty(RDF_TYPE);
		converter = new Jena2SAPEREConverter(aFactory);
	}

	/**
	 * <p>
	 * Disposes allocated resources.
	 * </p>
	 */
	public final void shutdown() {
		tdb.close();
	}

	/**
	 * <p>
	 * Checks if the LSA is contained in the model.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA resource
	 * @return True if contained, false otherwise
	 */
	private boolean lsaExist(final Resource lsa) {
		return model.contains(lsa, rdfTypeProp, LSA_TYPE);
	}

	@Override
	public final LSAspace inject(final LSA lsa) throws SAPEREException {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid LSA");
		}

		checkWriteTransactionStatus();
		try {
			if (checkExistencePreCondition(lsa.getLSAId())) {
				throw new SAPEREException("Duplicate LSA: " + lsa.getLSAId());
			}

			lsa.accept(new InjectorVisitorImpl(model));

			// Notification
			final String msg = String
					.format("LSA injected: %s", lsa.getLSAId());
			notifySpaceOperation(msg, lsa.getLSAId(),
					SpaceOperationType.AGENT_INJECT);
			notifyLSAObservers(msg, lsa, SpaceOperationType.AGENT_INJECT);

			return this;
		} finally {
			releaseLock();
		}
	}

	private void releaseLock() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * <p>
	 * Checks if LSA exists in the model.
	 * </p>
	 * 
	 * @param lsaId
	 *            The involved LSA's identifier
	 * @return True if ok, false otherwise
	 */
	private boolean checkExistencePreCondition(final LSAid lsaId) {
		return lsaExist(model.createResource(lsaId.toString()));
	}

	@Override
	public final LSA read(final LSAid lsaId) throws SAPEREException {
		if (lsaId == null) {
			throw new IllegalArgumentException("Invalid LSA-id");
		}

		checkReadTransactionStatus();
		try {
			if (!checkExistencePreCondition(lsaId)) {
				throw new SAPEREException("LSA not present in LSA-space: "
						+ lsaId);
			}

			final LSA lsa = converter.parseLSA(
					model.createResource(lsaId.toString()), model);

			// Space observation
			notifySpaceOperation(String.format("LSA read: %s", lsaId), lsaId,
					SpaceOperationType.AGENT_READ);

			return lsa;
		} catch (Exception e) {
			throw new SAPEREException("Cannot retrieve LSA: " + lsaId, e);
		} finally {
			releaseLock();
		}
	}

	@Override
	public final LSAspace remove(final LSA lsa) throws SAPEREException {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid LSA");
		}

		checkWriteTransactionStatus();
		try {
			if (!checkExistencePreCondition(lsa.getLSAId())) {
				throw new SAPEREException("LSA not present in LSA-space: "
						+ lsa.getLSAId());
			}

			final RemoverVisitorImpl visitor = new RemoverVisitorImpl(model);
			lsa.accept(visitor);

			// Notification
			final String msg = String.format("LSA removed: %s", lsa.getLSAId());
			notifySpaceOperation(msg, lsa.getLSAId(),
					SpaceOperationType.AGENT_REMOVE);
			notifyLSAObservers(msg, lsa, SpaceOperationType.AGENT_REMOVE);

			return this;
		} finally {
			releaseLock();
		}
	}

	@Override
	public final LSAspace update(final LSA lsa) throws SAPEREException {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid LSA");
		}

		checkWriteTransactionStatus();
		try {
			if (!checkExistencePreCondition(lsa.getLSAId())) {
				throw new SAPEREException("LSA not present in LSA-space: "
						+ lsa.getLSAId());
			}

			final RemoverVisitorImpl visitor = new RemoverVisitorImpl(model);
			lsa.accept(visitor);
			lsa.accept(new InjectorVisitorImpl(model));

			// Notification
			final String msg = String.format("LSA updated: %s", lsa.getLSAId());
			notifySpaceOperation(msg, lsa.getLSAId(),
					SpaceOperationType.AGENT_UPDATE);
			notifyLSAObservers(msg, lsa, SpaceOperationType.AGENT_UPDATE);

			return this;
		} finally {
			releaseLock();
		}
	}

	@Override
	public final LSAspace observe(final LSAid lsaId, final LSAObserver obs) {
		if (lsaId == null) {
			throw new IllegalArgumentException("Invalid LSA-id");
		}

		if (obs == null) {
			throw new IllegalArgumentException("Invalid LSAObserver");
		}

		checkWriteTransactionStatus();
		try {
			if (!observers.containsKey(lsaId)) {
				observers.put(lsaId, new LinkedList<LSAObserver>());
			}

			final List<LSAObserver> list = observers.get(lsaId);
			if (!list.contains(obs)) {
				list.add(obs);
			}

			// Space observation
			notifySpaceOperation(String.format(
					"LSA observation STARTED: %s (observer=%s)", lsaId, obs),
					lsaId, SpaceOperationType.AGENT_ACTION);

			return this;
		} finally {
			releaseLock();
		}
	}

	@Override
	public final LSAspace ignore(final LSAid lsaId, final LSAObserver obs) {
		if (lsaId == null) {
			throw new IllegalArgumentException("Invalid LSA-id");
		}

		if (obs == null) {
			throw new IllegalArgumentException("Invalid LSAObserver");
		}

		checkWriteTransactionStatus();
		try {
			final List<LSAObserver> list = observers.get(lsaId);
			if (list != null) {
				list.remove(obs);
			}

			// Space observation
			notifySpaceOperation(String.format(
					"LSA observation SUSPENDED: %s (observer=%s)", lsaId, obs),
					lsaId, SpaceOperationType.AGENT_ACTION);

			return this;
		} finally {
			releaseLock();
		}
	}

	@Override
	public final LSAspace clear() {
		checkWriteTransactionStatus();
//		try {
			model.removeAll();
			return this;
//		} catch () {
//			
//		}
	}

	@Override
	public final void addSpaceObserver(final SpaceObserver obs) {
		if (obs == null) {
			throw new IllegalArgumentException("Invalid space observer");
		}

		listeners.add(obs);
	}

	@Override
	public final void removeSpaceObserver(final SpaceObserver obs) {
		if (obs == null) {
			throw new IllegalArgumentException("Invalid space observer");
		}

		listeners.remove(obs);
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

	@Override
	public final void beginRead() {
		tdb.begin(ReadWrite.READ);
		everTransacted = true;
	}

	@Override
	public final void beginWrite() {
		tdb.begin(ReadWrite.WRITE);
		everTransacted = true;
	}

	@Override
	public final void commit() {
		try {
			tdb.commit();
		} finally {
			tdb.end();
		}
	}

	@Override
	public final void rollback() {
		try {
			tdb.abort();
		} finally {
			tdb.end();
		}
	}

	/**
	 * <p>
	 * Checks transaction status.
	 * </p>
	 */
	private void checkReadTransactionStatus() {
		if (everTransacted && !tdb.isInTransaction()) {
			throw new IllegalStateException(
					"Transactions should used always or never");
		}
	}

	/**
	 * <p>
	 * Checks transaction status.
	 * </p>
	 */
	private void checkWriteTransactionStatus() {
		if (everTransacted && !tdb.isInTransaction()) {
			throw new IllegalStateException(
					"Transactions should used always or never");
		}
	}
}
