package it.apice.sapere.api.space.core.impl;

import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.match.MatchResult;
import it.apice.sapere.api.ecolaws.match.MatchingEcolaw;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.api.space.core.CompiledEcolaw;
import it.apice.sapere.api.space.core.CompiledLSA;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.observation.LSAObserver;
import it.apice.sapere.api.space.observation.SpaceObserver;
import it.apice.sapere.api.space.observation.SpaceOperationType;
import it.apice.sapere.space.observation.impl.LSAEventImpl;
import it.apice.sapere.space.observation.impl.SpaceEventImpl;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * <p>
 * Implementation of LSAspaceCore.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see LSAspace
 */
public abstract class AbstractLSAspaceCoreImpl implements LSAspaceCore {

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

	/** RDF Graph Store. */
	private final transient Model model;

	/** Reference to an LSACompiler. */
	private final transient LSACompiler compiler;

	/** Reference to an LSAParser. */
	private final transient LSAParser parser;

	/** rdf:type property. */
	private final transient Property rdfTypeProp;

	/** Provides thread-safety. */
	private final transient ReadWriteLock mutex = new ReentrantReadWriteLock();

	/** Read/Write flag. */
	private transient boolean isWriting = false;

	/**
	 * <p>
	 * Builds a new {@link AbstractLSAspaceCoreImpl}.
	 * </p>
	 * 
	 * @param lsaCompiler
	 *            Reference to a {@link LSACompiler}
	 * @param lsaParser
	 *            Reference to a {@link LSAParser}
	 */
	public AbstractLSAspaceCoreImpl(final LSACompiler lsaCompiler,
			final LSAParser lsaParser) {
		if (lsaCompiler == null) {
			throw new IllegalArgumentException("Invalid LSACompiler provided");
		}

		if (lsaParser == null) {
			throw new IllegalArgumentException("Invalid LSAParser provided");
		}

		// Observation initialization
		listeners = new LinkedList<SpaceObserver>();
		observers = new HashMap<LSAid, List<LSAObserver>>();

		// RDFModel initialization
		model = initRDFGraphModel();

		// Other stuff initialization
		rdfTypeProp = model.createProperty(RDF_TYPE);
		compiler = lsaCompiler;
		parser = lsaParser;
	}

	/**
	 * <p>
	 * Creates and returns an instance of Jena's Model, where LSAs will be
	 * stored.
	 * </p>
	 * 
	 * @return A reference to a (Jena's) Model
	 */
	protected abstract Model initRDFGraphModel();

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

	/**
	 * <p>
	 * Acquires read lock on model.
	 * </p>
	 */
	private void acquireReadLock() {
		model.enterCriticalSection(false);
	}

	/**
	 * <p>
	 * Acquires write lock on model.
	 * </p>
	 */
	private void acquireWriteLock() {
		model.enterCriticalSection(true);
	}

	/**
	 * <p>
	 * Releases lock on model.
	 * </p>
	 */
	private void releaseLock() {
		model.leaveCriticalSection();
	}

	@Override
	public final LSAspace inject(final LSA lsa) throws SAPEREException {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid LSA");
		}

		injectCompiled(compile(lsa));
		return this;
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

