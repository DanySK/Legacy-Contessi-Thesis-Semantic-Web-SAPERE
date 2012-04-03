package it.apice.sapere.api.ecolaws;

/**
 * <p>
 * This interface models a generic template which represents a component of an
 * Ecolaw.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface ChemicalPattern {

	/**
	 * <p>
	 * Retrieves the name of the pattern.
	 * </p>
	 * 
	 * @return Symbolic name of the pattern
	 */
	PatternName getName();

	/**
	 * <p>
	 * Retrieves all filters applied to this pattern.
	 * </p>
	 * 
	 * @return List of all filters
	 */
	Filter[] filters();
}
