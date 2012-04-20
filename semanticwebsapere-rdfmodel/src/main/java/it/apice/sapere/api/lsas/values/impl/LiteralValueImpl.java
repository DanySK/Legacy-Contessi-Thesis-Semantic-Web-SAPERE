package it.apice.sapere.api.lsas.values.impl;

import it.apice.sapere.api.lsas.values.LiteralValue;


/**
 * <p>
 * Implementation of StringValue.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see StringValue
 */
public class LiteralValueImpl extends PropertyValueImpl<String, LiteralValue> implements
		LiteralValue {

	/**
	 * <p>
	 * Builds a new StringValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 */
	public LiteralValueImpl(final String aValue) {
		super(aValue);
	}

	/**
	 * <p>
	 * Builds a new StringValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 * @param aLangCode
	 *            The language code for localization
	 */
	public LiteralValueImpl(final String aValue, final String aLangCode) {
		super(aValue, aLangCode);
	}
}
