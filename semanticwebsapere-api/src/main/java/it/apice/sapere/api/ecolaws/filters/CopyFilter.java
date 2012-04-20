package it.apice.sapere.api.ecolaws.filters;

import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.terms.SDescTerm;

/**
 * <p>
 * This interface models a filter which will copy a SemanticDescription into
 * another one.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface CopyFilter extends Filter {

	/**
	 * <p>
	 * Retrieves what should be copied.
	 * </p>
	 * 
	 * @return The Semantic Description Term whose value should be copied
	 */
	SDescTerm getSource();
}
