package it.apice.sapere.api.ecolaws.match;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.terms.VarTerm;
import it.apice.sapere.api.space.core.LSAspaceCore;

import java.util.Map.Entry;

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
	 * Asks what term has been assigned to a specific value.
	 * </p>
	 * <p>
	 * This method does not modify the variable; in order to create the binding
	 * <code>assign(var)</code> should be used.
	 * </p>
	 * 
	 * @param var
	 *            The variable to be assigned
	 * @return The value to be assigned (as a Term)
	 */
	Term<?> lookup(VarTerm<?> var);

	/**
	 * <p>
	 * Retrieves the list of all the scores assigned to each assignment.
	 * </p>
	 * 
	 * @return All scores of each assignment
	 */
	Double[] getAssignmentScores();

	/**
	 * <p>
	 * Retrieves the whole list of assignments.
	 * </p>
	 * 
	 * @return A list of couples <code>&lt;VarName, AssignedValue&gt;</code>
	 */
	Entry<String, Term<?>>[] entries();

	/**
	 * <p>
	 * Binds the variable to the matching term.
	 * </p>
	 * 
	 * @param var
	 *            The variable to be assigned
	 * @throws SAPEREException
	 *             Cannot complete assignment
	 */
	void assign(VarTerm<?> var) throws SAPEREException;

	/**
	 * <p>
	 * Retrieves a reference to the Ecolaw which the bindings are relative to.
	 * </p>
	 * 
	 * @return An Ecolaw
	 */
	Ecolaw getRelativeEcolaw();

	/**
	 * <p>
	 * Retrieves a reference to the LSAspace that computed the match.
	 * </p>
	 * 
	 * @return A reference to the LSA-space
	 */
	LSAspaceCore getLSAspace();

}
