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

//	/** Number of milliseconds in one second. */
//	private static final transient int MILLISEC_IN_SEC = 1000;

	/** Rate Value. */
	private final ValueTerm<DoubleValue> rate;

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

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 *
	 * @param src The source
	 */
	public MarkovianRateImpl(final MarkovianRateImpl src) {
		try {
			rate = (ValueTerm<DoubleValue>) src.rate.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException("Cannot clone", e);
		}
		rng = src.rng;
	}

//	@Override
//	public final long getNextOccurrence(final double score,
//			final long currentTime) {
//		final double cts = (currentTime * 1.0) / MILLISEC_IN_SEC;
//
//		// Function extracted from previous work
//		return Math.round(cts
//				+ +Math.log((1.0 / rng.nextDouble())
//						/ (rate.getValue().getValue() * score)));
//	}

	@Override
	public final DoubleValue getRateValue() {
		return rate.getValue();
	}

	@Override
	public final String toString() {
		return rate.toString();
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result *= prime;
		if (rate != null) {
			result += rate.hashCode();
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
		final MarkovianRateImpl other = (MarkovianRateImpl) obj;
		if (rate == null) {
			if (other.rate != null) {
				return false;
			}
		} else if (!rate.equals(other.rate)) {
			return false;
		}
		return true;
	}

	@Override
	public final Rate<?> clone() throws CloneNotSupportedException {
		return new MarkovianRateImpl(this);
	}
}
