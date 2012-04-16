package it.apice.sapere.api.ecolaws.filter.impl;

import it.apice.sapere.api.ecolaws.filters.HasNotFilter;
import it.apice.sapere.api.ecolaws.terms.ListTerm;
import it.apice.sapere.api.ecolaws.terms.PropertyTerm;

/**
 * <p>
 * This class provides an implementation of the {@link HasNotFilter} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class HasNotFilterImpl extends OpFilterImpl implements HasNotFilter {

	/**
	 * <p>
	 * Bulids a new {@link HasNotFilterImpl}.
	 * </p>
	 * 
	 * @param leftTerm
	 *            Left-side operand (a property term)
	 * @param rightTerm
	 *            Right-side operand (a list of terms)
	 */
	public HasNotFilterImpl(final PropertyTerm leftTerm,
			final ListTerm<?> rightTerm) {
		super(leftTerm, rightTerm);
	}

}
