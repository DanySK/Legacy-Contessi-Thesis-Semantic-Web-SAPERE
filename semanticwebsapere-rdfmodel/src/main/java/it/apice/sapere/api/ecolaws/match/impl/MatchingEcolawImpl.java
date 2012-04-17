package it.apice.sapere.api.ecolaws.match.impl;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.impl.EcolawImpl;
import it.apice.sapere.api.ecolaws.match.MatchingEcolaw;
import it.apice.sapere.api.ecolaws.match.ScoredMatchResult;

/**
 * <p>
 * This class implements {@link MatchingEcolaw} interface.
 * </p>
 *
 * @author Paolo Contessi
 *
 */
public class MatchingEcolawImpl extends EcolawImpl implements MatchingEcolaw {

	/** The eco-law that has been matched. */
	private final transient Ecolaw law;

	/** The chosen match. */
	private final transient ScoredMatchResult match;

	/**
	 * <p>
	 * Builds a new Matching Eco-law given an eco-law and a possible set of
	 * assignments (maybe the best one).
	 * </p>
	 * 
	 * @param aLaw
	 *            The eco-law with variables to be bound
	 * @param aMatch
	 *            The ScoredMatchResult with all the assignments
	 */
	public MatchingEcolawImpl(final Ecolaw aLaw, 
			final ScoredMatchResult aMatch) {
		super(aLaw);

		if (aLaw == null) {
			throw new IllegalArgumentException("Invalid eco-law provided");
		}

		if (aMatch == null) {
			throw new IllegalArgumentException(
					"Invalid ScoredMatchResult provided");
		}

		law = aLaw;
		match = aMatch;
		setExtraInfo(String.format(":%.3f", match.getScore()));
	}

	@Override
	public final void apply() {
		match.getLSAspace().apply(this);
	}

	@Override
	public final double getScore() {
		return match.getScore();
	}

	@Override
	public final Ecolaw getOriginal() {
		return law;
	}

}
