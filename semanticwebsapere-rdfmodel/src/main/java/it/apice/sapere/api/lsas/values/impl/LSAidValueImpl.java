package it.apice.sapere.api.lsas.values.impl;

import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.lsas.values.LSAidValue;

/**
 * <p>
 * Implementation of LSAidValue.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see LSAidValue
 */
public class LSAidValueImpl extends PropertyValueImpl<LSAid, LSAidValue>
		implements LSAidValue {

	/**
	 * <p>
	 * Builds a new LSAidValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 */
	public LSAidValueImpl(final LSAid aValue) {
		super(aValue);
	}

}
