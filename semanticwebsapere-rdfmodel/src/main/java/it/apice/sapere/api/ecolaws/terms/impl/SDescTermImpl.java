package it.apice.sapere.api.ecolaws.terms.impl;

import it.apice.sapere.api.ecolaws.terms.Formula;
import it.apice.sapere.api.ecolaws.terms.SDescTerm;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.SemanticDescription;

/**
 * <p>
 * Implementation of the SDescTerm interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class SDescTermImpl extends AnnotatedVarTermImpl<SemanticDescription>
		implements SDescTerm {

	/**
	 * <p>
	 * Builds a new SDescTermImpl.
	 * </p>
	 * 
	 * @param varName
	 *            The name of the variable
	 */
	public SDescTermImpl(final String varName) {
		super(varName);
	}

	/**
	 * <p>
	 * Builds a new SDescTermImpl.
	 * </p>
	 * 
	 * @param varName
	 *            The name of the variable
	 * @param boolCond
	 *            The binding acceptance criteria
	 */
	public SDescTermImpl(final String varName,
			final Formula<SemanticDescription> boolCond) {
		super(varName, boolCond);
	}

	/**
	 * <p>
	 * Builds a new SDescTermImpl.
	 * </p>
	 * 
	 * @param sdesc
	 *            The semantic description
	 */
	public SDescTermImpl(final SemanticDescription sdesc) {
		super(sdesc);
	}

	/**
	 * <p>
	 * Builds a new SDescTermImpl.
	 * </p>
	 * 
	 * @param lsa
	 *            The involved LSA
	 */
	public SDescTermImpl(final LSA lsa) {
		this(lsa.getSemanticDescription());
	}

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 * 
	 * @param src
	 *            Term to be cloned
	 */
	public SDescTermImpl(final SDescTermImpl src) {
		super(src);
	}
}
