package it.apice.sapere.api.ecolaws.terms;

/**
 * <p>
 * This interface models a boolean condition for annotated variables.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            The type of the term's value
 */
public interface Formula<Type> {

	/**
	 * <p>
	 * Checks the term and decides if represents a valid assignment.
	 * </p>
	 * 
	 * @param value
	 *            The value to be verified
	 * @return True if condition is satisfied (valid assignment), false
	 *         otherwise
	 */
	boolean accept(Type value);

	/**
	 * <p>
	 * Provides a formula representation as a String.
	 * </p>
	 * 
	 * @return The representation of the formula as a String
	 */
	String getDescription();
}
