package it.apice.sapere.api.ecolaws.terms.observers;

import it.apice.sapere.api.ecolaws.Term;

/**
 * <p>
 * This interface models an entity which is capable of observing the binding
 * status of a term.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            The type used by observed term
 */
public interface BindingObserver<Type> {

	/**
	 * <p>
	 * Notifies that a new binding has been occurred.
	 * </p>
	 * 
	 * @param boundTerm
	 *            The term that has been resolved
	 * @param value
	 *            The actual term's value
	 */
	void bindingOccurred(Term<Type> boundTerm, Type value);

	/**
	 * <p>
	 * Notifies that the existing binding has been cleared.
	 * </p>
	 * 
	 * @param boundTerm
	 *            The term that has been resolved
	 */
	void bindingCleared(Term<Type> boundTerm);
}
