package it.apice.sapere.api.ecolaws.terms;

import it.apice.sapere.api.ecolaws.Term;

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
	 * @param term
	 *            The term to be verified
	 * @return True if condition is satisfied (valid assignment), false
	 *         otherwise
	 */
	boolean accept(Term<Type> term);
}
