package it.apice.sapere.api.ecolaws.terms.impl;

import it.apice.sapere.api.ecolaws.terms.Formula;
import it.apice.sapere.api.ecolaws.terms.ValueTerm;
import it.apice.sapere.api.lsas.values.PropertyValue;

/**
 * <p>
 * Implementation of the SDescTerm interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            Internal value type
 */
public class ValueTermImpl<Type extends PropertyValue<?>> extends
		AnnotatedVarTermImpl<Type> implements ValueTerm<Type> {

	/**
	 * <p>
	 * Builds a new ValueTermImpl.
	 * </p>
	 * 
	 * @param varName
	 *            The name of the variable
	 */
	public ValueTermImpl(final String varName) {
		super(varName);
	}

	/**
	 * <p>
	 * Builds a new ValueTermImpl.
	 * </p>
	 * 
	 * @param varName
	 *            The name of the variable
	 * @param boolCond
	 *            The binding acceptance criteria
	 */
	public ValueTermImpl(final String varName,
			final Formula<Type> boolCond) {
		super(varName, boolCond);
	}

	/**
	 * <p>
	 * Builds a new SDescTermImpl.
	 * </p>
	 * 
	 * @param val
	 *            The value
	 */
	public ValueTermImpl(final Type val) {
		super(val);
	}

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 * 
	 * @param src
	 *            Term to be cloned
	 */
	public ValueTermImpl(final ValueTermImpl<Type> src) {
		super(src);
	}
}
