package it.apice.sapere.api.ecolaws.impl;

import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

/**
 * <p>
 * This class provides basic common functionalities of a Rate.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            Internal type used for rate representation
 */
public abstract class AbstractRateImpl<Type> implements Rate<Type> {

	/**
	 * <p>
	 * Default Constructor.
	 * </p>
	 */
	public AbstractRateImpl() {
		super();
	}

	@Override
	public final void accept(final EcolawVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public Rate<?> clone() throws CloneNotSupportedException {
		return (Rate<?>) super.clone();
	}
}