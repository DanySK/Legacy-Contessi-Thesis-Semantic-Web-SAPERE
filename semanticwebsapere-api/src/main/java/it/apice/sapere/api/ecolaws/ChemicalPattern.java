package it.apice.sapere.api.ecolaws;

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

	/**
	 * <p>
	 * Adds an HAS filter.
	 * </p>
	 * 
	 * @param prop
	 *            The property term involved
	 * @param values
	 *            The values of the property
	 * @return The updated pattern
	 */
	ChemicalPattern has(Term prop, Term values);

	/**
	 * <p>
	 * Adds an HAS-NOT filter.
	 * </p>
	 * 
	 * @param prop
	 *            The property term involved
	 * @param values
	 *            The values of the property
	 * @return The updated pattern
	 */
	ChemicalPattern hasNot(Term prop, Term values);

	/**
	 * <p>
	 * Adds an ASSIGN (=) filter.
	 * </p>
	 * 
	 * @param prop
	 *            The property term involved
	 * @param values
	 *            The values of the property
	 * @return The updated pattern
	 */
	ChemicalPattern assign(Term prop, Term values);

	/**
	 * <p>
	 * Adds a CLONES filter.
	 * </p>
	 * 
	 * @param desc
	 *            The Semantic Description term to be cloned
	 * @return The updated pattern
	 */
	ChemicalPattern clone(Term desc);

	/**
	 * <p>
	 * Adds a EXTENDS filter.
	 * </p>
	 * 
	 * @param desc
	 *            The Semantic Description term to be extended
	 * @return The updated pattern
	 */
	ChemicalPattern extend(Term desc);

	ChemicalPattern match(Term term1, Term term2);

	ChemicalPattern smatch(Term term1, Term term2);
}
