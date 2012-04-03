package it.apice.sapere.api.ecolaws;

import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

/**
 * <p>
 * This interface models a pattern name, that should be use to handle the
 * association of matched reactants and products.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface PatternName {

	/**
	 * <p>
	 * Retrieves the representation of the name as a String.
	 * </p>
	 * 
	 * @return The pattern name as a String
	 */
	String getValue();

	/**
	 * <p>
	 * Accepts a Visitor.
	 * </p>
	 * 
	 * @param visitor
	 *            Ecolaw visitor
	 */
	void accept(EcolawVisitor visitor);
}
