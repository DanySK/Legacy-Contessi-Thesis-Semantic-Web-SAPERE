package it.apice.sapere.api.lsas.values;

import it.apice.sapere.api.lsas.SemanticDescription;

/**
 * <p>
 * Interface that represents a Semantic Description value that can be associated
 * to a property.
 * </p>
 * <p>
 * This type enables nesting capabilities in the LSA's structure.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface SDescValue extends
		PropertyValue<SemanticDescription, SDescValue> {

}
