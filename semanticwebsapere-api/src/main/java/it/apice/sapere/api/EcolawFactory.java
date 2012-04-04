package it.apice.sapere.api;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Product;
import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.Reactant;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.lsas.values.PropertyValue;

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

	/* === ECOLAWS === */

	/**
	 * <p>
	 * Creates a new Ecolaw with ASAP Rate.
	 * </p>
	 * 
	 * @return A fresh Ecolaw
	 */
	Ecolaw createEcolaw();

	/**
	 * <p>
	 * Creates a new Ecolaw with ASAP Rate.
	 * </p>
	 * 
	 * @param label
	 *            The label of the ecolaw
	 * @return A fresh Ecolaw
	 */
	Ecolaw createEcolaw(String label);

	/* === CHEMICAL PATTERNS === */

	/**
	 * <p>
	 * Creates a new Reactant.
	 * </p>
	 * 
	 * @param name
	 *            The name of the pattern
	 * @return A fresh Reactant
	 */
	Reactant createReactant(String name);

	/**
	 * <p>
	 * Creates a new Product.
	 * </p>
	 * 
	 * @param name
	 *            The name of the pattern
	 * @return A fresh Product
	 */
	Product createProduct(String name);

	/* === RATES === */

	/**
	 * <p>
	 * Creates a new Ecolaw scheduling rate which asserts that the law should be
	 * scheduled according to Continuous-time Markov Chains (CTMC).
	 * </p>
	 * 
	 * @param rate
	 *            The actual rate
	 * @return A Markovian Rate
	 */
	Rate<?> createMarkovianRate(double rate);

	/**
	 * <p>
	 * Creates a new Ecolaw scheduling rate which asserts that the law should be
	 * scheduled "As Soon As Possible".
	 * </p>
	 * 
	 * @return An ASAP Rate
	 */
	Rate<?> createASAPRate();

	/* === TERMS === */

	Term createValueTerm(PropertyValue<?> value);
}
