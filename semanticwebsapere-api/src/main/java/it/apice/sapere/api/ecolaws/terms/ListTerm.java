package it.apice.sapere.api.ecolaws.terms;

import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.lsas.values.PropertyValue;

import java.util.List;

/**
 * <p>
 * This interface models a List of values inside an Ecolaw.
 * </p>
 * <p>
 * It can be a ground, or a (un-)bound variable term.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            The type of the term's value
 */
public interface ListTerm<Type extends PropertyValue<?>> extends
		AnnotatedVarTerm<List<Term<Type>>> {

}
