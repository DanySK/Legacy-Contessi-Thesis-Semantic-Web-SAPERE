package it.apice.sapere.api.ecolaws.terms.impl;

import it.apice.sapere.api.ecolaws.terms.Formula;
import it.apice.sapere.api.ecolaws.terms.PatternNameTerm;
import it.apice.sapere.api.lsas.values.LSAidValue;

/**
 * <p>
 * Implementation of the SDescTerm interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class PatternNameTermImpl extends AnnotatedVarTermImpl<LSAidValue>
		implements PatternNameTerm {

	/**
	 * <p>
	 * Builds a new SDescTermImpl.
	 * </p>
	 * 
	 * @param varName
	 *            The name of the variable
	 */
	public PatternNameTermImpl(final String varName) {
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
	public PatternNameTermImpl(final String varName,
			final Formula<LSAidValue> boolCond) {
		super(varName, boolCond);
	}

	/**
	 * <p>
	 * Builds a new SDescTermImpl.
	 * </p>
	 * 
	 * @param lsaId
	 *            The LSA-id
	 */
	public PatternNameTermImpl(final LSAidValue lsaId) {
		super(lsaId);
	}
}
