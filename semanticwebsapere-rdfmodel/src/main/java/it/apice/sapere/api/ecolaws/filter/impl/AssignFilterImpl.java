package it.apice.sapere.api.ecolaws.filter.impl;

import it.apice.sapere.api.ecolaws.filters.AssignFilter;
import it.apice.sapere.api.ecolaws.terms.ListTerm;
import it.apice.sapere.api.ecolaws.terms.PropertyTerm;

/**
 * <p>
 * This class provides an implementation of the {@link AssignFilter} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class AssignFilterImpl extends OpFilterImpl implements AssignFilter {

	/**
	 * <p>
	 * Bulids a new {@link AssignFilterImpl}.
	 * </p>
	 * 
	 * @param leftTerm
	 *            Left-side operand (a property term)
	 * @param rightTerm
	 *            Right-side operand (a list of terms)
	 */
	public AssignFilterImpl(final PropertyTerm leftTerm,
			final ListTerm<?> rightTerm) {
		super(leftTerm, rightTerm);
	}

}
