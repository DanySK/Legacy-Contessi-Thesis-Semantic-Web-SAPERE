package it.apice.sapere.api.ecolaws.filters.terms;

/**
 * <p>
 * This interface models an annotated variable term.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface AnnotatedVarTerm extends VarTerm {

	/**
	 * <p>
	 * Retrieves the formula of the annotation.
	 * </p>
	 * 
	 * @return The formula for domain narrowing
	 */
	Formula getFormula();
}
