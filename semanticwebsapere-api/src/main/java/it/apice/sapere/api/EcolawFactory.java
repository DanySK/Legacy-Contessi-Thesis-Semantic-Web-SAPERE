package it.apice.sapere.api;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.Product;
import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.Reactant;

/**
 * <p>
 * An EcolawFactory provides basic functionalities that enables the creation of
 * Ecolaws according to the SAPERE model.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface EcolawFactory {

	/**
	 * <p>
	 * Creates a new Ecolaw.
	 * </p>
	 * 
	 * @return A fresh Ecolaw
	 */
	Ecolaw createEcolaw();

	/**
	 * <p>
	 * Creates a new Ecolaw.
	 * </p>
	 * 
	 * @param label
	 *            The label of the ecolaw
	 * @return A fresh Ecolaw
	 */
	Ecolaw createEcolaw(String label);

	/**
	 * <p>
	 * Creates a new Reactant.
	 * </p>
	 * 
	 * @param name
	 *            The name of the pattern
	 * @param filters
	 *            List of filters to be applied
	 * @return A fresh Reactant
	 */
	Reactant createReactant(String name, Filter... filters);

	/**
	 * <p>
	 * Creates a new Product.
	 * </p>
	 * 
	 * @param name
	 *            The name of the pattern
	 * @param filters
	 *            List of filters to be applied
	 * @return A fresh Product
	 */
	Product createProduct(String name, Filter... filters);

	Rate<?> createMarkovianRate(double rate);
	
	Rate<?> createASAPRate();
}
