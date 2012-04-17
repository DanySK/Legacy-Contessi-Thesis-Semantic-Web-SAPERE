package it.apice.sapere.api.ecolaws.terms.impl;

import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.terms.observers.BindingObserver;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * This class implements the {@link Term} interface. It provides basics.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            The internal value type
 */
public abstract class AbstractTerm<Type> implements Term<Type> {

	/** Term Value. */
	private Type value;

	/** Observers list. */
	private final transient List<BindingObserver<Type>> obss = 
			new LinkedList<BindingObserver<Type>>();

	@Override
	public final Type getValue() {
		return value;
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

	@Override
	public final void accept(final EcolawVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public final void addBindingObserver(final BindingObserver<Type> obs) {
		obss.add(obs);
	}

	@Override
	public final void removeBindingObserver(final BindingObserver<Type> obs) {
		obss.remove(obs);
	}

	/**
	 * <p>
	 * Notifies all the observers that a new binding occurred.
	 * </p>
	 */
	protected final void fireBindingOccurredEvent() {
		for (BindingObserver<Type> obs : obss) {
			obs.bindingOccurred(this, value);
		}
	}

	/**
	 * <p>
	 * Notifies all the observers that the existing binding has been cleared.
	 * </p>
	 */
	protected final void fireBindingClearedEvent() {
		for (BindingObserver<Type> obs : obss) {
			obs.bindingCleared(this);
		}
	}
	
	@Override
	public Term<Type> clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (Term<Type>) super.clone();
	}
}
