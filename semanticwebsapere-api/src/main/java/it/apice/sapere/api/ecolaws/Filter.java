package it.apice.sapere.api.ecolaws;

import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

/**
 * <p>
 * This interface models a generic filter that will be applied to a
 * ChemicalPattern.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface Filter {

	/**
	 * <p>
	 * Accepts a Visitor.
	 * </p>
	 * 
	 * @param visitor
	 *            Ecolaw visitor
	 */
	void accept(EcolawVisitor visitor);
	
	/**
	 * <p>
	 * Creates a clone of the filter.
	 * </p>
	 * 
	 * @return The clone instance
	 * @throws CloneNotSupportedException
	 *             Cannot clone
	 */
	Filter clone() throws CloneNotSupportedException;
}
