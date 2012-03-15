package it.apice.sapere.api.space;

import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.nodes.SAPEREException;

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
	 * @throws SAPEREException
	 *             An LSA with the same LSA-id has already been inserted
	 */
	void inject(LSA lsa) throws SAPEREException;

	/**
	 * <p>
	 * Retrieves the requested LSA.
	 * </p>
	 * 
	 * @param lsaId
	 *            The LSA-id of the requested LSA
	 * @return The LSA the caller requested
	 * @throws SAPEREException
	 *             The requested LSA does not exists in this LSA-space
	 */
	LSA read(LSAid lsaId) throws SAPEREException;

	/**
	 * <p>
	 * Removes the LSA from the space.
	 * </p>
	 * 
	 * @param lsaId
	 *            The LSA-id of the LSA to be removed
	 * @throws SAPEREException
	 *             The requester is not allowed to remove the LSA (e.g. it does
	 *             not owns it)
	 */
	void remove(LSAid lsaId) throws SAPEREException;

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
	 * @throws SAPEREException
	 *             The requester is not allowed to update the LSA (e.g. it does
	 *             not owns it)
	 */
	void update(LSA lsa) throws SAPEREException;

	/**
	 * <p>
	 * Starts observing the specified LSA.
	 * </p>
	 * 
	 * @param lsaId
	 *            The LSA-id of the LSA to be observed
	 * @throws SAPEREException
	 *             The requester is not allowed to observe the desired LSA (not
	 *             bound or not not local)
	 */
	void observe(LSAid lsaId) throws SAPEREException;

	/**
	 * <p>
	 * Stop observing the specified LSA.
	 * </p>
	 * 
	 * @param lsaId
	 *            The id of the LSA to ignore
	 */
	void ignore(LSAid lsaId);
}
