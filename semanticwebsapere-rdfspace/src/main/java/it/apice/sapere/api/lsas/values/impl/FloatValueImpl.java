package it.apice.sapere.api.lsas.values.impl;

import it.apice.sapere.api.lsas.values.FloatValue;

/**
 * <p>
 * Implementation of FloatValue.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see FloatValue
 */
public class FloatValueImpl extends PropertyValueImpl<Float> implements
		FloatValue {

	/**
	 * <p>
	 * Builds a new FloatValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 */
	public FloatValueImpl(final Float aValue) {
		super(aValue);
	}

	/**
	 * <p>
	 * Builds a new FloatValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 * @param aLangCode
	 *            The language code for localization
	 */
	public FloatValueImpl(final Float aValue, final String aLangCode) {
		super(aValue, aLangCode);
	}
}
