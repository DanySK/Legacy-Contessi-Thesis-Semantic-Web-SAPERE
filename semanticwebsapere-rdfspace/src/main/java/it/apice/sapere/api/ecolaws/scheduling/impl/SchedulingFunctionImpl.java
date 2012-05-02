package it.apice.sapere.api.ecolaws.scheduling.impl;

import java.util.Random;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.scheduling.SchedulableMatchResult;
import it.apice.sapere.api.ecolaws.scheduling.SchedulingFunction;
import it.apice.sapere.api.space.core.CompiledEcolaw;
import it.apice.sapere.api.space.match.MatchResult;

/**
 * <p>
 * This class implements the {@link SchedulingFunction} interface.
 * </p>
 * <p>
 * This class is derived from previous work by Marco Santarelli, Matteo Desanti.
 * </p>
 * 
 * @author Marco Santarelli
 * @author Matteo Desanti
 * 
 * @author Paolo Contessi
 * 
 */
public class SchedulingFunctionImpl implements SchedulingFunction {

	/** Random Number Generator. */
	private final transient Random rng;

	/**
	 * <p>
	 * Default Constructor.
	 * </p>
	 */
	public SchedulingFunctionImpl() {
		this(System.currentTimeMillis());
	}

	/**
	 * <p>
	 * Builds a new {@link SchedulingFunctionImpl}.
	 * </p>
	 * 
	 * @param seed
	 *            The seed to be used in the Random Number Generator (RNG)
	 */
	public SchedulingFunctionImpl(final long seed) {
		rng = new Random(seed);
	}

	@Override
	public final SchedulableMatchResult eval(final MatchResult mResult)
			throws SAPEREException {
		if (mResult == null) {
			throw new IllegalArgumentException("Invalid match result provided");
		}

		try {
			final CompiledEcolaw law = mResult.getRelCompiledEcolaw();

			String rString;
			if (law.hasVariableRate()) {
				rString = mResult.lookup(law.getRate().split("?")[1]);
			} else {
				rString = law.getRate();
			}

			// @author Danilo Pianini
			final double time = -Math.log(rng.nextDouble())
					/ Double.parseDouble(rString);

			return new SchedulableMatchResultImpl(mResult, Math.round(time));
		} catch (Exception ex) {
			throw new SAPEREException("Cannot compute scheduling rate", ex);
		}
	}

	@Override
	public final SchedulableMatchResult[] eval(final MatchResult[] mResults)
			throws SAPEREException {
		if (mResults == null) {
			throw new IllegalArgumentException("Invalid match result provided");
		}

		final SchedulableMatchResult[] res = 
				new SchedulableMatchResult[mResults.length];

		for (int i = 0; i < mResults.length; i++) {
			res[i] = eval(mResults[i]);
		}

		return res;
	}
}
