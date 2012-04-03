package it.apice.sapere.api.ecolaws.filters;

import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.Term;

/**
 * <p>
 * This interface models an Op filter, which is a filter which applies an
 * operator to a couple of operands.
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
	Term getLeftTerm();

	/**
	 * <p>
	 * Retrieves the right operand.
	 * </p>
	 * 
	 * @return Operand on the right
	 */
	Term getRightTerm();
}
