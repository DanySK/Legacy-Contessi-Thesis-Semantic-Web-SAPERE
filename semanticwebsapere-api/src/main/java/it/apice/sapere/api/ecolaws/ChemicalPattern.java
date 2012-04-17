package it.apice.sapere.api.ecolaws;

import it.apice.sapere.api.ecolaws.terms.ListTerm;
import it.apice.sapere.api.ecolaws.terms.PatternNameTerm;
import it.apice.sapere.api.ecolaws.terms.PropertyTerm;
import it.apice.sapere.api.ecolaws.terms.SDescTerm;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

/**
 * <p>
 * This interface models a generic template which represents a component of an
 * Ecolaw.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <ExtenderType>
 *            Is the class type which extends this interface (This information
 *            is used in order to provide a correct cast for methods that
 *            provide cascade invocation).
 */
public interface ChemicalPattern<ExtenderType> {

	/**
	 * <p>
	 * Retrieves the name of the pattern.
	 * </p>
	 * 
	 * @return Symbolic name of the pattern
	 */
	PatternNameTerm getName();

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
	ExtenderType has(PropertyTerm prop, ListTerm<?> values);

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
	ExtenderType hasNot(PropertyTerm prop, ListTerm<?> values);

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
	ExtenderType assign(PropertyTerm prop, ListTerm<?> values);

	/**
	 * <p>
	 * Adds a CLONES filter.
	 * </p>
	 * 
	 * @param desc
	 *            The Semantic Description term to be cloned
	 * @return The updated pattern
	 */
	ExtenderType clone(SDescTerm desc);

	/**
	 * <p>
	 * Adds a EXTENDS filter.
	 * </p>
	 * 
	 * @param desc
	 *            The Semantic Description term to be extended
	 * @return The updated pattern
	 */
	ExtenderType extend(SDescTerm desc);

	/**
	 * <p>
	 * Adds a MATCH filter.
	 * </p>
	 * 
	 * @param term1
	 *            First term that should match with the second one
	 * @param term2
	 *            Second term that should match with the first one
	 * @return The updated pattern
	 */
	ExtenderType match(Term<?> term1, Term<?> term2);
	
	/**
	 * <p>
	 * Creates a clone of the pattern.
	 * </p>
	 * 
	 * @return The clone instance
	 * @throws CloneNotSupportedException
	 *             Cannot clone
	 */
	ChemicalPattern<ExtenderType> clone() throws CloneNotSupportedException;
}
