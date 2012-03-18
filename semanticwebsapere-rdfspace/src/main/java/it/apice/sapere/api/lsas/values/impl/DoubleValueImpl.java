package it.apice.sapere.api.lsas.values.impl;

import it.apice.sapere.api.lsas.values.DoubleValue;

/**
 * <p>
 * Implementation of DoubleValue.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see DoubleValue
 */
public class DoubleValueImpl extends PropertyValueImpl<Double> implements
		DoubleValue {

	/**
	 * <p>
	 * Builds a new DoubleValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 */
	public DoubleValueImpl(final Double aValue) {
		super(aValue);
	}

	/**
	 * <p>
	 * Builds a new DoubleValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 * @param aLangCode
	 *            The language code for localization
	 */
	public DoubleValueImpl(final Double aValue, final String aLangCode) {
		super(aValue, aLangCode);
	}
}
