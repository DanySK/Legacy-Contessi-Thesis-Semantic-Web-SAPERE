package it.apice.sapere.api.ecolaws.filter.impl;

import it.apice.sapere.api.ecolaws.filters.CopyFilter;
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
public class CopyFilterImpl implements CopyFilter {

	/** Reference to the Semantic Description to be handled. */
	private final transient SDescTerm source;

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

	@Override
	public final void accept(final EcolawVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public final SDescTerm getSource() {
		return source;
	}

}
