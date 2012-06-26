package it.apice.sapere.api.space.core.impl;

import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.RDFFormat;
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
import it.apice.sapere.api.utils.Jena2SAPEREConverter;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindswap.pellet.jena.PelletInfGraph;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.GraphStoreFactory;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;

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

	/** RDF Graph Store (model). */
	private final transient Model model;

	/** RDF Graph Store (the real one). */
	private final transient GraphStore modelGraph;

	/** Reference to a Jena2SAPEREConverter. */
	private final transient Jena2SAPEREConverter converter;

	/** Resource representing the LSA owl:Class. */
	private final transient Resource lsaClass;

	/** rdf:type property. */
	private final transient Property rdfTypeProp;

	/** Set of loaded ontologies. */
	private final Set<URI> loadedOntos = new HashSet<URI>();

	/* === OBSERVATION (begin) === */

	/** SpaceObservers list. */
	private final transient List<SpaceObserver> listeners;

	/** LSAs observations map. */
	private final transient Map<LSAid, List<LSAObserver>> observers;

	/** Threads which notifies observers (one per type). */
	private final transient ExecutorService asapExec = Executors
			.newFixedThreadPool(2);

	/* ==== OBSERVATION (end) ==== */

	/** Current node-id. */
	private final transient String nodeId;

	/** Custom strategies application pipeline. */
	private final transient 
			CustomStrategyPipeline<StmtIterator> customStrategyPline;

	/* === OPTIMIZATION STUFFS (begin) === */

	/** Default optimization policy. */
	private static final transient boolean DEFAULT_OPT_ENABLED = true;

	/** Let optimization be enabled (or not) in future LSA-space instances. */
	private static transient boolean enableOptimization = DEFAULT_OPT_ENABLED;

	/** Optimization enabled flag. */
	private final transient boolean optEnabled = enableOptimization;

	/** Stores every parsed SPARQL query (avoid re-parsing). */
	private final transient Map<String, Query> parsedSparqlQueries = 
			new HashMap<String, Query>();

	/** Stores every parsed SPARUL query (avoid re-parsing). */
	private final transient Map<String, UpdateRequest> parsedSparulQueries = 
			new HashMap<String, UpdateRequest>();

	/** Map of each SPARUL Query to the applicable results (so bindings). */
	private final transient Map<String, List<MatchResult>> queryResults = 
			new HashMap<String, List<MatchResult>>();

	/** Map of each {@link MatchResult} to the related {@link QuerySolution}. */
	private final transient Map<MatchResult, QuerySolution> relBindings = 
			new HashMap<MatchResult, QuerySolution>();

	/* ==== OPTIMIZATION STUFFS (end) ==== */

	/** LSA Observers list mutex. */
	private final transient Lock lsaObsMutex = new ReentrantLock();

	/** Space Observers list mutex. */
	private final transient Lock spaceObsMutex = new ReentrantLock();

	/** Default Thread-Safety enforcement policy. */
	public static final transient boolean DEFAULT_ENFORCE_THREAD_SAFETY = true;

	/** Flag that enables scheduled Pellet update. */
	private transient boolean enforceThreadSafety = true;

	/** Reference to Pellet inference graph. */
	private transient PelletInfGraph infGraph;

	/**
	 * <p>
	 * Builds a new {@link AbstractLSAspaceCoreImpl}.
	 * </p>
	 * 
	 * @param lsaFactory
	 *            Reference to a {@link PrivilegedLSAFactory}
	 */
	public AbstractLSAspaceCoreImpl(final PrivilegedLSAFactory lsaFactory) {
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
		model.enterCriticalSection(Model.READ);
	}

	/**
	 * <p>
	 * Acquires write lock on model.
	 * </p>
	 */
	private void acquireWriteLock() {
		model.enterCriticalSection(Model.WRITE);
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

		lsaObsMutex.lock();
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
			lsaObsMutex.unlock();
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

		lsaObsMutex.lock();
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
			lsaObsMutex.unlock();
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
		if (optEnabled) {
			clearCache(law);
		}

		acquireReadLock();
		try {
			runPelletClassify();

			// 1. Run query
			final ResultSet iter = execQuery(model, law.getMatchQuery());

			while (iter.hasNext()) {
				final QuerySolution sol = (QuerySolution) iter.next();
				// 2. Extract bindings
				final MutableMatchResult match = new MutableMatchResultImpl(
						this, law);
				for (String vName : law.variablesNames()) {
					match.register(vName, extractValue(sol.get(vName)), 1.0);
				}

				// 2b. Registers bindings for optimization
				if (optEnabled) {
					cacheBindings(law, match, sol);
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
					SpaceOperationType.SYSTEM_MATCH);
		} catch (Exception ex) {
			throw new SAPEREException("Cannot complete match process", ex);
		} finally {
			releaseLock();
		}

		return res.toArray(new MatchResult[res.size()]);
	}

	/**
	 * <p>
	 * Optimization enforcer: saves the binding under the hood, for appliance.
	 * </p>
	 * 
	 * @param law
	 *            The involved eco-law
	 * @param match
	 *            The actual match
	 * @param bindings
	 *            The related binding (QuerySolution)
	 */
	private void cacheBindings(final CompiledEcolaw law,
			final MutableMatchResult match, final QuerySolution bindings) {
		final String key = law.getUpdateQueryTemplate().getPlainQuery();
		List<MatchResult> res = queryResults.get(key);
		if (res == null) {
			res = new LinkedList<MatchResult>();
			queryResults.put(key, res);
		}

		res.add(match);
		relBindings.put(match, bindings);
	}

	/**
	 * <p>
	 * Optimization enforcer: clears stored bindings in order to free memory.
	 * </p>
	 * 
	 * @param law
	 *            The involved law
	 */
	private void clearCache(final CompiledEcolaw law) {
		final List<MatchResult> res = queryResults.get(law
				.getUpdateQueryTemplate().getPlainQuery());
		if (res != null) {
			for (MatchResult mres : res) {
				relBindings.remove(mres);
			}

			res.clear();
		}
	}

	/**
	 * <p>
	 * Retrieves the cached binding and clears it.
	 * </p>
	 * 
	 * @param key
	 *            Cache key (the matchresult)
	 * @return The Binding
	 */
	private QuerySolution retrieveBindings(final MatchResult key) {
		return relBindings.remove(key);
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
	private String extractValue(final RDFNode res) {
		if (res.isLiteral()) {
			return String.format("\"%s\"", res.asNode().getLiteralValue()
					.toString());
		} else if (res.isResource()) {
			return String.format("<%s>", res.asNode().getURI());
		}

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
			execUpdateQuery(modelGraph, law);

			// Notification
			final StringBuilder msg = new StringBuilder("Applying eco-law");
			if (!law.getLabel().equals("")) {
				msg.append(" ").append(law.getLabel());
			}

			final String[] uIds = retrieveUpdatedLSAids(law);
			final List<CompiledLSA<StmtIterator>> uLsas = 
					new LinkedList<CompiledLSA<StmtIterator>>();
			for (String id : uIds) {
				uLsas.add(new CompiledLSAImpl(converter.getFactory()
						.createLSAid(URI.create(id)), extractLSAData(id)));
			}

			notifySpaceOperation(msg.toString(), uLsas,
					SpaceOperationType.SYSTEM_APPLY);
			notifyLSAObservers(msg.toString(), uLsas,
					SpaceOperationType.SYSTEM_APPLY);
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
	private String[] retrieveUpdatedLSAids(final MatchingEcolaw law)
			throws Exception {
		final Set<String> ids = new HashSet<String>();
		final Matcher matcher = LSA_ID_PATTERN.matcher(law.getUpdateQuery());
		while (matcher.find()) {
			final String sLsaId = matcher.group(1);
			ids.add(sLsaId);
		}

		return ids.toArray(new String[ids.size()]);
	}

	@Override
	public final void addSpaceObserver(final SpaceObserver obs) {
		if (obs == null) {
			throw new IllegalArgumentException("Invalid space observer");
		}

		spaceObsMutex.lock();
		try {
			listeners.add(obs);
		} finally {
			spaceObsMutex.unlock();
		}
	}

	@Override
	public final void removeSpaceObserver(final SpaceObserver obs) {
		if (obs == null) {
			throw new IllegalArgumentException("Invalid space observer");
		}

		spaceObsMutex.lock();
		try {
			listeners.remove(obs);
		} finally {
			spaceObsMutex.unlock();
		}
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
		asapExec.execute(new Runnable() {

			@Override
			public void run() {
				spaceObsMutex.lock();
				try {
					for (SpaceObserver obs : listeners) {
						obs.eventOccurred(new SpaceEventImpl(msg, type));
					}
				} finally {
					spaceObsMutex.unlock();
				}
			}
		});
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
		asapExec.execute(new Runnable() {

			@Override
			public void run() {
				spaceObsMutex.lock();
				try {
					for (SpaceObserver obs : listeners) {
						obs.eventOccurred(new SpaceEventImpl(msg,
								new LSAid[] { id }, type));
					}
				} finally {
					spaceObsMutex.unlock();
				}
			}
		});
	}

	/**
	 * <p>
	 * Notifies Space observers of an internal event.
	 * </p>
	 * 
	 * @param msg
	 *            Description of the event
	 * @param lsas
	 *            The involved LSAs
	 * @param type
	 *            Event type
	 */
	private void notifySpaceOperation(final String msg,
			final List<CompiledLSA<StmtIterator>> lsas,
			final SpaceOperationType type) {
		asapExec.execute(new Runnable() {

			@Override
			public void run() {
				spaceObsMutex.lock();
				try {
					for (SpaceObserver obs : listeners) {
						obs.eventOccurred(new SpaceEventImpl(msg, lsas
								.toArray(new CompiledLSA<?>[lsas.size()]), 
								type));
					}
				} finally {
					spaceObsMutex.unlock();
				}
			}
		});
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
		asapExec.execute(new Runnable() {

			@Override
			public void run() {
				spaceObsMutex.lock();
				try {
					for (SpaceObserver obs : listeners) {
						obs.eventOccurred(new SpaceEventImpl(msg,
								new CompiledLSA[] { lsa }, type));
					}
				} finally {
					spaceObsMutex.unlock();
				}
			}
		});
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
		asapExec.execute(new Runnable() {

			@Override
			public void run() {
				lsaObsMutex.lock();
				try {
					final List<LSAObserver> obss = 
							observers.get(lsa.getLSAId());
					if (obss != null) {
						for (LSAObserver obs : obss) {
							obs.eventOccurred(new LSAEventImpl(msg, lsa, type));
						}
					}
				} finally {
					lsaObsMutex.unlock();
				}
			}
		});
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
	 * @throws SAPEREException
	 *             Cannot parse LSA
	 */
	private void notifyLSAObservers(final String msg,
			final List<CompiledLSA<StmtIterator>> lsas,
			final SpaceOperationType type) throws SAPEREException {
		for (CompiledLSA<StmtIterator> cLsa : lsas) {
			notifyLSAObservers(msg, retrieveLSA(cLsa), type);
		}
	}

	@Override
	public final LSAspaceCore<StmtIterator> beginRead() {
		// System.err.println("§§§ BEGIN (READ)");
		return this;
	}

	@Override
	public final LSAspaceCore<StmtIterator> beginWrite() {
		// System.err.println("§§§ BEGIN (WRITE)");
		return this;
	}

	@Override
	public final void done() {
		// System.err.println("§§§ END");
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

			doInject(lsa);

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
	 * Executes the inject operation.
	 * </p>
	 * 
	 * @param lsa
	 *            The lsa to be injected
	 * @return The injected LSA
	 * @throws SAPEREException
	 *             Cannot inject
	 */
	public CompiledLSA<StmtIterator> doInject(
			final CompiledLSA<StmtIterator> lsa) throws SAPEREException {
		// Raises WARNING (Unsupported axiom): seems relative to some weird
		// interaction between Jena and Pellet, which concerns namespaces
		// and prefixes
		if (checkExistencePreCondition(lsa.getLSAid())) {
			throw new SAPEREException("Duplicate LSA: " + lsa.getLSAid());
		}

		model.add(lsa.getStatements());
		return lsa;
	}

	/**
	 * Runs the classifier of Pellet in order to update inferences (long run
	 * operation).
	 */
	private void runPelletClassify() {
		if (enforceThreadSafety && infGraph != null) {
			// Explicit Pellet refresh (for thread-safety)
			infGraph.classify();
		}
	}

	/**
	 * <p>
	 * Registers the reference to the graph to be classified, if available.
	 * </p>
	 * 
	 * @param g
	 *            The graph to be classified by the reasoner (only Pellet
	 *            supported)
	 */
	protected void setInfGraph(final Graph g) {
		if (g instanceof PelletInfGraph) {
			infGraph = ((PelletInfGraph) g);
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
				throw new SAPEREException("Operation aborted", e);
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
			runPelletClassify();
			checkReadAgainstCustomStrategy(lsaId);

			final CompiledLSA<StmtIterator> res = doRead(lsaId);

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
	 * Executes the read operation.
	 * </p>
	 * 
	 * @param lsaId
	 *            The LSA-id to be searched
	 * @return The read LSA
	 * @throws SAPEREException
	 *             Cannot read
	 */
	public CompiledLSA<StmtIterator> doRead(final LSAid lsaId)
			throws SAPEREException {
		if (!checkExistencePreCondition(lsaId)) {
			throw new SAPEREException("LSA not present in LSA-space: " + lsaId);
		}

		return new CompiledLSAImpl(lsaId, extractLSAData(lsaId));
	}

	/**
	 * <p>
	 * Extracts all data related to the LSA-id that has been provided.
	 * </p>
	 * <p>
	 * The method gets all the properties relative to the LSA-id and explores
	 * all (sub) blank-nodes.
	 * </p>
	 * 
	 * @param lsaId
	 *            The id of the LSA to be extracted (as a String)
	 * @return An interator over all information
	 */
	private StmtIterator extractLSAData(final String lsaId) {
		final Model data = ModelFactory.createDefaultModel();
		extractLSAData(model.createResource(lsaId), data);
		return data.listStatements();
	}

	/**
	 * <p>
	 * Extracts all data related to the LSA-id that has been provided.
	 * </p>
	 * <p>
	 * The method gets all the properties relative to the LSA-id and explores
	 * all (sub) blank-nodes.
	 * </p>
	 * 
	 * @param lsaId
	 *            The id of the LSA to be extracted
	 * @return An interator over all information
	 */
	private StmtIterator extractLSAData(final LSAid lsaId) {
		final Model data = ModelFactory.createDefaultModel();
		extractLSAData(model.createResource(lsaId.toString()), data);
		return data.listStatements();
	}

	/**
	 * <p>
	 * Explores the provided subject in order to extract data.
	 * </p>
	 * 
	 * @param subject
	 *            The resource to be explored
	 * @param res
	 *            The model in which all information should be collected in
	 *            order to be returned
	 */
	private void extractLSAData(final Resource subject, final Model res) {
		final List<Statement> stmts = subject.listProperties().toList();
		if (stmts != null) {
			res.add(stmts);

			for (Statement stm : stmts) {
				final RDFNode obj = stm.getObject();

				if (obj.isResource() && obj.isAnon()) {
					extractLSAData(obj.asResource(), res);
				}
			}
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
				throw new SAPEREException("Operation aborted", e);
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
			// runPelletClassify();
			checkRemoveAgainstCustomStrategy(lsa);

			if (!checkExistencePreCondition(lsa.getLSAid())) {
				throw new SAPEREException("LSA not present in LSA-space: "
						+ lsa.getLSAid());
			}

			final CompiledLSA<StmtIterator> rLsa = doRemove(lsa.getLSAid());

			// Notification
			final String msg = String.format("LSA removed: %s", lsa.getLSAid());
			notifySpaceOperation(msg, rLsa, SpaceOperationType.AGENT_REMOVE);
			notifyLSAObservers(msg, retrieveLSA(rLsa),
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
	 * Executes the remove operation.
	 * </p>
	 * 
	 * @param lsaId
	 *            The LSA-id of the LSA to be removed
	 * @return The removed LSA
	 * @throws SAPEREException
	 *             Cannot remove
	 */
	public CompiledLSA<StmtIterator> doRemove(final LSAid lsaId)
			throws SAPEREException {
		final CompiledLSA<StmtIterator> rLsa = doRead(lsaId);
		model.remove(rLsa.getStatements());
		return rLsa;
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
				throw new SAPEREException("Operation aborted", e);
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

			doRemove(lsa.getLSAid());
			doInject(lsa);

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
				throw new SAPEREException("Operation aborted", e);
			}
		}
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
				// Returns the first LSA in the set
				return lsa;
			}
		} catch (Exception e) {
			throw new SAPEREException("Cannot parse LSA", e);
		}

		return null;
	}

	@Override
	public final void loadOntology(final URI ontoURI) throws SAPEREException {
		loadOntology(ontoURI, RDFFormat.RDF_XML);
	}

	@Override
	public final void loadOntology(final URI ontoURI, final RDFFormat format)
			throws SAPEREException {
		if (ontoURI == null) {
			throw new IllegalArgumentException("Invalid URI provided");
		}

		acquireWriteLock();
		try {
			if (loadedOntos.add(ontoURI)) {
				model.read(ontoURI.toString(), format.toString());
			}
		} catch (Exception ex) {
			throw new SAPEREException(
					"Unable to retrieve and load the ontology at "
							+ ontoURI.toString(), ex);
		} finally {
			releaseLock();
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

		assert qObj.isSelectType();
		return QueryExecutionFactory.create(qObj, aModel).execSelect();
	}

	/**
	 * <p>
	 * Executes a SPARQL Update query on provided model.
	 * </p>
	 * 
	 * @param aGraphStore
	 *            The queried model's graph store
	 * @param law
	 *            The ecolaw to be applied
	 */
	private void execUpdateQuery(final GraphStore aGraphStore,
			final MatchingEcolaw law) {
		if (optEnabled) {
			final String query = law.getAppliedMatch().getRelCompiledEcolaw()
					.getUpdateQueryTemplate().getPlainQuery();
			UpdateRequest uReq = parsedSparulQueries.get(query);
			if (uReq == null) {
				uReq = UpdateFactory.create(query);
				parsedSparulQueries.put(query, uReq);
			}

			final QuerySolution bindings = retrieveBindings(law
					.getAppliedMatch());

			assert bindings != null;
			UpdateExecutionFactory.create(uReq, aGraphStore, bindings)
					.execute();
		} else {
			UpdateAction.parseExecute(law.getUpdateQuery(), aGraphStore);
		}
	}

	@Override
	public final String toString() {
		final StringWriter strW = new StringWriter();
		acquireReadLock();
		try {
			model.write(strW, "TURTLE");
			return "===== SAPERE :: LSA-space (sapere:" + nodeId.split("#")[1]
					+ ") =====\n" + strW.toString();
		} finally {
			releaseLock();
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
	public final 
			CustomStrategyPipeline<StmtIterator> getCustomStrategyPipeline() {
		return customStrategyPline;
	}

	/**
	 * <p>
	 * Lets the optimization be enabled (or not) when the LSA-space will be
	 * created.
	 * </p>
	 * 
	 * @param en
	 *            True = will enabled, False = will disable
	 */
	public static void setOptimizationEnabled(final boolean en) {
		enableOptimization = en;
	}

	/**
	 * <p>
	 * Checks if optimization is enabled (Future instances will use
	 * optimization).
	 * </p>
	 * 
	 * @return True if enabled, false otherwise
	 */
	public static boolean getOptimizationEnabled() {
		return enableOptimization;
	}
}
