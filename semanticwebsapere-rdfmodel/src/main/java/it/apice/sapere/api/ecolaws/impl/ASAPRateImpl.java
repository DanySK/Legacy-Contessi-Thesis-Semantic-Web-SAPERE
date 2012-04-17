package it.apice.sapere.api.ecolaws.impl;

import it.apice.sapere.api.ecolaws.Rate;

/**
 * <p>
 * This class implements the As-Soon-As-Possible Rate.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see Rate<?>
 */
public class ASAPRateImpl extends AbstractRateImpl<Double> implements
		Rate<Double> {

	@Override
	public final long getNextOccurrence(final double score,
			final long currentTime) {
		return currentTime;
	}

	@Override
	public final Double getRateValue() {
		return 0.0;
	}

	@Override
	public final String toString() {
		return "";
	}
	
	@Override
	public final int hashCode() {
		return super.hashCode();
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
		
		return true;
	}
	
	@Override
	public final Rate<?> clone() throws CloneNotSupportedException {
		return this;
	}
}
