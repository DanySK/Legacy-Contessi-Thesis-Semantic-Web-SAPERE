package it.apice.sapere.api.ecolaws.formulas.impl;

import it.apice.sapere.api.ecolaws.formulas.IsFormula;


/**
 * <p>
 * This class represents a formula of the type "x is &lt;something&gt;".
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            The formula inner type
 */
public abstract class IsFormulaImpl<Type> extends AbstractFormulaImpl<Type>
		implements IsFormula<Type> {

	/**
	 * <p>
	 * Builds a new {@link IsFormulaImpl}.
	 * </p>
	 * 
	 * @param rightOperandString
	 *            The right operand as a String
	 */
	public IsFormulaImpl(final String rightOperandString) {
		super();
		setRightOp(rightOperandString);
		setOperator("=");
	}

	@Override
	public final boolean accept(final Type value) {
		return value == rightOperand();
	}
}
