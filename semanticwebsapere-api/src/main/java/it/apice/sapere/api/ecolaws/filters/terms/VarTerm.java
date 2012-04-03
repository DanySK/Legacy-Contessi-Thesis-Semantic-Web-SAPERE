package it.apice.sapere.api.ecolaws.filters.terms;

import it.apice.sapere.api.ecolaws.Term;

/**
 * <p>
 * This interface models an unconstrained variable term.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface VarTerm extends Term {

	/**
	 * <p>
	 * Retrieves variable name.
	 * </p>
	 * 
	 * @return The name of the variable
	 */
	String getVariableName();
}
