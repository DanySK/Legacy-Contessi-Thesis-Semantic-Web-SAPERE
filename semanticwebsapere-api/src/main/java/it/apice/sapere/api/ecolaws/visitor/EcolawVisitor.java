package it.apice.sapere.api.ecolaws.visitor;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.Product;
import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.Reactant;
import it.apice.sapere.api.ecolaws.Term;

/**
 * <p>
 * This interface models pattern visitor on Ecolaws.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface EcolawVisitor {

	/**
	 * <p>
	 * Visits an Ecolaw.
	 * </p>
	 * 
	 * @param law
	 *            instance
	 */
	void visit(Ecolaw law);

	/**
	 * <p>
	 * Visits an Ecolaw's rate.
	 * </p>
	 * 
	 * @param rate
	 *            instance
	 */
	void visit(Rate<?> rate);

	/**
	 * <p>
	 * Visits an Ecolaw's product.
	 * </p>
	 * 
	 * @param prod
	 *            instance
	 */
	void visit(Product prod);

	/**
	 * <p>
	 * Visits an Ecolaw's reactant.
	 * </p>
	 * 
	 * @param react
	 *            instance
	 */
	void visit(Reactant react);

	/**
	 * <p>
	 * Visits an Ecolaw's pattern's filter.
	 * </p>
	 * 
	 * @param filter
	 *            instance
	 */
	void visit(Filter filter);
	
	/**
	 * <p>
	 * Visits an Ecolaw's pattern's term.
	 * </p>
	 * 
	 * @param term
	 *            instance
	 */
	void visit(Term<?> term);
}
