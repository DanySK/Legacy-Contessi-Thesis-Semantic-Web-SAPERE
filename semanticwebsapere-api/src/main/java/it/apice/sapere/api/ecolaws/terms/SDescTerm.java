package it.apice.sapere.api.ecolaws.terms;

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
public interface SDescTerm extends
		AnnotatedVarTerm<SemanticDescription> {

}
