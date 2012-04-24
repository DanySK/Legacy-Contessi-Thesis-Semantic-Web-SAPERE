package it.apice.sapere.api.ecolaws.match;

import it.apice.sapere.api.space.core.LSAspaceCore;

/**
 * <p>
 * This interface models an entity which will contain all assignments made
 * during matching phase.
 * 
 * @author Paolo Contessi
 * 
 */
public interface MatchResult {

	/**
	 * <p>
	 * Retrieves the value of the variable whose name has been provided.
	 * </p>
	 * 
	 * @param varName
	 *            The name of the variable
	 * @return The assigned value
	 */
	String lookup(String varName);

	/**
	 * <p>
	 * Retrieves the score of a specific assignment.
	 * </p>
	 * 
	 * @param varName
	 *            The name of the variable whose assigment score should be
	 *            retrieved
	 * @return The score of the assignment
	 */
	Double getAssignmentScore(String varName);

	/**
	 * <p>
	 * Retrieves a list of all variables names that has been stored (with
	 * relative bindings) in this {@link MatchResult}.
	 * </p>
	 * 
	 * @return A list of all variables names
	 */
	String[] getAllMatchedVariablesNames();

	/**
	 * <p>
	 * Retrieves a reference to the LSAspace that computed the match.
	 * </p>
	 * 
	 * @return A reference to the LSA-space
	 */
	LSAspaceCore<?> getLSAspace();

}
