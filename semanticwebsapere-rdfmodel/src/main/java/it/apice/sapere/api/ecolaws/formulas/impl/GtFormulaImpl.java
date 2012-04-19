package it.apice.sapere.api.ecolaws.formulas.impl;

import it.apice.sapere.api.ecolaws.formulas.GtFormula;

/**
 * <p>
 * This class represents a formula of the type "x &gt; &lt;something&gt;".
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            The formula inner type
 */
public abstract class GtFormulaImpl<Type extends Comparable<Type>> extends
		AbstractFormulaImpl<Type> implements GtFormula<Type> {

	/**
	 * <p>
	 * Builds a new {@link GtFormulaImpl}.
	 * </p>
	 * 
	 * @param rightOperandString
	 *            The right operand as a String
	 */
	public GtFormulaImpl(final String rightOperandString) {
		super();
		setRightOp(rightOperandString);
	}

	@Override
	public final boolean accept(final Type value) {
		return value.compareTo(rightOperand()) > 0;
	}

}
