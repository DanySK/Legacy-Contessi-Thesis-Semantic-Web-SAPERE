package it.apice.sapere.api.lsas;

import it.apice.sapere.api.nodes.SAPEREAgent;

/**
 * <p>
 * This interface models an LSA: Live Semantic Annotation.
 * </p>
 * <p>
 * An LSA is the basic chunk of information that an agent can inject, update,
 * remove, read or observe in an LSASpace. It is identified in a SAPERE
 * ecosystem by a so called LSA-id and is composed by a SemanticDescription.
 * </p>
 * <p>
 * This entity is intended to be:
 * </p>
 * <ul>
 * <li>Composite</li>
 * <li>Passive</li>
 * <li>Functional (not-mutable state)</li>
 * </ul>
 * 
 * @author Paolo Contessi
 * 
 */
public interface LSA extends Cloneable {

	/**
	 * <p>
	 * Retrieves the unique identifier of this LSA in the SAPERE ecosystem
	 * scope.
	 * </p>
	 * 
	 * @return The LSA identifier
	 */
	LSAId getLSAId();

	/**
	 * <p>
	 * Retrieves the SemanticDescription contained in the LSA.
	 * </p>
	 * 
	 * @return The Semantic Description of this LSA
	 */
	SemanticDescription getSemanticDescription();

	/**
	 * <p>
	 * Checks if the provided agent owns the LSA.
	 * </p>
	 * 
	 * @param agent
	 *            An agent of the ecosystem
	 * @return True if the agent owns the LSA, false otherwise
	 */
	boolean isOwnedBy(SAPEREAgent agent);
}
