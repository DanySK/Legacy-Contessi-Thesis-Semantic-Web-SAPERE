package it.apice.sapere.api.lsas.values.impl;

import it.apice.sapere.api.lsas.values.BooleanValue;

/**
 * <p>
 * Implementation of BooleanValue.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see BooleanValue
 */
public class BooleanValueImpl extends PropertyValueImpl<Boolean> implements
		BooleanValue {

	/**
	 * <p>
	 * Builds a new BooleanValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 */
	public BooleanValueImpl(final Boolean aValue) {
		super(aValue);
	}

	/**
	 * <p>
	 * Builds a new BooleanValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 * @param aLangCode
	 *            The language code for localization
	 */
	public BooleanValueImpl(final Boolean aValue, final String aLangCode) {
		super(aValue, aLangCode);
	}
}
