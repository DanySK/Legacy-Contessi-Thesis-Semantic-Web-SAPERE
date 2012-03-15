package it.apice.sapere.api.lsas.values;

/**
 * <p>
 * A Property Value is a generic value (URI or Literal) that can be associated
 * to a Property.
 * </p>
 * <p>
 * This entity is intended to be:
 * </p>
 * <ul>
 * <li>Atomic</li>
 * <li>Passive</li>
 * <li>Functional (not-mutable state)</li>
 * </ul>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            Inner type stored inside the Property Value
 */
public interface PropertyValue<Type> extends Cloneable {

	/**
	 * <p>
	 * Checks if the value is an URI.
	 * </p>
	 * 
	 * @return True if is an URI, false otherwise
	 */
	boolean isURI();

	/**
	 * <p>
	 * Checks if the value is a Literal (a String).
	 * </p>
	 * 
	 * @return True if is a Literal, false otherwise
	 */
	boolean isLiteral();

	/**
	 * <p>
	 * Checks if the value is a Number.
	 * </p>
	 * 
	 * @return True if is an Number, false otherwise
	 */
	boolean isNumber();

	/**
	 * <p>
	 * Checks if the value is a Boolean.
	 * </p>
	 * 
	 * @return True if is a Boolean, false otherwise
	 */
	boolean isBoolean();

	/**
	 * <p>
	 * Checks if the value is an LSA-id.
	 * </p>
	 * 
	 * @return True if is an LSA-id, false otherwise
	 */
	boolean isLSAId();

	/**
	 * <p>
	 * Checks if the value has a language code.
	 * </p>
	 * 
	 * @return True if language code has been defined, false otherwise
	 */
	boolean hasLocale();

	/**
	 * <p>
	 * Retrieves the language code associated to this value.
	 * </p>
	 * 
	 * @return The language code
	 */
	String getLanguageCode();

	/**
	 * <p>
	 * Extracts the value from this Property Value.
	 * </p>
	 * 
	 * @return The value
	 */
	Type getValue();

	/**
	 * <p>
	 * Clones this entity.
	 * </p>
	 * 
	 * @return The cloned entity
	 */
	PropertyValue<Type> clone();
}
