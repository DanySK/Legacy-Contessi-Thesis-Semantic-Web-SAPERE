package it.apice.sapere.api.ecolaws.filter.impl;

import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.filters.MatchFilter;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

/**
 * <p>
 * This class provides an implementation of the {@link MatchFilter} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class MatchFilterImpl implements MatchFilter {

	/** Operator String. */
	private static final transient String OP_STRING = "matches";

	/** Left-side operand. */
	private final Term<?> left;

	/** Right-side operand. */
	private final Term<?> right;

	/**
	 * <p>
	 * Builds a new {@link MatchFilterImpl}.
	 * </p>
	 * 
	 * @param leftTerm
	 *            Left-side operand (a property term)
	 * @param rightTerm
	 *            Right-side operand (a list of terms)
	 */
	public MatchFilterImpl(final Term<?> leftTerm, final Term<?> rightTerm) {
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
	public MatchFilterImpl(final MatchFilterImpl src) {
		try {
			if (src.left.isVar()) {
				left = (Term) src.left.clone();
			} else {
				left = src.left;
			}
			
			if (src.right.isVar()) {
				right = (Term) src.right.clone();
			} else {
				right = src.right;
			}
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException("Cannot clone", e);
		}
	}

	@Override
	public final Filter clone() throws CloneNotSupportedException {
		return new MatchFilterImpl(this);
	}

	@Override
	public final Term<?> getLeftTerm() {
		return left;
	}

	@Override
	public final Term<?> getRightTerm() {
		return right;
	}

	@Override
	public final void accept(final EcolawVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder();

		builder.append(left.toString()).append(" ").append(OP_STRING)
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
		if (OP_STRING != null) {
			result += OP_STRING.hashCode();
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
		final MatchFilterImpl other = (MatchFilterImpl) obj;
		if (left == null) {
			if (other.left != null) {
				return false;
			}
		} else if (!left.equals(other.left)) {
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
}
