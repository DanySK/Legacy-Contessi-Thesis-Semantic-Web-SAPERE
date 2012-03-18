package it.apice.sapere.api.lsas.values.impl;

import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.lsas.values.PropertyValue;

import java.net.URI;

/**
 * <p>
 * Implementation of PropertyValue.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            Inner type stored inside the Property Value
 * 
 * @see PropertyValue
 */
public class PropertyValueImpl<Type> implements PropertyValue<Type> {

	/** Stored value. */
	private final Type value;

	/** Language code for localization. */
	private final String langCode;

	/**
	 * <p>
	 * Builds a new PropertyValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 */
	public PropertyValueImpl(final Type aValue) {
		this(aValue, null);
	}

	/**
	 * <p>
	 * Builds a new PropertyValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 * @param aLangCode
	 *            Language code for localization
	 */
	public PropertyValueImpl(final Type aValue, final String aLangCode) {
		if (aValue == null) {
			throw new IllegalArgumentException("Invalid value");
		}

		value = aValue;
		langCode = aLangCode;
	}

	@Override
	public final boolean isURI() {
		return value instanceof URI;
	}

	@Override
	public final boolean isLiteral() {
		return value instanceof String;
	}

	@Override
	public final boolean isNumber() {
		return value instanceof Integer || value instanceof Long
				|| value instanceof Float || value instanceof Double;
	}

	@Override
	public final boolean isBoolean() {
		return value instanceof Boolean;
	}

	@Override
	public final boolean isLSAId() {
		return value instanceof LSAid;
	}

	@Override
	public final boolean hasLocale() {
		return langCode == null || langCode.equals("");
	}

	@Override
	public final String getLanguageCode() {
		return langCode;
	}

	@Override
	public final Type getValue() {
		return value;
	}

	@Override
	public final String toString() {
		return value.toString();
	}
}
