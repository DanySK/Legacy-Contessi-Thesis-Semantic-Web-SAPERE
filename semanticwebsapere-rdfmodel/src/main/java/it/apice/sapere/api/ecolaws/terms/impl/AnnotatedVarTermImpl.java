package it.apice.sapere.api.ecolaws.terms.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.terms.AnnotatedVarTerm;
import it.apice.sapere.api.ecolaws.terms.Formula;

/**
 * <p>
 * This is the implementation of the AnnotatedVarTerm interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            Internal value type
 */
public class AnnotatedVarTermImpl<Type> extends VarTermImpl<Type> implements
		AnnotatedVarTerm<Type> {

	/** The boolean condition for value acceptance. */
	private final transient Formula<Type> formula;

	/**
	 * <p>
	 * Builds a new AnnotatedVarTermImpl.
	 * </p>
	 * 
	 * @param val
	 *            The value to be stored
	 */
	public AnnotatedVarTermImpl(final Type val) {
		super(val);
		formula = null;
	}

	/**
	 * <p>
	 * Builds a new AnnotatedVarTermImpl.
	 * </p>
	 * 
	 * @param varName
	 *            The name of the variable
	 */
	public AnnotatedVarTermImpl(final String varName) {
		super(varName);
		formula = null;
	}

	/**
	 * <p>
	 * Builds a new AnnotatedVarTermImpl.
	 * </p>
	 * 
	 * @param varName
	 *            The name of the variable
	 * @param boolCond
	 *            The acceptance criteria to be applied to the value
	 */
	public AnnotatedVarTermImpl(final String varName,
			final Formula<Type> boolCond) {
		super(varName);

		if (boolCond == null) {
			throw new IllegalArgumentException("Invalid formula");
		}

		formula = boolCond;
	}

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 * 
	 * @param src
	 *            Term to be cloned
	 */
	public AnnotatedVarTermImpl(final AnnotatedVarTermImpl<Type> src) {
		super(src);

		formula = src.formula;
	}

	@Override
	public final Formula<Type> getFormula() {
		return formula;
	}

	@Override
	protected final void checkBindingPreconditions(final Type value)
			throws SAPEREException {
		super.checkBindingPreconditions(value);

		if (formula != null && !formula.accept(value)) {
			throw new SAPEREException("The value does not fit the annotation");
		}
	}
}
