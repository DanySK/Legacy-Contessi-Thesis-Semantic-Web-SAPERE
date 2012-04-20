package it.apice.sapere.api.ecolaws.terms;

import it.apice.sapere.api.lsas.values.LSAidValue;

/**
 * <p>
 * This interface models a PatternName inside an Ecolaw, which is a variable
 * that represents an LSA-id for a matching LSA.
 * </p>
 * <p>
 * It can be a ground, or a (un-)bound variable term.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface PatternNameTerm extends VarTerm<LSAidValue> {

}
