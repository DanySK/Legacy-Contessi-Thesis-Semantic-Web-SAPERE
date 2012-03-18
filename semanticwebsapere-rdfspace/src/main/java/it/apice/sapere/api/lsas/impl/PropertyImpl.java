package it.apice.sapere.api.lsas.impl;

import it.apice.sapere.api.lsas.Property;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.values.PropertyValue;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Implementation of Property.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see Property
 * 
 */
public class PropertyImpl implements Property {

	/** Property name. */
	private final PropertyName name;

	/** Set of values. */
	private final Set<PropertyValue<?>> values;

	/**
	 * <p>
	 * Builds a new PropertyImpl.
	 * </p>
	 * 
	 * @param aName
	 *            The name of the property
	 */
	public PropertyImpl(final PropertyName aName) {
		if (aName == null) {
			throw new IllegalArgumentException("Invalid name");
		}

		name = aName;
		values = new HashSet<PropertyValue<?>>();
	}

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 * 
	 * @param property
	 *            Property to be cloned
	 */
	public PropertyImpl(final PropertyImpl property) {
		name = property.name;
		values = new HashSet<PropertyValue<?>>(property.values);
	}

	@Override
	public final PropertyName getName() {
		return name;
	}

	@Override
	public final Property addValue(final PropertyValue<?> value) {
		values.add(value);
		return this;
	}

	@Override
	public final Property removeValue(final PropertyValue<?> value) {
		values.remove(value);
		return this;
	}

	@Override
	public final Property changeValue(final PropertyValue<?> oldValue,
			final PropertyValue<?> newValue) {
		if (!values.contains(oldValue)) {
			throw new IllegalArgumentException(String.format(
					"Cannot change value %s to %s (unknown old value)",
					oldValue, newValue));
		}

		values.remove(oldValue);
		if (!values.add(newValue)) {
			throw new IllegalArgumentException(String.format(
					"Cannot add new value (%s)", newValue));
		}

		return this;
	}

	@Override
	public final PropertyValue<?>[] values() {
		return values.toArray(new PropertyValue<?>[values.size()]);
	}

	@Override
	public final boolean hasValue(final PropertyValue<?> value) {
		return values.contains(value);
	}

	@Override
	public final boolean isExtensionOf(final Property other) {
		for (PropertyValue<?> val : other.values()) {
			if (!values.contains(val)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public final Property clone() {
		return new PropertyImpl(this);
	}
}
