package it.apice.sapere.api.ecolaws;

import it.apice.sapere.api.ecolaws.filters.terms.PropertyTerm;
import it.apice.sapere.api.ecolaws.filters.terms.SemDescTerm;
import it.apice.sapere.api.ecolaws.filters.terms.ValuesListTerm;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

/**
 * <p>
 * This interface models a generic template which represents a component of an
 * Ecolaw.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface ChemicalPattern {

	/**
	 * <p>
	 * Retrieves the name of the pattern.
	 * </p>
	 * 
	 * @return Symbolic name of the pattern
	 */
	PatternName getName();

	/**
	 * <p>
	 * Retrieves all filters applied to this pattern.
	 * </p>
	 * 
	 * @return List of all filters
	 */
	Filter[] filters();

	/**
	 * <p>
	 * Accepts a Visitor.
	 * </p>
	 * 
	 * @param visitor
	 *            Ecolaw visitor
	 */
	void accept(EcolawVisitor visitor);

	ChemicalPattern has(PropertyTerm term, ValuesListTerm values);

	ChemicalPattern has_not(PropertyTerm term, ValuesListTerm values);

	ChemicalPattern assign(PropertyTerm term, ValuesListTerm values);

	ChemicalPattern clone(SemDescTerm term);

	ChemicalPattern extend(SemDescTerm term);

	ChemicalPattern match(Term term1, Term term2);
	
	ChemicalPattern s_match(Term term1, Term term2);
}
