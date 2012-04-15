package it.apice.sapere.api.ecolaws.impl;

import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.terms.ValueTerm;
import it.apice.sapere.api.ecolaws.terms.impl.ValueTermImpl;
import it.apice.sapere.api.lsas.values.DoubleValue;
import it.apice.sapere.api.lsas.values.impl.DoubleValueImpl;

import java.util.Random;

/**
 * <p>
 * This class implements the rate of a (memoryless) Markovian Process.
 * </p>
 * 
 * @author Marco Santarelli
 * @author Matteo Desanti
 * 
 * @author Paolo Contessi
 * 
 * @see Rate<?>
 */
public class MarkovianRateImpl extends AbstractRateImpl<DoubleValue> implements
		Rate<DoubleValue> {

	/** Number of milliseconds in one second. */
	private static final transient int MILLISEC_IN_SEC = 1000;

	/** Rate Value. */
	private final transient ValueTerm<DoubleValue> rate;

	/** Random Number Generator. */
	private final transient Random rng;

	/**
	 * <p>
	 * Builds a new MarkovianRateImpl.
	 * </p>
	 * 
	 * @param aRate
	 *            Rate Value as a double
	 */
	public MarkovianRateImpl(final double aRate) {
		this(new DoubleValueImpl(aRate));
	}

	/**
	 * <p>
	 * Builds a new MarkovianRateImpl.
	 * </p>
	 * 
	 * @param aRate
	 *            Rate Value as a double
	 * @param seed
	 *            Seed for Random Number Generator (RNG)
	 */
	public MarkovianRateImpl(final double aRate, final Long seed) {
		this(new DoubleValueImpl(aRate), seed);
	}

	/**
	 * <p>
	 * Builds a new MarkovianRateImpl.
	 * </p>
	 * 
	 * @param aRate
	 *            Rate Value as a DoubleValue
	 */
	public MarkovianRateImpl(final DoubleValue aRate) {
		this(new ValueTermImpl<DoubleValue>(aRate));
	}

	/**
	 * <p>
	 * Builds a new MarkovianRateImpl.
	 * </p>
	 * 
	 * @param aRate
	 *            Rate Value as a DoubleValue
	 * @param seed
	 *            Seed for Random Number Generator (RNG)
	 */
	public MarkovianRateImpl(final DoubleValue aRate, final Long seed) {
		this(new ValueTermImpl<DoubleValue>(aRate), seed);
	}

	/**
	 * <p>
	 * Builds a new MarkovianRateImpl.
	 * </p>
	 * 
	 * @param aRate
	 *            Rate Value as a ValueTerm
	 */
	public MarkovianRateImpl(final ValueTerm<DoubleValue> aRate) {
		this(aRate, null);
	}

	/**
	 * <p>
	 * Builds a new MarkovianRateImpl.
	 * </p>
	 * 
	 * @param aRate
	 *            Rate Value as a ValueTerm
	 * @param seed
	 *            Seed for Random Number Generator (RNG)
	 */
	public MarkovianRateImpl(final ValueTerm<DoubleValue> aRate, 
			final Long seed) {
		if (aRate == null) {
			throw new IllegalArgumentException("Invalid rate value");
		}

		rate = aRate;
		rng = new Random(seed);
	}

	@Override
	public final long getNextOccurrence(final double score,
			final long currentTime) {
		final double cts = (currentTime * 1.0) / MILLISEC_IN_SEC;

		// Function extracted from previous work
		return Math.round(cts
				+ +Math.log((1.0 / rng.nextDouble())
						/ (rate.getValue().getValue() * score)));
	}

	@Override
	public final DoubleValue getRateValue() {
		return rate.getValue();
	}

	@Override
	public final String toString() {
		return rate.toString();
	}
}
