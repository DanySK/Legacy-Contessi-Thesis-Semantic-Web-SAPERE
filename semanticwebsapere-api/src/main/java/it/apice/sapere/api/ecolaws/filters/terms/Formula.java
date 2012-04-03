package it.apice.sapere.api.ecolaws.filters.terms;

import it.apice.sapere.api.ecolaws.Term;

/**
 * <p>
 * This interface models a Formula used to annotate a Variable term.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface Formula {

	/**
	 * <p>
	 * Checks if provided value can be assigned to the annotated variable.
	 * </p>
	 * 
	 * @param value
	 *            The candidate value
	 * @return True if can be assigned, false otherwise
	 */
	boolean accept(Term value);
}
