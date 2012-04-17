package it.apice.sapere.api.ecolaws.terms.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.terms.VarTerm;

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
public class VarTermImpl<Type> extends AbstractTerm<Type> implements
		VarTerm<Type> {

	/** Variable name. */
	private final String name;

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
		setValue(src.getValue());
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
		return isVar() && getValue() != null;
	}

	@Override
	public final String getVarName() {
		return name;
	}

	@Override
	public final void bindTo(final Type value) throws SAPEREException {
		if (value == null) {
			throw new IllegalArgumentException("Invalid value to be bound");
		}

		if (isGround()) {
			throw new SAPEREException(
					"Cannot bind a value to a term which is not variable");
		}

		checkBindingPreconditions(value);

		setValue(value);
		fireBindingOccurredEvent();
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void bindTo(final Term<?> term) throws SAPEREException {
		if (term.getValue() == null) {
			throw new IllegalArgumentException("Invalid value to be bound");
		}

		if (isGround()) {
			throw new SAPEREException(
					"Cannot bind a value to a term which is not variable");
		}

		checkBindingPreconditions((Type) term.getValue());

		setValue((Type) term.getValue());
		fireBindingOccurredEvent();
	}

	/**
	 * <p>
	 * Checks if binding preconditions on provided (non null) term are
	 * satisfied.
	 * </p>
	 * 
	 * @param value
	 *            The value for the variable
	 * @throws SAPEREException
	 *             Cannot bind, preconditions not satisfied
	 */
	protected void checkBindingPreconditions(final Type value)
			throws SAPEREException {

	}

	@Override
	public final void clearBinding() throws SAPEREException {
		if (isGround()) {
			throw new SAPEREException(
					"Cannot bind a value to a term which is not variable");
		}

		if (getValue() == null) {
			throw new SAPEREException("Cannot clear binding. "
					+ "No value has been bound to this variable");
		}

		setValue(null);
		fireBindingClearedEvent();
	}

	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder();
		if (isBound() || isGround()) {
			builder.append(getValue().toString().replace('[', '(')
					.replace(']', ')'));
		} else {
			builder.append("?");
			varNamePrefix(builder);
			builder.append(name);
			varNameSuffix(builder);
		}

		return builder.toString();
	}

	/**
	 * <p>
	 * Specifies a prefix for name representation.
	 * </p>
	 *
	 * @param builder The String Builder
	 */
	public void varNamePrefix(final StringBuilder builder) {

	}

	/**
	 * <p>
	 * Specifies a suffix for name representation.
	 * </p>
	 *
	 * @param builder The String Builder
	 */
	public void varNameSuffix(final StringBuilder builder) {

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		VarTermImpl other = (VarTermImpl) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (getValue() == null) {
			if (other.getValue() != null) {
				return false;
			}
		} else if (!getValue().equals(other.getValue())) {
			return false;
		}
		return true;
	}

	@Override
	public Term<Type> clone() throws CloneNotSupportedException {
		return new VarTermImpl<Type>(this);
	}
}
