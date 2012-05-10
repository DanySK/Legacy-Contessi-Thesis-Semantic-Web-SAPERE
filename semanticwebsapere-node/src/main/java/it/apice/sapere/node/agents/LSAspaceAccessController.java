package it.apice.sapere.node.agents;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.api.space.observation.LSAObserver;

import java.net.URI;

/**
 * <p>
 * This class is conceived in order to analyze the requests that an agent
 * forwards and filter out those whose are not legal according to some
 * SAPEREmodel constraint.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class LSAspaceAccessController implements LSAspace {

	/** Reference to the LSA-space. */
	private final transient LSAspace space;

	/** Reference to the requestor. */
	private final transient SAPEREAgent agent;

	/**
	 * <p>
	 * Builds a new {@link LSAspaceAccessController}.
	 * </p>
	 * 
	 * @param spaceRef
	 *            Reference to the LSA-space
	 * @param anAgent
	 *            A {@link SAPEREAgent}
	 */
	public LSAspaceAccessController(final LSAspace spaceRef,
			final SAPEREAgent anAgent) {
		if (spaceRef == null) {
			throw new IllegalArgumentException(
					"Invalid LSA-space reference provided");
		}

		if (anAgent == null) {
			throw new IllegalArgumentException("Invalid SAPEREagent provided");
		}

		space = spaceRef;
		agent = anAgent;
	}

	@Override
	public final void loadOntology(final URI ontoURI) throws SAPEREException {
		space.loadOntology(ontoURI);
	}

	@Override
	public final URI[] getLoadedOntologies() {
		return space.getLoadedOntologies();
	}

	@Override
	public final LSAspace inject(final LSA lsa) throws SAPEREException {
		checkInjectPermissions(lsa, agent);
		space.inject(lsa);
		return this;
	}

	/**
	 * <p>
	 * Checks if the requestor has the right to execute the INJECT operation.
	 * </p>
	 * <p>
	 * In order to deny the permission a SAPEREException must be thrown.
	 * </p>
	 * 
	 * @param lsa
	 *            The involved LSA
	 * @param requestor
	 *            The requestor {@link SAPEREAgent}
	 * @throws SAPEREException
	 *             Operation forbidden
	 */
	public void checkInjectPermissions(final LSA lsa,
			final SAPEREAgent requestor) throws SAPEREException {

	}

	@Override
	public final LSA read(final LSAid lsaId) throws SAPEREException {
		checkReadPermissions(lsaId, agent);
		space.read(lsaId);
		return null;
	}

	/**
	 * <p>
	 * Checks if the requestor has the right to execute the READ operation.
	 * </p>
	 * <p>
	 * In order to deny the permission a SAPEREException must be thrown.
	 * </p>
	 * 
	 * @param lsaId
	 *            The involved LSA-id
	 * @param requestor
	 *            The requestor {@link SAPEREAgent}
	 * @throws SAPEREException
	 *             Operation forbidden
	 */
	public void checkReadPermissions(final LSAid lsaId,
			final SAPEREAgent requestor) throws SAPEREException {

	}

	@Override
	public final LSAspace remove(final LSA lsa) throws SAPEREException {
		checkRemovePermissions(lsa, agent);
		space.remove(lsa);
		return this;
	}

	/**
	 * <p>
	 * Checks if the requestor has the right to execute the REMOVE operation.
	 * </p>
	 * <p>
	 * In order to deny the permission a SAPEREException must be thrown.
	 * </p>
	 * 
	 * @param lsa
	 *            The involved LSA
	 * @param requestor
	 *            The requestor {@link SAPEREAgent}
	 * @throws SAPEREException
	 *             Operation forbidden
	 */
	public void checkRemovePermissions(final LSA lsa,
			final SAPEREAgent requestor) throws SAPEREException {

	}

	@Override
	public final LSAspace update(final LSA lsa) throws SAPEREException {
		checkUpdatePermissions(lsa, agent);
		space.update(lsa);
		return this;
	}

	/**
	 * <p>
	 * Checks if the requestor has the right to execute the UPDATE operation.
	 * </p>
	 * <p>
	 * In order to deny the permission a SAPEREException must be thrown.
	 * </p>
	 * 
	 * @param lsa
	 *            The involved LSA
	 * @param requestor
	 *            The requestor {@link SAPEREAgent}
	 * @throws SAPEREException
	 *             Operation forbidden
	 */
	public void checkUpdatePermissions(final LSA lsa,
			final SAPEREAgent requestor) throws SAPEREException {

	}

	@Override
	public final LSAspace observe(final LSAid lsaId, final LSAObserver obs)
			throws SAPEREException {
		checkObservePermissions(lsaId, agent);
		space.observe(lsaId, obs);
		return this;
	}

	/**
	 * <p>
	 * Checks if the requestor has the right to execute the OBSERVE operation.
	 * </p>
	 * <p>
	 * In order to deny the permission a SAPEREException must be thrown.
	 * </p>
	 * 
	 * @param lsaId
	 *            The involved LSA-id
	 * @param requestor
	 *            The requestor {@link SAPEREAgent}
	 * @throws SAPEREException
	 *             Operation forbidden
	 */
	public void checkObservePermissions(final LSAid lsaId,
			final SAPEREAgent requestor) throws SAPEREException {

	}

	@Override
	public final LSAspace ignore(final LSAid lsaId, final LSAObserver obs) {
		space.ignore(lsaId, obs);
		return this;
	}

	@Override
	public final void beginRead() {
		space.beginRead();
	}

	@Override
	public final void beginWrite() {
		space.beginWrite();
	}

	@Override
	public final void done() {
		space.done();
	}

}
