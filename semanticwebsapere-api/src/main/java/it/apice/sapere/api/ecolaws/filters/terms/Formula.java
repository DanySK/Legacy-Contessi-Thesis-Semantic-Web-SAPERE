package it.apice.sapere.api.ecolaws.filters.terms;

/**
 * <p>
 * This interface models a Formula used to annotate a Variable term.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <VarType>
 *            The expected type for the variable
 */
public interface Formula<VarType> {

	/**
	 * <p>
	 * Checks if provided value can be assigned to the annotated variable.
	 * </p>
	 * 
	 * @param value
	 *            The candidate value
	 * @return True if can be assigned, false otherwise
	 */
	boolean accept(VarType value);
}
