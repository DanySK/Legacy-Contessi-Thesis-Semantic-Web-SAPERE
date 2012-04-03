package it.apice.sapere.api.ecolaws;

import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

/**
 * This interface represents the rate at which a reaction is triggered.
 * 
 * @author Marco Santarelli
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            Internal type used to represent the rate
 */
public interface Rate<Type> {

	/**
	 * Computes the next occurrence of the reaction given the current time and
	 * the score of the match between template and LSAs. Reactions with higher
	 * scores are triggered first.
	 * 
	 * @param score
	 *            matching scores of the reaction
	 * @param currentTime
	 *            current time in milliseconds
	 * @return the next activation time of the reaction in milliseconds
	 */
	long getNextOccurrence(double score, long currentTime);

	/**
	 * <p>
	 * Retrieves the rate value.
	 * </p>
	 * 
	 * @return The rate value
	 */
	Type getRateValue();

	/**
	 * <p>
	 * Accepts a Visitor.
	 * </p>
	 * 
	 * @param visitor
	 *            Ecolaw visitor
	 */
	void accept(EcolawVisitor visitor);
}
