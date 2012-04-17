package it.apice.sapere.api.ecolaws.filter.impl;

import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.filters.CopyFilter;
import it.apice.sapere.api.ecolaws.terms.ListTerm;
import it.apice.sapere.api.ecolaws.terms.PropertyTerm;
import it.apice.sapere.api.ecolaws.terms.SDescTerm;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

/**
 * <p>
 * This class implements {@link CopyFilter} interface.
 * </p>
 *
 * @author Paolo Contessi
 *
 */
public abstract class CopyFilterImpl implements CopyFilter {

	/** String that represents the operator. */
	private String copyOpString = "";

	/** Reference to the Semantic Description to be handled. */
	private final SDescTerm source;

	/**
	 * <p>
	 * Builds a new {@link CopyFilterImpl}.
	 * </p>
	 * 
	 * @param src
	 *            The source of the filter
	 */
	public CopyFilterImpl(final SDescTerm src) {
		if (src == null) {
			throw new IllegalArgumentException("Invalid source provided");
		}

		source = src;
	}

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 * 
	 * @param src
	 *            The source
	 */
	public CopyFilterImpl(final CopyFilterImpl src) {
		try {
			if (src.source.isVar()) {
				source = (SDescTerm) src.source.clone();
			} else {
				source = src.source;
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
	public final SDescTerm getSource() {
		return source;
	}

	/**
	 * <p>
	 * Sets Copy Operator String, used by toString() method.
	 * </p>
	 * 
	 * @param op
	 *            The copy op string
	 */
	protected final void setCopyOpString(final String op) {
		copyOpString = op;
	}

	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder();

		builder.append(copyOpString).append(" ")
				.append(source.toString());
		return builder.toString();
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((copyOpString == null) ? 0 : copyOpString.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		CopyFilterImpl other = (CopyFilterImpl) obj;
		if (copyOpString == null) {
			if (other.copyOpString != null) {
				return false;
			}
		} else if (!copyOpString.equals(other.copyOpString)) {
			return false;
		}
		if (source == null) {
			if (other.source != null) {
				return false;
			}
		} else if (!source.equals(other.source)) {
			return false;
		}
		return true;
	}
	
	@Override
	public Filter clone() throws CloneNotSupportedException {
		return (Filter) super.clone();
	}
}
