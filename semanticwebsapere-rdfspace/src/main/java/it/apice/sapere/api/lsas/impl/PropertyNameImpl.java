package it.apice.sapere.api.lsas.impl;

import it.apice.sapere.api.lsas.PropertyName;

import java.net.URI;

/**
 * <p>
 * Implementation of PropertyName.
 * </p>
 *
 * @author Paolo Contessi
 *
 * @see PropertyName
 *
 */
public class PropertyNameImpl implements PropertyName {

	/** Property name. */
	private final URI id;

	/**
	 * <p>
	 * Builds a new PropertyNameImpl.
	 * </p>
	 * 
	 * @param value
	 *            The PropertyName value
	 */
	public PropertyNameImpl(final URI value) {
		if (value == null) {
			throw new IllegalArgumentException("Invalid value");
		}

		id = value;
	}

	@Override
	public final URI getValue() {
		return id;
	}

	@Override
	public final String toString() {
		return id.toString();
	}
}
