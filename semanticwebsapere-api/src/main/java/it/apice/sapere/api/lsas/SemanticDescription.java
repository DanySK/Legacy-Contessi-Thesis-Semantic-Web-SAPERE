package it.apice.sapere.api.lsas;


/**
 * <p>
 * A Semantic Description is a collection of properties that are associated to
 * the LSA containing the description.
 * </p>
 * <p>
 * These properties are intended for description of agents' state or knowing and
 * can be managed only by the agent that created the LSA or by the system; other
 * agents can only observe them.
 * </p>
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
public interface SemanticDescription extends Cloneable {

	/**
	 * <p>
	 * Adds a new property to this description.
	 * </p>
	 * 
	 * @param prop
	 *            The property to be added
	 * @return The updated SemanticDescription
	 */
	SemanticDescription addProperty(Property prop);

	/**
	 * <p>
	 * Removes a property from this description.
	 * </p>
	 * 
	 * @param prop
	 *            The property to be removed
	 * @return The updated SemanticDescription
	 */
	SemanticDescription removeProperty(Property prop);

	/**
	 * <p>
	 * Lists all SemanticDescription's properties.
	 * </p>
	 * 
	 * @return SemanticDescription's properties
	 */
	Property[] properties();

	/**
	 * <p>
	 * Checks if the named property is part of this description.
	 * </p>
	 * 
	 * @param name
	 *            The name of the property to be checked
	 * @return True if contained, false otherwise
	 */
	boolean contains(PropertyName name);

	/**
	 * <p>
	 * Retrieves a specific property.
	 * </p>
	 * 
	 * @param name
	 *            The name of the property to be retrieved
	 * @return The wanted property
	 */
	Property get(PropertyName name);

	/**
	 * <p>
	 * Checks if this SemanticDescription is a clone of the provided one.
	 * </p>
	 * <p>
	 * In other words checks if the two are equal.
	 * </p>
	 * 
	 * @param other
	 *            Another SemanticDescription to be checked against
	 * @return True if cloned, false otherwise
	 */
	boolean isCloneOf(SemanticDescription other);

	/**
	 * <p>
	 * Checks if this SemanticDescription is an extension of the provided one.
	 * </p>
	 * <p>
	 * Verification is achieved verifying that all properties (and relative
	 * values) that are contained in the other description are also in this one.
	 * </p>
	 * 
	 * @param other Another SemanticDescription to be checked against
	 * @return True if extends, false otherwise
	 */
	boolean isExtensionOf(SemanticDescription other);

	/**
	 * <p>
	 * Clones this entity.
	 * </p>
	 *
	 * @return	The clone entity
	 */
	SemanticDescription clone();
}
