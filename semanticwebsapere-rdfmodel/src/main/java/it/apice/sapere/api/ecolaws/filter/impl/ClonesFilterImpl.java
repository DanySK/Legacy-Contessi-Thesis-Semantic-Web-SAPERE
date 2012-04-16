package it.apice.sapere.api.ecolaws.filter.impl;

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
	}

}
