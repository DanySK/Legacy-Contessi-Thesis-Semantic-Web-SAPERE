package it.apice.sapere.api.space;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.match.MatchingEcolaw;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.observation.LSAObserver;
import it.apice.sapere.api.space.observation.SpaceObserver;

/**
 * <p>
 * This interface models an LSA-space (local) as seen by a SAPEREAgent, so
 * capable of storing and retrieve LSAs whenever needed.
 * </p>
 * <p>
 * This entity is intended to be:
 * </p>
 * <ul>
 * <li>Atomic</li>
 * <li>Passive</li>
 * <li>Mutable</li>
 * <li>Thread-safe</li>
 * </ul>
 * 
 * @author Paolo Contessi
 * 
 */
public interface LSAspace {

	/**
	 * <p>
	 * Inserts in the space a new LSA.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA to be inserted
	 * @return The LSAspace itself for further operations
	 * @throws SAPEREException
	 *             An LSA with the same LSA-id has already been inserted
	 */
	LSAspace inject(LSA lsa) throws SAPEREException;

	/**
	 * <p>
	 * Retrieves the requested LSA.
	 * </p>
	 * 
	 * @param lsaId
	 *            The LSA-id of the requested LSA
	 * @return The LSA the caller requested
	 * @throws SAPEREException
	 *             The requested LSA does not exists in this LSA-space or
	 *             parsing errors
	 */
	LSA read(LSAid lsaId) throws SAPEREException;

	/**
	 * <p>
	 * Removes the LSA from the space.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA to be removed
	 * @return The LSAspace itself for further operations
	 * @throws SAPEREException
	 *             The LSA is not in the LSA-space
	 */
	LSAspace remove(LSA lsa) throws SAPEREException;

	/**
	 * <p>
	 * Updates the LSA in the space, which has the same LSA-id, with values from
	 * the provided one.
	 * </p>
	 * <code>
	 * LSAId lsaId = getLSAId();<br />
	 * LSA lsa = read(lsaId);<br />
	 * <br />
	 * LSA modLsa = modifyLSA(lsa);<br />
	 * assert modLsa.getLSAId().equals(lsaId);<br />
	 * s<br />
	 * update(modLsa);<br />
	 * assert modLsa.equals(read(lsaId));<br />
	 * </code>
	 * 
	 * @param lsa
	 *            The LSA to be removed
	 * @return The LSAspace itself for further operations
	 * @throws SAPEREException
	 *             The LSA is not in the LSA-space
	 */
	LSAspace update(LSA lsa) throws SAPEREException;

	/**
	 * <p>
	 * Starts observing the specified LSA.
	 * </p>
	 * 
	 * @param lsaId
	 *            The LSA-id of the LSA to be observed
	 * @param obs
	 *            The entity that will observe the LSA
	 * @return The LSAspace itself for further operations
	 */
	LSAspace observe(LSAid lsaId, LSAObserver obs);

	/**
	 * <p>
	 * Stop observing the specified LSA.
	 * </p>
	 * <p>
	 * If <code>obs</code> isn't actually observing the LSA then the request is
	 * dropped silently.
	 * </p>
	 * 
	 * @param lsaId
	 *            The id of the LSA to ignore
	 * @return The LSAspace itself for further operations
	 * @param obs
	 *            The entity that is observing the LSA
	 */
	LSAspace ignore(LSAid lsaId, LSAObserver obs);

	/**
	 * <p>
	 * Removes all LSAs.
	 * </p>
	 * 
	 * @return The LSAspace itself for further operations
	 */
	LSAspace clear();

	/* === ECOLAWS HANDLING === */

	/**
	 * <p>
	 * Finds possible matches in the LSA-space for the provided Ecolaw.
	 * </p>
	 * 
	 * @param law
	 *            The law to be checked
	 * @return Possible matches that has been found (empty list if no match)
	 */
	MatchingEcolaw[] match(Ecolaw law);

	/**
	 * <p>
	 * Executes the Ecolaw, changing LSA-space current state.
	 * </p>
	 * 
	 * @param law
	 *            The law to be applied
	 * @return The updated LSA-space
	 */
	LSAspace apply(MatchingEcolaw law);

	/* === LSA-SPACE OBSERVATION === */

	/**
	 * <p>
	 * Adds a Space Observer.
	 * </p>
	 * 
	 * @param obs
	 *            The observer
	 */
	void addSpaceObserver(SpaceObserver obs);

	/**
	 * <p>
	 * Removes a Space Observer.
	 * </p>
	 * 
	 * @param obs
	 *            The observer
	 */
	void removeSpaceObserver(SpaceObserver obs);

	/* === TRANSACTION SUPPORT === */

	/**
	 * <p>
	 * Starts a new READ Transaction.
	 * </p>
	 * <p>
	 * At the end <code>commit()</code> or <code>rollback()</code> should be
	 * called in order to release resources.
	 * </p>
	 */
	void beginRead();

	/**
	 * <p>
	 * Starts a new WRITE Transaction.
	 * </p>
	 * <p>
	 * At the end <code>commit()</code> or <code>rollback()</code> should be
	 * called in order to release resources.
	 * </p>
	 */
	void beginWrite();

	/**
	 * <p>
	 * Terminates a Transaction, stabilizing changes.
	 * </p>
	 */
	void commit();

	/**
	 * <p>
	 * Terminates a Transaction, reverting its operations.
	 * </p>
	 */
	void rollback();
}
