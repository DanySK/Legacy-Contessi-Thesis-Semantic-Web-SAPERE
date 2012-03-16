package it.apice.sapere.api.space;

import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.nodes.SAPEREException;
import it.apice.sapere.api.space.observation.LSAObserver;
import it.apice.sapere.api.space.observation.SpaceObserver;

/**
 * <p>
 * This interface models an LSA-space (local), providing each feature declared
 * in LSAspace, plus some special methods useful for background handling.
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
public interface ExtLSAspace extends LSAspace {

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
	 *            The entity that will observe the LSA (an Agent)
	 * @return The LSAspace itself for further operations
	 * @throws SAPEREException
	 *             The requester is not allowed to observe the desired LSA (not
	 *             bound or not not local)
	 */
	LSAspace observe(LSAid lsaId, LSAObserver obs)
			throws SAPEREException;

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
	 *            The entity that is observing the LSA (an Agent)
	 */
	LSAspace ignore(LSAid lsaId, LSAObserver obs);

	/* === LSA-SPACE OBSERVER (for logging purposes and so on) === */

	/**
	 * <p>
	 * Registers an LSA-space Observer, useful to inspect actual space status.
	 * </p>
	 * 
	 * @param obs
	 *            The observer to be registered
	 */
	void addSpaceObserver(SpaceObserver obs);

	/**
	 * <p>
	 * Unregisters an LSA-space Observer.
	 * </p>
	 * 
	 * @param obs
	 *            The observer to be unregistered
	 */
	void removeSpaceObserver(SpaceObserver obs);
}
