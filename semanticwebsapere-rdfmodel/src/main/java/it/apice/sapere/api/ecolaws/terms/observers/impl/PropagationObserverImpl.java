package it.apice.sapere.api.ecolaws.terms.observers.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.terms.VarTerm;
import it.apice.sapere.api.ecolaws.terms.observers.BindingObserver;

/**
 * <p>
 * This class is the implementation of the {@link BindingObserver} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            Common value type
 */
public class PropagationObserverImpl<Type> implements BindingObserver<Type> {

	/** The destination of the propagation. */
	private final transient VarTerm<Type> dest;

	/**
	 * <p>
	 * Builds a new {@link PropagationObserverImpl}.
	 * </p>
	 * 
	 * @param aDest
	 *            The destination of the propagation
	 */
	public PropagationObserverImpl(final VarTerm<Type> aDest) {
		if (aDest == null) {
			throw new IllegalArgumentException("");
		}

		dest = aDest;
	}

	@Override
	public final void bindingOccurred(final Term<Type> boundTerm,
			final Type value) {
		try {
			dest.bindTo(value);
		} catch (SAPEREException e) {
			System.err.println("Cannot propagate the binding from " + boundTerm
					+ " to " + dest + "(value: " + value + ", desc: "
					+ e.getMessage() + ")");
		}
	}

	@Override
	public final void bindingCleared(final Term<Type> boundTerm) {
		try {
			dest.clearBinding();
		} catch (SAPEREException e) {
			System.err.println("Cannot propagate the binding clearing from "
					+ boundTerm + " to " + dest);
		}
	}

}
