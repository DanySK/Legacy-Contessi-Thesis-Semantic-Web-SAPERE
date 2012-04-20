package it.apice.sapere.api.ecolaws.terms;

import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.values.URIValue;

/**
 * <p>
 * This interface models a Property inside an Ecolaw.
 * </p>
 * <p>
 * It can be a ground, or a (un-)bound variable term.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface PropertyTerm extends AnnotatedVarTerm<PropertyName> {

	/**
	 * <p>
	 * Converts the property term to a value term.
	 * </p>
	 * <p>
	 * This method is useful when a property should be read from a list of
	 * property values.
	 * 
	 * @return A value term containing the same URI of the Property
	 */
	ValueTerm<URIValue> toValueTerm();
}
