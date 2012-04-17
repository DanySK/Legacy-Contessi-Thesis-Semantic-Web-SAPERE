package it.apice.sapere.api.ecolaws.filter.impl;

import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.filters.HasFilter;
import it.apice.sapere.api.ecolaws.terms.ListTerm;
import it.apice.sapere.api.ecolaws.terms.PropertyTerm;

/**
 * <p>
 * This class provides an implementation of the {@link HasFilter} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class HasFilterImpl extends OpFilterImpl implements HasFilter {

	/** Operator String. */
	private static final transient String OP_STRING = "has";

	/**
	 * <p>
	 * Bulids a new {@link HasFilterImpl}.
	 * </p>
	 * 
	 * @param leftTerm
	 *            Left-side operand (a property term)
	 * @param rightTerm
	 *            Right-side operand (a list of terms)
	 */
	public HasFilterImpl(final PropertyTerm leftTerm,
			final ListTerm<?> rightTerm) {
		super(leftTerm, rightTerm);
		setOpString(OP_STRING);
	}

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 * 
	 * @param src
	 *            The source
	 */
	public HasFilterImpl(final HasFilterImpl src) {
		super(src);
	}

	@Override
	public Filter clone() throws CloneNotSupportedException {
		return new HasFilterImpl(this);
	}
}
