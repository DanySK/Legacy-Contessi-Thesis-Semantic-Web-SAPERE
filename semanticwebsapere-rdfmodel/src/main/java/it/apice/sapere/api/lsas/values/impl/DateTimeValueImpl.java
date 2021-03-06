package it.apice.sapere.api.lsas.values.impl;

import it.apice.sapere.api.lsas.values.DateTimeValue;

import java.util.Date;

/**
 * <p>
 * Implementation of DateValue.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see DateValue
 */
public class DateTimeValueImpl extends PropertyValueImpl<Date, DateTimeValue>
		implements DateTimeValue {

	/**
	 * <p>
	 * Builds a new DateValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 */
	public DateTimeValueImpl(final Date aValue) {
		super(aValue);
	}

}
