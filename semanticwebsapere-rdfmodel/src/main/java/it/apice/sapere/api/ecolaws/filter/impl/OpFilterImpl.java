package it.apice.sapere.api.ecolaws.filter.impl;

import it.apice.sapere.api.ecolaws.filters.OpFilter;
import it.apice.sapere.api.ecolaws.terms.ListTerm;
import it.apice.sapere.api.ecolaws.terms.PropertyTerm;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

/**
 * <p>
 * This class provides an implementation of the {@link OpFilter} interface.
 * </p>
 *
 * @author Paolo Contessi
 *
 */
public class OpFilterImpl implements OpFilter {

	/** Left-side operand. */
	private final transient PropertyTerm left;

	/** Right-side operand. */
	private final transient ListTerm<?> right;

	/**
	 * <p>
	 * Builds a new {@link OpFilterImpl}.
	 * </p>
	 * 
	 * @param leftTerm
	 *            Left-side operand (a property term)
	 * @param rightTerm
	 *            Right-side operand (a list of terms)
	 */
	public OpFilterImpl(final PropertyTerm leftTerm, 
			final ListTerm<?> rightTerm) {
		if (leftTerm == null) {
			throw new IllegalArgumentException("Invalid left operand provided");
		}

		if (rightTerm == null) {
			throw new IllegalArgumentException("Invalid right "
					+ "operand provided");
		}

		left = leftTerm;
		right = rightTerm;
	}

	@Override
	public final void accept(final EcolawVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public final PropertyTerm getLeftTerm() {
		return left;
	}

	@Override
	public final ListTerm<?> getRightTerm() {
		return right;
	}

}
