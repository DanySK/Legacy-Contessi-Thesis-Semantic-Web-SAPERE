package it.apice.sapere.api.space.match.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.match.MutableMatchResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * <p>
 * This class implements the {@link MutableMatchResult} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class MutableMatchResultImpl implements MutableMatchResult {

	/** Reference to LSA-space. */
	private final transient LSAspaceCore<StmtIterator> space;

	/** Assignments map. */
	private final Map<String, String> assignments;

	/** Scores map. */
	private final Map<String, Double> scores;

	/** Mutex. */
	private final transient Lock mutex = new ReentrantLock();

	/**
	 * <p>
	 * Builds a new {@link MutableMatchResultImpl}.
	 * </p>
	 * 
	 * @param relSpace
	 *            The LSA-space that produced the match
	 */
	public MutableMatchResultImpl(final LSAspaceCore<StmtIterator> relSpace) {
		if (relSpace == null) {
			throw new IllegalArgumentException("Invalid space provided");
		}

		space = relSpace;
		assignments = new HashMap<String, String>();
		scores = new HashMap<String, Double>();
	}

	@Override
	public final String lookup(final String varName) throws SAPEREException {
		mutex.lock();
		try {
			final String val = assignments.get(varName);
			if (val == null) {
				throw new SAPEREException(String.format(
						"No binding for variable \"%s\"", varName));
			}

			return val;
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public final Double getAssignmentScore(final String varName)
			throws SAPEREException {
		mutex.lock();
		try {
			final Double val = scores.get(varName);
			if (val == null) {
				throw new SAPEREException(String.format(
						"No binding (and so no score) for variable \"%s\"",
						varName));
			}

			return val;
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public final LSAspaceCore<?> getLSAspace() {
		return space;
	}

	@Override
	public final void register(final String varName, final String value,
			final double score) throws SAPEREException {
		mutex.lock();
		try {
			if (assignments.containsKey(varName)) {
				throw new SAPEREException("Variable binding clash (var="
						+ varName + ")");
			}
			assignments.put(varName, value);
			scores.put(varName, score);
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public final void reset() {
		mutex.lock();
		try {
			assignments.clear();
			scores.clear();
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public final String[] getAllMatchedVariablesNames() {
		final Set<String> vars = assignments.keySet();
		return vars.toArray(new String[vars.size()]);
	}

}
