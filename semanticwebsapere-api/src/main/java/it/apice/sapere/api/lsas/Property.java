package it.apice.sapere.api.lsas;

import it.apice.sapere.api.lsas.values.PropertyValue;

/**
 * <p>
 * A property is a component of an LSA's Semantic Description. It is composed
 * by:
 * </p>
 * <ul>
 * <li>A name (in the form of an URI)</li>
 * <li>A (possibly non-empty) set of values which can be in the form of URIs or
 * String literals</li>
 * </ul>
 * <p>
 * This entity is intended to be:
 * </p>
 * <ul>
 * <li>Composite</li>
 * <li>Passive</li>
 * <li>Mutable</li>
 * </ul>
 * 
 * @author Paolo Contessi
 * 
 */
public interface Property {

	/**
	 * <p>
	 * Retrieves the name of the property.
	 * </p>
	 * 
	 * @return The property's name
	 */
	PropertyName getName();

	/**
	 * <p>
	 * Adds the specified values to the statements of this property.
	 * </p>
	 * 
	 * @param value
	 *            The value to be added
	 * @return A reference to the updated property
	 */
	Property addValue(PropertyValue<?> value);

	/**
	 * <p>
	 * Removes the specified value from the statements of this property.
	 * <p>
	 * 
	 * @param value
	 *            The value to be removed
	 * @return A reference to the updated property
	 */
	Property removeValue(PropertyValue<?> value);

	/**
	 * <p>
	 * Modifies a property statement changing its value.
	 * </p>
	 * 
	 * @param oldValue
	 *            The value that the property currently has
	 * @param newValue
	 *            The new value that will replace the actual one
	 * @return A reference to the updated property
	 */
	Property changeValue(PropertyValue<?> oldValue, PropertyValue<?> newValue);

	/**
	 * <p>
	 * Lists all the values associated to this property.
	 * </p>
	 * 
	 * @return All values that this property has
	 */
	PropertyValue<?>[] values();

	/**
	 * <p>
	 * Check if the provided value is associated to this property.
	 * </p>
	 * 
	 * @param value
	 *            The value to be checked
	 * @return True if associated, false otherwise
	 */
	boolean hasValue(PropertyValue<?> value);

	/**
	 * <p>
	 * Clones this entity.
	 * </p>
	 * 
	 * @return The clone entity
	 */
	Property clone();

	/**
	 * <p>
	 * Checks if this property extends the provided one.
	 * </p>
	 * 
	 * @param other
	 *            Another property to be checked against
	 * @return True if extends, false otherwise
	 */
	boolean isExtensionOf(Property other);
}
