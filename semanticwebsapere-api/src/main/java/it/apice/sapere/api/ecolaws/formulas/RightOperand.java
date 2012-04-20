package it.apice.sapere.api.ecolaws.formulas;

/**
 * <p>
 * This interface models the right operand of a formula.
 * </p>
 *
 * @author Paolo Contessi
 *
 * @param <Type> Formula operand inner type
 */
public interface RightOperand<Type> {

	/**
	 * <p>
	 * Provides the right operand evaluator of the formula.
	 * </p>
	 * 
	 * @return The evaluated right operand
	 */
	Type rightOperand();
}
