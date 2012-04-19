package it.apice.sapere.api.lsas.values.impl;

import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.lsas.values.PropertyValue;
import it.apice.sapere.api.lsas.visitor.LSAVisitor;

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
 * @param <CompType>
 *            The type for comparable
 * 
 * @see PropertyValue
 */
public class PropertyValueImpl<Type extends Comparable<Type>, CompType>
		implements PropertyValue<Type, CompType> {

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
		return langCode != null && !langCode.equals("");
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

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result *= prime;
		if (langCode != null) {
			result += langCode.hashCode();
		}

		result *= prime;
		if (value != null) {
			result += value.hashCode();
		}

		return result;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		PropertyValueImpl other = (PropertyValueImpl) obj;
		if (langCode == null) {
			if (other.langCode != null) {
				return false;
			}
		} else if (!langCode.equals(other.langCode)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public final void accept(final LSAVisitor visitor) {
		visitor.visit(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final int compareTo(final CompType other) {
		if (other == null) {
			throw new NullPointerException();
		}

		if (other instanceof PropertyValue<?, ?>) {
			return value.compareTo((Type) ((PropertyValue<?, ?>) other)
					.getValue());
		}

		throw new IllegalArgumentException("Cannot compare incompatible types");
	}

}
