package it.apice.sapere.api.ecolaws;

import it.apice.sapere.api.ecolaws.match.MatchingEcolaw;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

/**
 * <p>
 * This interface represents an Ecolaw that is applied in the SAPERE ecosystem.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface Ecolaw {

	/**
	 * <p>
	 * Retrieves a label which identifies the Ecolaw.
	 * </p>
	 * <p>
	 * This label should be a shortcut for the user.
	 * </p>
	 * 
	 * @return The ecolaw's label
	 */
	String getLabel();

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
	 * Retrieves the rate of this Ecolaw.
	 * </p>
	 * 
	 * @return Scheduling rate
	 */
	Rate<?> getRate();

	/**
	 * <p>
	 * Retrieves all reactants.
	 * </p>
	 * 
	 * @return List of reactants
	 */
	Reactant[] reactants();

	/**
	 * <p>
	 * Retrieves all products.
	 * </p>
	 * 
	 * @return List of products
	 */
	Product[] products();

	/**
	 * <p>
	 * Modifies the rate of this Ecolaw.
	 * </p>
	 * 
	 * @param rate
	 *            The new scheduling rate
	 * @return The updated ecolaw
	 */
	Ecolaw rate(Rate<?> rate);

	/**
	 * <p>
	 * Adds a new Reactant.
	 * </p>
	 * 
	 * @param react
	 *            The reactant
	 * @return The updated ecolaw
	 */
	Ecolaw reactant(Reactant react);

	/**
	 * <p>
	 * Adds a new Product.
	 * </p>
	 * 
	 * @param prod
	 *            The product
	 * @return The updated ecolaw
	 */
	Ecolaw product(Product prod);

	/**
	 * <p>
	 * Binds Ecolaw's variables to matching values found in LSA-space.
	 * </p>
	 * 
	 * @return The Matching Ecolaw
	 */
	MatchingEcolaw bind(); // TODO find how to pass bindings
}
