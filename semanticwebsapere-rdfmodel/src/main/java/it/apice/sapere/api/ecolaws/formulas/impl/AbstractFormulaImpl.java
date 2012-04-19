package it.apice.sapere.api.ecolaws.formulas.impl;

import it.apice.sapere.api.ecolaws.terms.Formula;

/**
 * <p>
 * This class represents a basic implementation of {@link Formula} interface.
 * </p>
 *
 * @author Paolo Contessi
 *
 * @param <Type> Formula inner type
 */
abstract class AbstractFormulaImpl<Type> implements Formula<Type> {

	/** Left operand string. */
	private transient String leftOp;

	/** Operator string. */
	private transient String operator;

	/** Right operand string. */
	private transient String rightOp;

	@Override
	public abstract boolean accept(final Type value);

	@Override
	public final String getStringRepr() {
		return String.format("%s %s %s", leftOp, operator, rightOp);
	}

	@Override
	public final String getOperator() {
		return operator;
	}

	@Override
	public final String getRightOp() {
		return rightOp;
	}

	@Override
	public final void setLeftOp(final String lOp) {
		if (lOp == null || lOp.equals("")) {
			throw new IllegalArgumentException("Invalid variable name");
		}

		leftOp = lOp;
	}

	/**
	 * <p>
	 * Sets the operator string of the formula.
	 * </p>
	 * 
	 * @param op
	 *            The operator as a String
	 */
	protected final void setOperator(final String op) {
		if (op == null || op.equals("")) {
			throw new IllegalArgumentException("Invalid operator");
		}

		operator = op;
	}

	/**
	 * <p>
	 * Sets the right operand string of the formula.
	 * </p>
	 * 
	 * @param rOp
	 *            The right operand as a String
	 */
	protected final void setRightOp(final String rOp) {
		if (rOp == null || rOp.equals("")) {
			throw new IllegalArgumentException("Invalid right operand");
		}

		rightOp = rOp;
	}
}
