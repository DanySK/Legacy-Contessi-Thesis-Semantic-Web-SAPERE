package it.apice.sapere.api.space.match.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.space.core.CompiledEcolaw;
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

	/** The eco-law which produced this match. */
	private final CompiledEcolaw law;

	/** Mutex. */
	private final transient Lock mutex = new ReentrantLock();

	/**
	 * <p>
	 * Builds a new {@link MutableMatchResultImpl}.
	 * </p>
	 * 
	 * @param relSpace
	 *            The LSA-space that produced the match
	 * @param relEcolaw
	 *            The eco-law used to produce this match
	 */
	public MutableMatchResultImpl(final LSAspaceCore<StmtIterator> relSpace,
			final CompiledEcolaw relEcolaw) {
		if (relSpace == null) {
			throw new IllegalArgumentException("Invalid space provided");
		}

		space = relSpace;
		assignments = new HashMap<String, String>();
		scores = new HashMap<String, Double>();
		law = relEcolaw;
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
						+ varName + "; bindings= " + assignments + ")");
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

	@Override
	public final CompiledEcolaw getRelCompiledEcolaw() {
		return law;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result *= prime;
		if (assignments != null) {
			result += assignments.hashCode();
		}
	
		result *= prime;
		if (law != null) {
			result += law.hashCode();
		}

		result *= prime;
		if (scores != null) {
			result += scores.hashCode();
		}

		return result;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MutableMatchResultImpl other = (MutableMatchResultImpl) obj;
		if (assignments == null) {
			if (other.assignments != null) {
				return false;
			}
		} else if (!assignments.equals(other.assignments)) {
			return false;
		}
		if (law == null) {
			if (other.law != null) {
				return false;
			}
		} else if (!law.equals(other.law)) {
			return false;
		}
		if (scores == null) {
			if (other.scores != null) {
				return false;
			}
		} else if (!scores.equals(other.scores)) {
			return false;
		}
		return true;
	}

	@Override
	public final String toString() {
		return "MatchResult [assignments=" + assignments
				+ ", scores=" + scores + ", law=" + law + "]";
	}

	
}
