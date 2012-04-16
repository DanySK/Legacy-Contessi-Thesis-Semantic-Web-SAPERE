package it.apice.sapere.api.ecolaws.terms.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.terms.VarTerm;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

/**
 * <p>
 * Implementation of the VarTerm interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            Internal value type
 */
public class VarTermImpl<Type> implements VarTerm<Type> {

	/** Variable name. */
	private final transient String name;

	/** Term Value. */
	private transient Type value;

	/**
	 * <p>
	 * Builds a new VarTermImpl.
	 * </p>
	 * 
	 * @param val
	 *            The value to be stored
	 */
	public VarTermImpl(final Type val) {
		name = null;
		setValue(val);
	}

	/**
	 * <p>
	 * Builds a new VarTermImpl.
	 * </p>
	 * 
	 * @param varName
	 *            The name of the variable
	 */
	public VarTermImpl(final String varName) {
		name = varName;
	}

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 * 
	 * @param src
	 *            Term to be cloned
	 */
	public VarTermImpl(final VarTermImpl<Type> src) {
		name = src.name;
		value = src.value;
	}

	@Override
	public final boolean isGround() {
		return !isVar();
	}

	@Override
	public final boolean isVar() {
		return name != null;
	}

	@Override
	public final boolean isBound() {
		return isVar() && value != null;
	}

	@Override
	public final Type getValue() {
		return value;
	}

	@Override
	public final void accept(final EcolawVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public final String getVarName() {
		return name;
	}

	@Override
	public void bindTo(final Term<Type> term) throws SAPEREException {
		if (term == null) {
			throw new IllegalArgumentException("Invalid value to be bound");
		}

		if (isGround()) {
			throw new SAPEREException(
					"Cannot bind a value to a term which is not variable");
		}

		value = term.getValue();
	}

	@Override
	public final void clearBinding() throws SAPEREException {
		if (isGround()) {
			throw new SAPEREException(
					"Cannot bind a value to a term which is not variable");
		}

		if (value == null) {
			throw new SAPEREException("Cannot clear binding. "
					+ "No value has been bound to this variable");
		}

		value = null;
	}

	/**
	 * <p>
	 * Sets the value of the term.
	 * </p>
	 * 
	 * @param val
	 *            The value to be set
	 */
	protected final void setValue(final Type val) {
		value = val;
	}
}
