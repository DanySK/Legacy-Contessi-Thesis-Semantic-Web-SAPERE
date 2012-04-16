package it.apice.sapere.api.ecolaws.filter.impl;

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
	}

}
