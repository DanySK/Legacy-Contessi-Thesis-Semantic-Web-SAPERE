package it.apice.sapere.api.ecolaws;


/**
 * <p>
 * This interface represents an Ecolaw that is applied in the SAPERE ecosystem.
 * </p>
 * <p>
 * This interface allows the modification of the Ecolaw.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface MutableEcolaw extends Ecolaw {

	/**
	 * <p>
	 * Modifies the rate of this Ecolaw.
	 * </p>
	 * 
	 * @param rate
	 *            The new scheduling rate
	 * @return The updated ecolaw
	 */
	MutableEcolaw setRate(Rate<?> rate);

	/**
	 * <p>
	 * Adds a new Reactant.
	 * </p>
	 * 
	 * @param react
	 *            The reactant
	 * @return The updated ecolaw
	 */
	MutableEcolaw addReactant(Reactant react);

	/**
	 * <p>
	 * Removes a Reactant.
	 * </p>
	 * 
	 * @param react
	 *            The reactant
	 * @return The updated ecolaw
	 */
	MutableEcolaw removeReactant(Reactant react);

	/**
	 * <p>
	 * Adds a new Product.
	 * </p>
	 * 
	 * @param prod
	 *            The product
	 * @return The updated ecolaw
	 */
	MutableEcolaw addProduct(Product prod);

	/**
	 * <p>
	 * Removes a Product.
	 * </p>
	 * 
	 * @param prod
	 *            The product
	 * @return The updated ecolaw
	 */
	MutableEcolaw removeProduct(Product prod);
}
