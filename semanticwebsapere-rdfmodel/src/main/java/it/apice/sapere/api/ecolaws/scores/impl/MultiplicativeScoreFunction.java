package it.apice.sapere.api.ecolaws.scores.impl;

import it.apice.sapere.api.ecolaws.match.MatchResult;
import it.apice.sapere.api.ecolaws.scores.ScoreFunction;

/**
 * <p>
 * Realization of a Score Function for an EcoLaw. The total score is obtained as
 * mathematical product between the single scores of the reagents of the ecolaw
 * </p>
 * 
 * @author Marco Santarelli
 * @author Paolo Contessi
 * 
 */
public class MultiplicativeScoreFunction implements ScoreFunction {

	// @Override
	// public double getScore() {
	// double tmp = 1.0;
	// for(Reactant re:parentReaction.getReactants())
	// tmp*=parentReaction.getCandidateLsaScore(re.getIdentifier());
	// return tmp;
	// }

	@Override
	public final double computeScore(final MatchResult bindings) {
		// TODO Auto-generated method stub
		return 0;
	}

}
