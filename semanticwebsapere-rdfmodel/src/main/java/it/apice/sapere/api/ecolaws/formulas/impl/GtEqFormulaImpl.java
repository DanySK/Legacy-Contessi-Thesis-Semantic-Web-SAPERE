package it.apice.sapere.api.ecolaws.formulas.impl;

import it.apice.sapere.api.ecolaws.formulas.GtEqFormula;

/**
 * <p>
 * This class represents a formula of the type "x &gt;= &lt;something&gt;".
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            The formula inner type
 */
public abstract class GtEqFormulaImpl<Type extends Comparable<Type>> extends
		AbstractFormulaImpl<Type> implements GtEqFormula<Type> {

	/**
	 * <p>
	 * Builds a new {@link GtEqFormulaImpl}.
	 * </p>
	 * 
	 * @param rightOperandString
	 *            The right operand as a String
	 */
	public GtEqFormulaImpl(final String rightOperandString) {
		super();
		setRightOp(rightOperandString);
	}

	@Override
	public final boolean accept(final Type value) {
		return value.compareTo(rightOperand()) >= 0;
	}

}
