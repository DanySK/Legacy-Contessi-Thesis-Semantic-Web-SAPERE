package it.apice.sapere.api.ecolaws.scores;

import it.apice.sapere.api.ecolaws.match.MatchResult;
import it.apice.sapere.api.ecolaws.match.ScoredMatchResult;
import it.apice.sapere.api.ecolaws.match.impl.ScoredMatchResultImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * This class provides an implementation of the {@link RankingProducer}
 * interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class RankingProducerImpl implements RankingProducer {

	/** The score function. */
	private final transient ScoreFunction function;

	/**
	 * <p>
	 * Builds a new {@link RankingProducerImpl}.
	 * </p>
	 *
	 * @param sFunction The score function
	 */
	public RankingProducerImpl(final ScoreFunction sFunction) {
		if (sFunction == null) {
			throw new IllegalArgumentException("Invalid function provided");
		}

		function = sFunction;
	}

	@Override
	public final ScoredMatchResult[] rank(final MatchResult[] alternatives) {
		int size = alternatives.length;
		final List<ScoredMatchResultImpl> res = 
				new ArrayList<ScoredMatchResultImpl>(size);

		for (MatchResult alt : alternatives) {
			final ScoredMatchResultImpl smr = new ScoredMatchResultImpl(alt,
					function);
			int idx = Collections.binarySearch(res, smr);
			assert idx < 0;
			res.add(-1 - idx, smr);
		}

		return res.toArray(new ScoredMatchResult[size]);
	}

}
