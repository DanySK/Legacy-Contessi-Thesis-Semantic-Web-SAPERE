package it.apice.sapere.api.ecolaws.formulas.impl;

import it.apice.sapere.api.ecolaws.formulas.LtEqFormula;

/**
 * <p>
 * This class represents a formula of the type "x &lt;= &lt;something&gt;".
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            The formula inner type
 */
public abstract class LtEqFormulaImpl<Type extends Comparable<Type>> extends
		AbstractFormulaImpl<Type> implements LtEqFormula<Type> {

	/**
	 * <p>
	 * Builds a new {@link LtEqFormulaImpl}.
	 * </p>
	 * 
	 * @param rightOperandString
	 *            The right operand as a String
	 */
	public LtEqFormulaImpl(final String rightOperandString) {
		super();
		setRightOp(rightOperandString);
	}

	@Override
	public final boolean accept(final Type value) {
		return value.compareTo(rightOperand()) <= 0;
	}

}
