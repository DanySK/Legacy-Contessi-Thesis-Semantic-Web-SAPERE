package it.apice.sapere.api.ecolaws.scores.impl;

import it.apice.sapere.api.ecolaws.match.MatchResult;
import it.apice.sapere.api.ecolaws.scores.ScoreFunction;

/**
 * <p>
 * Realization of a Score Function for an EcoLaw. The total score is obtained as
 * mathematical mean between the single scores of the reagents of the ecolaw
 * <p>
 * 
 * @author Marco Santarelli
 * @author Paolo Contessi
 * 
 */
public class MeanScoreFunction implements ScoreFunction {

//	@Override
//	public double getScore() {
//		double tmp = 0.0;
//		int i = 0;
//		for (Reactant re : parentReaction.getReactants()) {
//			tmp += parentReaction.getCandidateLsaScore(re.getIdentifier());
//			i++;
//		}
//		return tmp / i;
//	}

	@Override
	public final double computeScore(final MatchResult bindings) {
		// TODO Auto-generated method stub
		return 0;
	}

}
