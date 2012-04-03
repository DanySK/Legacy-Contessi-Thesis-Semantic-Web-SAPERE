package it.apice.sapere.api.ecolaws.visitor;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Product;
import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.Reactant;

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
}