		return retrieveLSA(readCompiled(lsaId));
	}

	@Override
	public final LSAspace remove(final LSA lsa) throws SAPEREException {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid LSA");
		}

		removeCompiled(compile(lsa));
		return this;
	}

	@Override
	public final LSAspace update(final LSA lsa) throws SAPEREException {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid LSA");
		}

		updateCompiled(compile(lsa));
		return this;
	}

	@Override
	public final LSAspace observe(final LSAid lsaId, final LSAObserver obs) {
		if (lsaId == null) {
			throw new IllegalArgumentException("Invalid LSA-id");
		}

		if (obs == null) {
			throw new IllegalArgumentException("Invalid LSAObserver");
		}

		acquireWriteLock();
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

		acquireWriteLock();
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
		acquireWriteLock();
		try {
			model.removeAll();
			return this;
		} finally {
			releaseLock();
		}
	}

	@Override
	public final MatchResult[] match(final CompiledEcolaw law) {
		// TODO Auto-generated method stub

		// 1. Run query
		// 2. Extract bindings
		// 3. Return them for evaluation

		return null;
	}

	@Override
	public final LSAspace apply(final MatchingEcolaw law) {
		// TODO Auto-generated method stub

		// 1. Apply selected binding to the law
		// 2. Run query

		return null;
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
		mutex.readLock().lock();
		isWriting = false;
	}

	@Override
	public final void beginWrite() {
		mutex.writeLock().lock();
		isWriting = true;
	}

	@Override
	public final void done() {
		if (isWriting) {
			mutex.writeLock().unlock();
		} else {
			mutex.readLock().unlock();
		}
	}

	@Override
	public final LSAspace injectCompiled(final CompiledLSA lsa)
			throws SAPEREException {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid compiled LSA provided");
		}

		acquireWriteLock();
		try {
			if (checkExistencePreCondition(lsa.getLSAid())) {
				throw new SAPEREException("Duplicate LSA: " + lsa.getLSAid());
			}

			model.add(lsa.getStatements());

			// Notification
			final String msg = String
					.format("LSA injected: %s", lsa.getLSAid());
			notifySpaceOperation(msg, lsa.getLSAid(),
					SpaceOperationType.AGENT_INJECT);
			notifyLSAObservers(msg, retrieveLSA(lsa),
					SpaceOperationType.AGENT_INJECT);

			return this;
		} catch (Exception ex) {
			throw new SAPEREException("Cannot inject", ex);
		} finally {
			releaseLock();
		}
	}

	@Override
	public final CompiledLSA readCompiled(final LSAid lsaId)
			throws SAPEREException {
		if (lsaId == null) {
			throw new IllegalArgumentException("Invalid LSA-id provided");
		}

		acquireReadLock();
		try {
			if (!checkExistencePreCondition(lsaId)) {
				throw new SAPEREException("LSA not present in LSA-space: "
						+ lsaId);
			}

			return new CompiledLSAImpl(lsaId, model.createResource(
					lsaId.toString()).listProperties());
		} catch (Exception e) {
			throw new SAPEREException("Cannot retrieve LSA: " + lsaId, e);
		} finally {
			releaseLock();
		}
	}

	@Override
	public final LSAspace removeCompiled(final CompiledLSA lsa)
			throws SAPEREException {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid compiled LSA provided");
		}

		acquireWriteLock();
		try {
			if (!checkExistencePreCondition(lsa.getLSAid())) {
				throw new SAPEREException("LSA not present in LSA-space: "
						+ lsa.getLSAid());
			}

			model.remove(lsa.getStatements());

			// Notification
			final String msg = String.format("LSA removed: %s", lsa.getLSAid());
			notifySpaceOperation(msg, lsa.getLSAid(),
					SpaceOperationType.AGENT_REMOVE);
			notifyLSAObservers(msg, retrieveLSA(lsa),
					SpaceOperationType.AGENT_REMOVE);

			return this;
		} catch (Exception ex) {
			throw new SAPEREException("Cannot remove", ex);
		} finally {
			releaseLock();
		}
	}

	@Override
	public final LSAspace updateCompiled(final CompiledLSA lsa)
			throws SAPEREException {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid compiled LSA provided");
		}

		acquireWriteLock();
		try {
			if (!checkExistencePreCondition(lsa.getLSAid())) {
				throw new SAPEREException("LSA not present in LSA-space: "
						+ lsa.getLSAid());
			}

			removeCompiled(readCompiled(lsa.getLSAid()));
			injectCompiled(lsa);

			// Notification
			final String msg = String.format("LSA updated: %s", lsa.getLSAid());
			notifySpaceOperation(msg, lsa.getLSAid(),
					SpaceOperationType.AGENT_UPDATE);
			notifyLSAObservers(msg, retrieveLSA(lsa),
					SpaceOperationType.AGENT_UPDATE);

			return this;
		} catch (Exception ex) {
			throw new SAPEREException("Cannot update", ex);
		} finally {
			releaseLock();
		}
	}

	/**
	 * <p>
	 * Reverts the CompiledLSA to a SAPERE model LSA, exploiting
	 * {@link LSAParser} capabilities.
	 * </p>
	 * 
	 * @param cLsa
	 *            The compiled LSA to be reverted
	 * @return A SAPERE model LSA
	 * @throws SAPEREException
	 *             Cannot retrieve LSA due to parsing issues
	 */
	private LSA retrieveLSA(final CompiledLSA cLsa) throws SAPEREException {
		final Model tmp = ModelFactory.createDefaultModel();
		final PipedOutputStream out = new PipedOutputStream();

		tmp.add(cLsa.getStatements());
		tmp.write(out, RDFFormat.RDF_XML.toString());

		try {
			final Set<LSA> res = parser.parseLSAs(new PipedInputStream(out),
					RDFFormat.RDF_XML);
			if (res.isEmpty() || res.size() > 1) {
				throw new SAPEREException("Invalid compiled LSA: "
						+ "should contains information about a single LSA");
			}

			for (LSA lsa : res) {
				return lsa;
			}
		} catch (IOException e) {
			throw new SAPEREException("Cannot parse LSA", e);
		}

		return null;
	}

	/**
	 * <p>
	 * Compiles the LSA.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA to be compiled
	 * @return A CompiledLSA
	 */
	private CompiledLSA compile(final LSA lsa) {
		return compiler.compile(lsa);
	}
}
