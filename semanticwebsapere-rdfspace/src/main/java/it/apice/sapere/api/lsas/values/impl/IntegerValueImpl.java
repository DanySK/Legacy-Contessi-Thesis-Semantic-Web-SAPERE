package it.apice.sapere.api.lsas.values.impl;

import it.apice.sapere.api.lsas.values.IntegerValue;

/**
 * <p>
 * Implementation of IntegerValue.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see IntegerValue
 */
public class IntegerValueImpl extends PropertyValueImpl<Integer> implements
		IntegerValue {

	/**
	 * <p>
	 * Builds a new IntegerValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 */
	public IntegerValueImpl(final Integer aValue) {
		super(aValue);
	}

	/**
	 * <p>
	 * Builds a new IntegerValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 * @param aLangCode
	 *            The language code for localization
	 */
	public IntegerValueImpl(final Integer aValue, final String aLangCode) {
		super(aValue, aLangCode);
	}
}
