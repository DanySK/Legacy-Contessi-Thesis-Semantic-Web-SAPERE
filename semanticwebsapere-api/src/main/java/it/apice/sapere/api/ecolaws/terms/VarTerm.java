package it.apice.sapere.api.ecolaws.terms;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.Term;

/**
 * <p>
 * This interface models a logic-like unconstrained variable term for an Ecolaw.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            The type of the term's value
 */
public interface VarTerm<Type> extends Term<Type> {

	/**
	 * <p>
	 * The name of the variable.
	 * </p>
	 * 
	 * @return The variable's name
	 */
	String getVarName();

	/**
	 * <p>
	 * Assigns the provided value to this variable, checking if it satisfies the
	 * boolean condition (if any).
	 * </p>
	 * 
	 * @param term
	 *            The value to be assigned
	 * @throws SAPEREException
	 *             The value does not satisfy the boolean condition or this is
	 *             not a variable
	 */
	void bindTo(Term<Type> term) throws SAPEREException;

	/**
	 * <p>
	 * Clears any value assignment.
	 * </p>
	 * 
	 * @throws SAPEREException
	 *             The term is not a variable
	 */
	void clearBinding() throws SAPEREException;
}
