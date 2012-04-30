package it.apice.sapere.api.space.core.impl;

import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.api.space.core.CompiledEcolaw;
import it.apice.sapere.api.space.core.CompiledLSA;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.match.MatchResult;
import it.apice.sapere.api.space.match.MatchingEcolaw;
import it.apice.sapere.api.space.match.MutableMatchResult;
import it.apice.sapere.api.space.match.impl.MutableMatchResultImpl;
import it.apice.sapere.api.space.observation.LSAObserver;
import it.apice.sapere.api.space.observation.SpaceObserver;
import it.apice.sapere.api.space.observation.SpaceOperationType;
import it.apice.sapere.space.impl.Jena2SAPEREConverter;
import it.apice.sapere.space.observation.impl.LSAEventImpl;
import it.apice.sapere.space.observation.impl.SpaceEventImpl;

import java.io.StringWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.GraphStoreFactory;
import com.hp.hpl.jena.update.UpdateFactory;

/**
 * <p>
 * Implementation of {@link LSAspaceCore}.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractLSAspaceCoreImpl implements
		LSAspaceCore<StmtIterator> {

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

	/** RDF Graph Store (model). */
	private final transient Model model;

	/** RDF Graph Store (real one). */
	private final transient GraphStore modelGraph;

	/** Reference to an LSACompiler. */
	private final transient LSACompiler<StmtIterator> compiler;

	/** Reference to a Jena2SAPEREConverter. */
	private final transient Jena2SAPEREConverter converter;

	/** Resource representing the LSA owl:Class. */
	private final transient Resource lsaClass;

	/** rdf:type property. */
	private final transient Property rdfTypeProp;

	/** Provides thread-safety. */
	private final transient ReadWriteLock mutex = new ReentrantReadWriteLock();

	/** Read/Write flag. */
	private transient boolean isWriting = false;

	/** Notification enabled flag. */
	private transient boolean notificationsEnabled = true;

	/** Set of loaded ontologies. */
	private final Set<URI> loadedOntos = new HashSet<URI>();

	/** Current node-id. */
	private final transient String nodeId;

	/**
	 * <p>
	 * Builds a new {@link AbstractLSAspaceCoreImpl}.
	 * </p>
	 * 
	 * @param lsaCompiler
	 *            Reference to a {@link LSACompiler}
	 * @param lsaFactory
	 *            Reference to a {@link PrivilegedLSAFactory}
	 */
	public AbstractLSAspaceCoreImpl(
			final LSACompiler<StmtIterator> lsaCompiler,
			final PrivilegedLSAFactory lsaFactory) {
		this(lsaCompiler, lsaFactory, ReasoningLevel.NONE);
	}

	/**
	 * <p>
	 * Builds a new {@link AbstractLSAspaceCoreImpl}.
	 * </p>
	 * 
	 * @param lsaCompiler
	 *            Reference to a {@link LSACompiler}
	 * @param lsaFactory
	 *            Reference to a {@link PrivilegedLSAFactory}
	 * @param rLevel
	 *            The {@link ReasoningLevel}
	 */
	public AbstractLSAspaceCoreImpl(
			final LSACompiler<StmtIterator> lsaCompiler,
			final PrivilegedLSAFactory lsaFactory, 
			final ReasoningLevel rLevel) {
		if (lsaCompiler == null) {
			throw new IllegalArgumentException("Invalid LSACompiler provided");
		}

		if (lsaFactory == null) {
			throw new IllegalArgumentException("Invalid LSAFactory provided");
		}

		// Observation initialization
		listeners = new LinkedList<SpaceObserver>();
		observers = new HashMap<LSAid, List<LSAObserver>>();

		// RDFModel initialization
		model = initRDFGraphModel(rLevel);
		lsaClass = model.createResource(LSA_TYPE);
		modelGraph = GraphStoreFactory.create(DatasetFactory.create(model));

		// Other stuff initialization
		rdfTypeProp = model.createProperty(RDF_TYPE);
		compiler = lsaCompiler;
		converter = new Jena2SAPEREConverter(lsaFactory);
		nodeId = lsaFactory.getNodeID();
	}

	/**
	 * <p>
	 * Creates and returns an instance of Jena's Model, where LSAs will be
	 * stored.
	 * </p>
	 * 
	 * @param rLevel
	 *            The {@link ReasoningLevel}
	 * @return A reference to a (Jena's) Model
	 */
	protected abstract Model initRDFGraphModel(ReasoningLevel rLevel);

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
		return model.contains(lsa, rdfTypeProp, lsaClass);
	}

	/**
	 * <p>
	 * Acquires read lock on model.
	 * </p>
	 */
	private void acquireReadLock() {
		model.enterCriticalSection(true/* Model.READ */);
	}

	/**
	 * <p>
	 * Acquires write lock on model.
	 * </p>
	 */
	private void acquireWriteLock() {
		model.enterCriticalSection(false/* Model.WRITE */);
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
	public final MatchResult[] match(final CompiledEcolaw law)
			throws SAPEREException {
		if (law == null) {
			throw new IllegalArgumentException(
					"Invalid compiled eco-law provided");
		}

		final List<MatchResult> res = new LinkedList<MatchResult>();

		acquireReadLock();
		try {
			// 1. Run query
			final ResultSet iter = execQuery(model, law.getMatchQuery());

			while (iter.hasNext()) {
				final QuerySolution sol = (QuerySolution) iter.next();
				// 2. Extract bindings
				final MutableMatchResult match = new MutableMatchResultImpl(
						this);
				for (String varName : law.variablesNames()) {
					match.register(varName,
							extractValue(sol.getResource(varName)), 1.0);
				}

				// 3. Return them for evaluation
				res.add(match);
			}
		} catch (Exception ex) {
			throw new SAPEREException("Cannot complete match process", ex);
		} finally {
			releaseLock();
		}

		return res.toArray(new MatchResult[res.size()]);
	}

	/**
	 * <p>
	 * Extracts a String that represents the provided resource's value.
	 * </p>
	 * 
	 * @param res
	 *            The resource whose value should be extracted
	 * @return A String representation of the Resource's value
	 */
	private String extractValue(final Resource res) {
		return res.toString();
	}

	@Override
	public final LSAspace apply(final MatchingEcolaw law)
			throws SAPEREException {
		if (law == null) {
			throw new IllegalArgumentException(
					"Invalid matching eco-law provided");
		}

		acquireWriteLock();
		try {
			execUpdateQuery(modelGraph, law.getUpdateQuery());
		} catch (Exception ex) {
			throw new SAPEREException("Cannot apply eco-law", ex);
		} finally {
			releaseLock();
		}

		return this;
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
		if (notificationsEnabled) {
			for (SpaceObserver obs : listeners) {
				obs.eventOccurred(new SpaceEventImpl(msg, new LSAid[] { id },
						type));
			}
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
		if (notificationsEnabled) {
			final List<LSAObserver> obss = observers.get(lsa.getLSAId());
			if (obss != null) {
				for (LSAObserver obs : obss) {
					obs.eventOccurred(new LSAEventImpl(msg, lsa, type));
				}
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
	public final LSAspace injectCompiled(final CompiledLSA<StmtIterator> lsa)
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
	public final CompiledLSA<StmtIterator> readCompiled(final LSAid lsaId)
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

			final CompiledLSAImpl res = new CompiledLSAImpl(lsaId, model
					.createResource(lsaId.toString()).listProperties());
			notifySpaceOperation(String.format("LSA read: %s", lsaId), lsaId,
					SpaceOperationType.AGENT_READ);

			return res;
		} catch (Exception e) {
			throw new SAPEREException("Cannot retrieve LSA: " + lsaId, e);
		} finally {
			releaseLock();
		}
	}

	@Override
	public final LSAspace removeCompiled(final CompiledLSA<StmtIterator> lsa)
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
	public final LSAspace updateCompiled(final CompiledLSA<StmtIterator> lsa)
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

			disableNotifications();
			removeCompiled(readCompiled(lsa.getLSAid()));
			injectCompiled(lsa);
			enableNotifications();

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
	 * Disables notifications to (LSA|Space)Observers.
	 * </p>
	 */
	private void disableNotifications() {
		notificationsEnabled = false;
	}

	/**
	 * <p>
	 * Enables notifications to (LSA|Space)Observers.
	 * </p>
	 */
	private void enableNotifications() {
		notificationsEnabled = true;
	}

	/**
	 * <p>
	 * Reverts the CompiledLSA to a SAPERE model LSA.
	 * </p>
	 * 
	 * @param cLsa
	 *            The compiled LSA to be reverted
	 * @return A SAPERE model LSA
	 * @throws SAPEREException
	 *             Cannot retrieve LSA due to parsing issues
	 */
	private LSA retrieveLSA(final CompiledLSA<StmtIterator> cLsa)
			throws SAPEREException {
		final Model tmp = ModelFactory.createDefaultModel();

		tmp.add(cLsa.getStatements());
		if (tmp.isEmpty()) {
			throw new SAPEREException("LSA is empty");
		}

		try {
			final Set<LSA> res = converter.parseJenaModel(tmp);
			if (res.isEmpty() || res.size() > 1) {
				throw new SAPEREException("Invalid compiled LSA: "
						+ "should contains information about a single LSA");
			}

			for (LSA lsa : res) {
				return lsa;
			}
		} catch (Exception e) {
			throw new SAPEREException("Cannot parse LSA", e);
		}

		return null;
	}

	@Override
	public final void loadOntology(final URI ontoURI) throws SAPEREException {
		if (ontoURI == null) {
			throw new IllegalArgumentException("Invalid URI provided");
		}

		try {
			if (loadedOntos.add(ontoURI)) {
				model.read(ontoURI.toString());
			}
		} catch (Exception ex) {
			throw new SAPEREException(
					"Unable to retrieve and load the ontology at "
							+ ontoURI.toString(), ex);
		}
	}

	@Override
	public final URI[] getLoadedOntologies() {
		return loadedOntos.toArray(new URI[loadedOntos.size()]);
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
	private CompiledLSA<StmtIterator> compile(final LSA lsa) {
		return compiler.compile(lsa);
	}

	/**
	 * <p>
	 * Executes a SPARQL query on provided model.
	 * </p>
	 * 
	 * @param aModel
	 *            The queried model
	 * @param query
	 *            The query to be executed (provided as a String)
	 * @return Query's result set
	 */
	private ResultSet execQuery(final Model aModel, final String query) {
		return QueryExecutionFactory.create(QueryFactory.create(query), aModel)
				.execSelect();
	}

	/**
	 * <p>
	 * Executes a SPARQL Update query on provided model.
	 * </p>
	 * 
	 * @param aGraphStore
	 *            The queried model's graph store
	 * @param query
	 *            The update query to be executed
	 */
	private void execUpdateQuery(final GraphStore aGraphStore,
			final String query) {
		UpdateFactory.create(query).exec(aGraphStore);
	}

	@Override
	public final String toString() {
		final StringWriter strW = new StringWriter();
		try {
			model.write(strW, "TURTLE");
			return "===== SAPERE :: LSA-space (sapere:" + nodeId.split("#")[1]
					+ ") =====\n" + strW.toString();
		} finally {
			if (strW != null) {
				try {
					strW.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
