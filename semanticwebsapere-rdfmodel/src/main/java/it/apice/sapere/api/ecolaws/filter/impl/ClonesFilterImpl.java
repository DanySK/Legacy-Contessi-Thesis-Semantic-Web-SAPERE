package it.apice.sapere.api.ecolaws.filter.impl;

import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.filters.ClonesFilter;
import it.apice.sapere.api.ecolaws.terms.SDescTerm;

/**
 * <p>
 * This class provides an implementation of the {@link ClonesFilter} inteface.
 * </p>
 *
 * @author Paolo Contessi
 *
 */
public class ClonesFilterImpl extends CopyFilterImpl implements ClonesFilter {

	/** Copy Operator String. */
	private static final transient String COPY_OP_STRING = "clones";

	/**
	 * <p>
	 * Builds a new {@link ClonesFilterImpl}.
	 * </p>
	 * 
	 * @param src
	 *            The source of the filter
	 */
	public ClonesFilterImpl(final SDescTerm src) {
		super(src);
		setCopyOpString(COPY_OP_STRING);
	}

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 * 
	 * @param src
	 *            The source
	 */
	public ClonesFilterImpl(final ClonesFilterImpl src) {
		super(src);
	}

	@Override
	public Filter clone() throws CloneNotSupportedException {
		return new ClonesFilterImpl(this);
	}
}
