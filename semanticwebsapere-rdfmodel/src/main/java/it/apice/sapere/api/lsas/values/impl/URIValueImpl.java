package it.apice.sapere.api.lsas.values.impl;

import java.net.URI;

import it.apice.sapere.api.lsas.values.URIValue;

/**
 * <p>
 * Implementation of URIValue.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see URIValue
 */
public class URIValueImpl extends PropertyValueImpl<URI> implements URIValue {

	/**
	 * <p>
	 * Builds a new URIValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 */
	public URIValueImpl(final URI aValue) {
		super(aValue);
	}

}
