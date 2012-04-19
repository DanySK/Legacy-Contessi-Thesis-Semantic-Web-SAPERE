package it.apice.sapere.api.ecolaws.filter.impl;

import it.apice.sapere.api.ecolaws.Filter;
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

	/** Operator String. */
	private static final transient String OP_STRING = "has-not";

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
	public HasNotFilterImpl(final HasNotFilterImpl src) {
		super(src);
	}

	@Override
	public final Filter clone() throws CloneNotSupportedException {
		return new HasNotFilterImpl(this);
	}
}
