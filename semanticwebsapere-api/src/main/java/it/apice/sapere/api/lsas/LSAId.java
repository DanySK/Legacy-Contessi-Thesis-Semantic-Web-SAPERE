package it.apice.sapere.api.lsas;

import java.net.URI;

/**
 * <p>
 * This interface models an LSA-id which is a unique system-wide key used to
 * identify an LSA.
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
public interface LSAid extends Cloneable {

	/**
	 * <p>
	 * Retrieves the identifier's value as URI.
	 * </p>
	 * 
	 * @return The id as an URI
	 */
	URI getId();

	/**
	 * <p>
	 * Clones this entity.
	 * </p>
	 *
	 * @return	The clone entity
	 */
	LSAid clone();
}
