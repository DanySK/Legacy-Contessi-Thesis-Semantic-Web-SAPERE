package it.apice.sapere.api.ecolaws.formulas.impl;

import it.apice.sapere.api.ecolaws.formulas.LtFormula;

/**
 * <p>
 * This class represents a formula of the type "x &lt; &lt;something&gt;".
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            The formula inner type
 */
public abstract class LtFormulaImpl<Type extends Comparable<Type>> extends
		AbstractFormulaImpl<Type> implements LtFormula<Type> {

	/**
	 * <p>
	 * Builds a new {@link LtFormulaImpl}.
	 * </p>
	 * 
	 * @param rightOperandString
	 *            The right operand as a String
	 */
	public LtFormulaImpl(final String rightOperandString) {
		super();
		setRightOp(rightOperandString);
		setOperator("<");
	}

	@Override
	public final boolean accept(final Type value) {
		return value.compareTo(rightOperand()) < 0;
	}

}
