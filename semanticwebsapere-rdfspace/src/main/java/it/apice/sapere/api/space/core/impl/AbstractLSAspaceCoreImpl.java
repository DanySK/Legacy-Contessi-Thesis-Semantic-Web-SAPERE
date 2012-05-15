package it.apice.sapere.api.space.core.impl;

import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.core.CompiledEcolaw;
import it.apice.sapere.api.space.core.CompiledLSA;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.core.strategy.CustomStrategyPipeline;
import it.apice.sapere.api.space.core.strategy.CustomStrategyPipelineStep;
import it.apice.sapere.api.space.core.strategy.impl.CustomStrategyPipelineImpl;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.Query;
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

	/** A SAPERE LSA-id prefix. */
	private static final transient String LSA_PREFIX = "http://"
			+ "www.sapere-project.eu/ontologies/2012/0/sapere-model.owl#lsa";

	/** LSA-id pattern. */
	private static final transient Pattern LSA_ID_PATTERN = Pattern.compile("("
			+ LSA_PREFIX + "\\w+-\\w+)");

	/** The rdf:type. */
	private static final transient String RDF_TYPE = "http://"
			+ "www.w3.org/1999/02/22-rdf-syntax-ns#type";

	/** SpaceObservers list. */
	private final transient List<SpaceObserver> listeners;

	/** LSAs observations map. */
	private final transient Map<LSAid, List<LSAObserver>> observers;

	/** RDF Graph Store (model). */
	private final transient Model model;

	/** RDF Graph Store (the real one). */
	private final transient GraphStore modelGraph;

	/** Reference to a Jena2SAPEREConverter. */
	private final transient Jena2SAPEREConverter converter;

	/** Resource representing the LSA owl:Class. */
	private final transient Resource lsaClass;

	/**
	 * Optimization map: stores every parsed spqrql query in order to avoid
	 * re-parsing.
	 */
	private final transient 
		Map<String, Query> parsedSparqlQueries = new HashMap<String, Query>();

	/** rdf:type property. */
	private final transient Property rdfTypeProp;

	/** Provides thread-safety. */
	private final transient ReadWriteLock mutex = new ReentrantReadWriteLock(
			true);

	/** Read/Write flag. */
	private transient boolean isWriting = false;

	/** Notification enabled flag. */
	private transient boolean notificationsEnabled = true;

	/** Set of loaded ontologies. */
	private final Set<URI> loadedOntos = new HashSet<URI>();

	/** Current node-id. */
	private final transient String nodeId;

	/** Custom strategies application pipeline. */
	private final transient 
		CustomStrategyPipeline<StmtIterator> customStrategyPline;

	/**
	 * <p>
	 * Builds a new {@link AbstractLSAspaceCoreImpl}.
	 * </p>
	 * 
	 * @param lsaFactory
	 *            Reference to a {@link PrivilegedLSAFactory}
	 */
	public AbstractLSAspaceCoreImpl(
			final PrivilegedLSAFactory lsaFactory) {
		this(lsaFactory, ReasoningLevel.NONE);
	}

	/**
	 * <p>
	 * Builds a new {@link AbstractLSAspaceCoreImpl}.
	 * </p>
	 * 
	 * @param lsaFactory
	 *            Reference to a {@link PrivilegedLSAFactory}
	 * @param rLevel
	 *            The {@link ReasoningLevel}
	 */
	public AbstractLSAspaceCoreImpl(final PrivilegedLSAFactory lsaFactory, 
			final ReasoningLevel rLevel) {

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
		converter = new Jena2SAPEREConverter(lsaFactory);
		nodeId = lsaFactory.getNodeID();

		customStrategyPline = new CustomStrategyPipelineImpl<StmtIterator>();
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
	public final LSAspaceCore<StmtIterator> observe(final LSAid lsaId,
			final LSAObserver obs) {
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
	public final LSAspaceCore<StmtIterator> ignore(final LSAid lsaId,
			final LSAObserver obs) {
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
	public final LSAspaceCore<StmtIterator> clear() {
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
						this, law);
				for (String varName : law.variablesNames()) {
					match.register(varName,
							extractValue(sol.getResource(varName)), 1.0);
				}

				// 3. Return them for evaluation
				res.add(match);
			}

			// Notification
			final StringBuilder msg = new StringBuilder(
					"Trying match for eco-law");
			if (!law.getLabel().equals("")) {
				msg.append(" ").append(law.getLabel());
			}

			notifySpaceOperation(msg.toString(),
					SpaceOperationType.SYSTEM_ACTION);
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
	public final LSAspaceCore<StmtIterator> apply(final MatchingEcolaw law)
			throws SAPEREException {
		if (law == null) {
			throw new IllegalArgumentException(
					"Invalid matching eco-law provided");
		}

		acquireWriteLock();
		try {
			execUpdateQuery(modelGraph, law.getUpdateQuery());

			// Notification
			final StringBuilder msg = new StringBuilder("Applying eco-law");
			if (!law.getLabel().equals("")) {
				msg.append(" ").append(law.getLabel());
			}

			notifySpaceOperation(msg.toString(),
					SpaceOperationType.SYSTEM_ACTION);
			notifyLSAObservers(msg.toString(), retrieveUpdatedLSAs(law),
					SpaceOperationType.SYSTEM_ACTION);
		} catch (Exception ex) {
			throw new SAPEREException("Cannot apply eco-law", ex);
		} finally {
			releaseLock();
		}

		return this;
	}

	/**
	 * <p>
	 * Retrieves all the LSAs that has been modified by an applied eco-law.
	 * </p>
	 * 
	 * @param law
	 *            The applied eco-law
	 * @return A list of LSAs that has been modified by the applied eco-law
	 * @throws Exception
	 *             Cannot retrieve wanted LSA for notification
	 */
	private LSA[] retrieveUpdatedLSAs(final MatchingEcolaw law)
			throws Exception {
		final List<LSA> res = new LinkedList<LSA>();
		final Matcher matcher = LSA_ID_PATTERN.matcher(law.getUpdateQuery());
		while (matcher.find()) {
			String sLsaId = matcher.group(1);
			res.add(converter.parseLSA(model.getResource(sLsaId), model));
		}

		return res.toArray(new LSA[res.size()]);
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
	 * @param type
	 *            Event type
	 */
	private void notifySpaceOperation(final String msg,
			final SpaceOperationType type) {
		if (notificationsEnabled) {
			for (SpaceObserver obs : listeners) {
				obs.eventOccurred(new SpaceEventImpl(msg, type));
			}
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
	 *            The LSA-id of the involved LSA
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
	 * Notifies Space observers of an internal event.
	 * </p>
	 * 
	 * @param msg
	 *            Description of the event
	 * @param lsa
	 *            The involved LSA
	 * @param type
	 *            Event type
	 */
	private void notifySpaceOperation(final String msg,
			final CompiledLSA<StmtIterator> lsa, 
			final SpaceOperationType type) {
		if (notificationsEnabled) {
			for (SpaceObserver obs : listeners) {
				obs.eventOccurred(new SpaceEventImpl(msg,
						new CompiledLSA[] { lsa }, type));
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

	/**
	 * <p>
	 * Notifies observing agents (only the interested ones) that something
	 * happened to an LSA.
	 * </p>
	 * 
	 * @param msg
	 *            Description of the event
	 * @param lsas
	 *            Actual LSAs status
	 * @param type
	 *            Event type
	 */
	private void notifyLSAObservers(final String msg, final LSA[] lsas,
			final SpaceOperationType type) {
		if (notificationsEnabled) {
			for (LSA lsa : lsas) {
				final List<LSAObserver> obss = observers.get(lsa.getLSAId());
				if (obss != null) {
					for (LSAObserver obs : obss) {
						obs.eventOccurred(new LSAEventImpl(msg, lsa, type));
					}
				}
			}
		}
	}

	@Override
	public final LSAspaceCore<StmtIterator> beginRead() {
		mutex.readLock().lock();
		isWriting = false;

		return this;
	}

	@Override
	public final LSAspaceCore<StmtIterator> beginWrite() {
		mutex.writeLock().lock();
		isWriting = true;

		return this;
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
	public final LSAspaceCore<StmtIterator> inject(
			final CompiledLSA<StmtIterator> lsa) throws SAPEREException {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid compiled LSA provided");
		}

		acquireWriteLock();
		try {
			checkInjectAgainstCustomStrategy(lsa);

			if (checkExistencePreCondition(lsa.getLSAid())) {
				throw new SAPEREException("Duplicate LSA: " + lsa.getLSAid());
			}

			model.add(lsa.getStatements());

			// Notification
			final String msg = String
					.format("LSA injected: %s", lsa.getLSAid());
			notifySpaceOperation(msg, lsa, SpaceOperationType.AGENT_INJECT);
			notifyLSAObservers(msg, retrieveLSA(lsa),
					SpaceOperationType.AGENT_INJECT);

			return this;
		} catch (Exception ex) {
			throw new SAPEREException("Cannot inject", ex);
		} finally {
			releaseLock();
		}
	}

	/**
	 * <p>
	 * Checks custom strategies constraints on INJECT.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA to be checked
	 * @throws SAPEREException
	 *             Operation aborted
	 */
	private void checkInjectAgainstCustomStrategy(
			final CompiledLSA<StmtIterator> lsa) throws SAPEREException {
		CompiledLSA<StmtIterator> cLsa = lsa;
		for (CustomStrategyPipelineStep<StmtIterator> step : customStrategyPline
				.getSteps()) {
			try {
				cLsa = step.handleInject(cLsa);
			} catch (SAPEREException e) {
				throw new SAPEREException("Operation abored", e);
			}
		}
	}

	@Override
	public final CompiledLSA<StmtIterator> read(final LSAid lsaId)
			throws SAPEREException {
		if (lsaId == null) {
			throw new IllegalArgumentException("Invalid LSA-id provided");
		}

		acquireReadLock();
		try {
			checkReadAgainstCustomStrategy(lsaId);

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

	/**
	 * <p>
	 * Checks custom strategies constraints on READ.
	 * </p>
	 * 
	 * @param lsaId
	 *            The LSA-id to be checked
	 * @throws SAPEREException
	 *             Operation aborted
	 */
	private void checkReadAgainstCustomStrategy(final LSAid lsaId)
			throws SAPEREException {
		LSAid cLsaId = lsaId;
		for (CustomStrategyPipelineStep<StmtIterator> step : customStrategyPline
				.getSteps()) {
			try {
				cLsaId = step.handleRead(cLsaId);
			} catch (SAPEREException e) {
				throw new SAPEREException("Operation abored", e);
			}
		}
	}

	@Override
	public final LSAspaceCore<StmtIterator> remove(
			final CompiledLSA<StmtIterator> lsa) throws SAPEREException {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid compiled LSA provided");
		}

		acquireWriteLock();
		try {
			checkRemoveAgainstCustomStrategy(lsa);

			if (!checkExistencePreCondition(lsa.getLSAid())) {
				throw new SAPEREException("LSA not present in LSA-space: "
						+ lsa.getLSAid());
			}

			model.remove(lsa.getStatements());

			// Notification
			final String msg = String.format("LSA removed: %s", lsa.getLSAid());
			notifySpaceOperation(msg, lsa, SpaceOperationType.AGENT_REMOVE);
			notifyLSAObservers(msg, retrieveLSA(lsa),
					SpaceOperationType.AGENT_REMOVE);

			return this;
		} catch (Exception ex) {
			throw new SAPEREException("Cannot remove", ex);
		} finally {
			releaseLock();
		}
	}

	/**
	 * <p>
	 * Checks custom strategies constraints on REMOVE.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA to be checked
	 * @throws SAPEREException
	 *             Operation aborted
	 */
	private void checkRemoveAgainstCustomStrategy(
			final CompiledLSA<StmtIterator> lsa) throws SAPEREException {
		CompiledLSA<StmtIterator> cLsa = lsa;
		for (CustomStrategyPipelineStep<StmtIterator> step : customStrategyPline
				.getSteps()) {
			try {
				cLsa = step.handleRemove(cLsa);
			} catch (SAPEREException e) {
				throw new SAPEREException("Operation abored", e);
			}
		}
	}

	@Override
	public final LSAspaceCore<StmtIterator> update(
			final CompiledLSA<StmtIterator> lsa) throws SAPEREException {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid compiled LSA provided");
		}

		acquireWriteLock();
		try {
			checkUpdateAgainstCustomStrategy(lsa);

			if (!checkExistencePreCondition(lsa.getLSAid())) {
				throw new SAPEREException("LSA not present in LSA-space: "
						+ lsa.getLSAid());
			}

			disableNotifications();
			remove(read(lsa.getLSAid()));
			inject(lsa);
			enableNotifications();

			// Notification
			final String msg = String.format("LSA updated: %s", lsa.getLSAid());
			notifySpaceOperation(msg, lsa, SpaceOperationType.AGENT_UPDATE);
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
	 * Checks custom strategies constraints on UPDATE.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA to be checked
	 * @throws SAPEREException
	 *             Operation aborted
	 */
	private void checkUpdateAgainstCustomStrategy(
			final CompiledLSA<StmtIterator> lsa) throws SAPEREException {
		CompiledLSA<StmtIterator> cLsa = lsa;
		for (CustomStrategyPipelineStep<StmtIterator> step : customStrategyPline
				.getSteps()) {
			try {
				cLsa = step.handleUpdate(cLsa);
			} catch (SAPEREException e) {
				throw new SAPEREException("Operation abored", e);
			}
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
		// Flyweight pattern on queries: Map<String, Query> (optimization)
		Query qObj = parsedSparqlQueries.get(query);
		if (qObj == null) {
			qObj = QueryFactory.create(query);
			parsedSparqlQueries.put(query, qObj);
		}

		return QueryExecutionFactory.create(qObj, aModel).execSelect();
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

	@Override
	public final CustomStrategyPipeline<StmtIterator> 
			getCustomStrategyPipeline() {
		return customStrategyPline;
	}
}
