package it.apice.sapere.api.ecolaws.terms;

/**
 * <p>
 * This interface models a logic-like annotated variable term for an Ecolaw.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            The type of the term's value
 */
public interface AnnotatedVarTerm<Type> extends VarTerm<Type> {

	/**
	 * <p>
	 * Retrieves the boolean condition that each assigned value must satisfy.
	 * </p>
	 * 
	 * @return A boolean condition (the annotation)
	 */
	Formula<Type> getFormula();
}
