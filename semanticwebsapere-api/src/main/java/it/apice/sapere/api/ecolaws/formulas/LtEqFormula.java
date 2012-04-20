package it.apice.sapere.api.ecolaws.formulas;

import it.apice.sapere.api.ecolaws.terms.Formula;

/**
 * <p>
 * This interface models a Formula with operator "&lt;=".
 * </p>
 *
 * @author Paolo Contessi
 *
 * @param <Type>
 */
public interface LtEqFormula<Type> extends Formula<Type> {

	/**
	 * <p>
	 * Provides the right operand evaluator of the formula.
	 * </p>
	 * 
	 * @return The evaluated right operand
	 */
	Type rightOperand();
}
