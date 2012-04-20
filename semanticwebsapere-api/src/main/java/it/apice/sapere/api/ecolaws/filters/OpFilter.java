package it.apice.sapere.api.ecolaws.filters;

import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.terms.ListTerm;
import it.apice.sapere.api.ecolaws.terms.PropertyTerm;

/**
 * <p>
 * This interface models an OpFilter, which is a filter which applies an
 * operator to a couple of operands: a property on the left and a list of values
 * on the right.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface OpFilter extends Filter {

	/**
	 * <p>
	 * Retrieves the left operand.
	 * </p>
	 * 
	 * @return Operand on the left
	 */
	PropertyTerm getLeftTerm();

	/**
	 * <p>
	 * Retrieves the right operand.
	 * </p>
	 * 
	 * @return Operand on the right
	 */
	ListTerm<?> getRightTerm();
}
