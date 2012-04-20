package it.apice.sapere.api.ecolaws.terms;

/**
 * <p>
 * This interface models a Value inside an Ecolaw.
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
public interface ValueTerm<Type> extends
		AnnotatedVarTerm<Type> {

}
