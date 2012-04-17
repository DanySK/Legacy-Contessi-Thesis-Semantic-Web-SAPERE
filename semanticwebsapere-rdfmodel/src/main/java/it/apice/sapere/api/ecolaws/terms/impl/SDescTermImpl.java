package it.apice.sapere.api.ecolaws.terms.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.terms.Formula;
import it.apice.sapere.api.ecolaws.terms.PatternNameTerm;
import it.apice.sapere.api.ecolaws.terms.SDescTerm;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
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
	 * Link to a PatternNameTerm which should be resolved to provide an LSA-id
	 * to be used for SemanticDescription retrieval.
	 */
	private final transient PatternNameTerm link;

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
		link = null;
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
		link = null;
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
		link = null;
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
	 * Builds a new {@link SDescTermImpl}.
	 * </p>
	 * 
	 * @param pattern
	 *            The interested PatternNameTerm
	 */
	public SDescTermImpl(final PatternNameTerm pattern) {
		super(pattern.getVarName() + ".D");
		link = pattern;
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
		link = src.link;
	}

	@Override
	public final LSAid getLinkedLSAid() throws SAPEREException {
		if (link == null) {
			throw new SAPEREException(
					"This term is not linked to an LSAid");
		}

		if (link.isGround() || link.isBound()) {
			return link.getValue().getValue();
		}

		throw new SAPEREException("Cannot retrieve LSAid, "
				+ "because the linked Pattern has not been resolved");
	}
	
	@Override
	public Term<SemanticDescription> clone() throws CloneNotSupportedException {
		return new SDescTermImpl(this);
	}
}
