package it.apice.sapere.api.lsas.values.impl;

import it.apice.sapere.api.lsas.values.LongValue;

/**
 * <p>
 * Implementation of LongValue.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see LongValue
 */
public class LongValueImpl extends PropertyValueImpl<Long> implements
		LongValue {

	/**
	 * <p>
	 * Builds a new LongValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 */
	public LongValueImpl(final Long aValue) {
		super(aValue);
	}

	/**
	 * <p>
	 * Builds a new LongValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 * @param aLangCode
	 *            The language code for localization
	 */
	public LongValueImpl(final Long aValue, final String aLangCode) {
		super(aValue, aLangCode);
	}
}
