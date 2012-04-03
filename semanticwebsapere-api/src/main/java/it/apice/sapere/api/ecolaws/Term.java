package it.apice.sapere.api.ecolaws;

/**
 * <p>
 * This interface models an Ecolaw Term, used in filters.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface Term {

	/**
	 * <p>
	 * Checks if the term is ground (not a variable).
	 * </p>
	 * 
	 * @return True if ground, false otherwise
	 */
	boolean isGround();

	/**
	 * <p>
	 * Checks if the term is a variable (not ground).
	 * </p>
	 * 
	 * @return True if is a variable, false otherwise
	 */
	boolean isVar();

	/**
	 * <p>
	 * Checks if the variable term has a bound value.
	 * </p>
	 * 
	 * @return True if is a variable and has an assigned value, false otherwise
	 */
	boolean isBound();
}
