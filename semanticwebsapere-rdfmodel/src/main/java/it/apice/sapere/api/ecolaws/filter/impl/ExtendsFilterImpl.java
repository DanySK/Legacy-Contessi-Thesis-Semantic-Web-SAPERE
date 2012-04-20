package it.apice.sapere.api.ecolaws.filter.impl;

import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.filters.ExtendsFilter;
import it.apice.sapere.api.ecolaws.terms.SDescTerm;

/**
 * <p>
 * This class provides an implementation of the {@link ExtendsFilter} inteface.
 * </p>
 *
 * @author Paolo Contessi
 *
 */
public class ExtendsFilterImpl extends CopyFilterImpl implements ExtendsFilter {

	/** Copy Operator String. */
	private static final transient String COPY_OP_STRING = "extends";

	/**
	 * <p>
	 * Builds a new {@link ExtendsFilterImpl}.
	 * </p>
	 * 
	 * @param src
	 *            The source of the filter
	 */
	public ExtendsFilterImpl(final SDescTerm src) {
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
	public ExtendsFilterImpl(final ExtendsFilterImpl src) {
		super(src);
	}

	@Override
	public final Filter clone() throws CloneNotSupportedException {
		return new ExtendsFilterImpl(this);
	}
}
