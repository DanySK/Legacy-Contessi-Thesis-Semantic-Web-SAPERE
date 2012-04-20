package it.apice.sapere.api.ecolaws.filter.impl;

import it.apice.sapere.api.ecolaws.Filter;
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
public abstract class OpFilterImpl implements OpFilter {

	/** String that represents the operator. */
	private String opString = "";

	/** Left-side operand. */
	private final PropertyTerm left;

	/** Right-side operand. */
	private final ListTerm<?> right;

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

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 * 
	 * @param src
	 *            The source
	 */
	@SuppressWarnings("rawtypes")
	public OpFilterImpl(final OpFilterImpl src) {
		try {
			if (src.left.isVar()) {
				left = (PropertyTerm) src.left.clone();
			} else {
				left = src.left;
			}
			
			if (src.right.isVar()) {
				right = (ListTerm) src.right.clone();
			} else {
				right = src.right;
			}
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException("Cannot clone", e);
		}
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

	/**
	 * <p>
	 * Sets Operator String, used by toString() method.
	 * </p>
	 * 
	 * @param op
	 *            The op string
	 */
	protected final void setOpString(final String op) {
		opString = op;
	}

	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder();

		builder.append(left.toString()).append(" ").append(opString)
				.append(" ").append(right.toString());
		return builder.toString();
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result *= prime;
		if (left != null) {
			result += left.hashCode();
		}

		result *= prime;
		if (opString != null) {
			result += opString.hashCode();
		}

		result *= prime;
		if (right != null) {
			result += right.hashCode();
		}
		return result;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		OpFilterImpl other = (OpFilterImpl) obj;
		if (left == null) {
			if (other.left != null) {
				return false;
			}
		} else if (!left.equals(other.left)) {
			return false;
		}
		if (opString == null) {
			if (other.opString != null) {
				return false;
			}
		} else if (!opString.equals(other.opString)) {
			return false;
		}
		if (right == null) {
			if (other.right != null) {
				return false;
			}
		} else if (!right.equals(other.right)) {
			return false;
		}
		return true;
	}

	@Override
	public Filter clone() throws CloneNotSupportedException {
		return (Filter) super.clone();
	}
}
