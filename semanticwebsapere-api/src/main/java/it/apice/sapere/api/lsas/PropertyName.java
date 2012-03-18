package it.apice.sapere.api.lsas;

import java.net.URI;

/**
 * <p>
 * This interface models a generic name which is valid to identify an LSA
 * property.
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
 */
public interface PropertyName {

	/**
	 * <p>
	 * Retrieves the property name.
	 * </p>
	 * 
	 * @return The URI which identifies the property
	 */
	URI getValue();

}
