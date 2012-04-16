package it.apice.sapere.api.ecolaws.terms;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.lsas.SemanticDescription;

/**
 * <p>
 * This interface models a SemanticDescription that can be cloned or extended
 * inside an Ecolaw.
 * </p>
 * <p>
 * It can be a ground, or a (un-)bound variable term.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface SDescTerm extends AnnotatedVarTerm<SemanticDescription> {

	/**
	 * <p>
	 * Retrieves, if possible, the LSA-id needed to retrieve the LSA's Semantic
	 * Description to be cloned or extended from the LSA-space.
	 * </p>
	 * <p>
	 * This method should be used only if the term has been generated starting
	 * from a PatternName, otherwise is useless; no LSA-id can be returned until
	 * the Pattern is resolved and the related term is bound.
	 * </p>
	 * 
	 * @return An LSA-id to be passed to <code>LSAspace.read</code>
	 * @throws SAPEREException
	 *             The LSAid cannot be retrieved
	 */
	LSAid getLinkedLSAid() throws SAPEREException;
}
