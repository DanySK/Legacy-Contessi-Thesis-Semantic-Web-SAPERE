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
public class LongValueImpl extends PropertyValueImpl<Long, LongValue> implements
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

}
