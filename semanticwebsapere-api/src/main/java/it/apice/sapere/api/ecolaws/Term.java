package it.apice.sapere.api.ecolaws;

import it.apice.sapere.api.ecolaws.terms.observers.BindingObserver;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

/**
 * <p>
 * This interface models an Ecolaw Term, used in filters.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            The type of the term's value
 */
public interface Term<Type> {

	/**
	 * <p>
	 * Checks if the term is ground (not a variable).
	 * </p>
	 * 
	 * @return True if ground, false otherwise
	 */
	boolean isGround();

	/**
	 * <p>
	 * Checks if the term is a variable (not ground).
	 * </p>
	 * 
	 * @return True if is a variable, false otherwise
	 */
	boolean isVar();

	/**
	 * <p>
	 * Checks if the variable term has a bound value.
	 * </p>
	 * 
	 * @return True if is a variable and has an assigned value, false otherwise
	 */
	boolean isBound();

	/**
	 * <p>
	 * Retrieves the value stored in the term.
	 * </p>
	 * 
	 * @return The inner value
	 */
	Type getValue();

	/**
	 * <p>
	 * Accepts a Visitor.
	 * </p>
	 * 
	 * @param visitor
	 *            Ecolaw visitor
	 */
	void accept(EcolawVisitor visitor);

	/**
	 * <p>
	 * Registers a new BindingObserver.
	 * </p>
	 * 
	 * @param obs
	 *            The observer
	 */
	void addBindingObserver(final BindingObserver<Type> obs);

	/**
	 * <p>
	 * Removes a BindingObserver.
	 * </p>
	 * 
	 * @param obs
	 *            The observer
	 */
	void removeBindingObserver(final BindingObserver<Type> obs);

	/**
	 * <p>
	 * Creates a clone of the term.
	 * </p>
	 * 
	 * @return The clone instance
	 * @throws CloneNotSupportedException
	 *             Cannot clone
	 */
	Term<Type> clone() throws CloneNotSupportedException;
}
