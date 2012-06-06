package it.apice.sapere.node.agents.impl;

import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.node.agents.SAPEREAgent;
import it.apice.sapere.api.space.core.CompiledLSA;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.node.agents.AbstractAccessPolicy;

import java.net.URI;

/**
 * <p>
 * This class provides a default LSA-space access policy.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
@SuppressWarnings("rawtypes")
public class DefaultAccessPolicy extends AbstractAccessPolicy {

	/** SAPERE namespace. */
	private static final transient String SAPERE_NS = "http://"
			+ "www.sapere-project.eu/ontologies/2012/0/sapere-model.owl#";

	/** Creator ID property URI. */
	private static final transient URI CREATOR_PROP = URI.create(SAPERE_NS
			+ "creatorId");

	/**
	 * <p>
	 * Builds a new {@link DefaultAccessPolicy}.
	 * </p>
	 * 
	 * @param spaceRef
	 *            Reference to the LSA-space
	 * @param lsaCompiler
	 *            Reference to an LSA Compiler
	 * @param lsaParser
	 *            Reference to a LSA Parser
	 */
	public DefaultAccessPolicy(final LSAspaceCore/* <StmtIterator> */spaceRef,
			final LSACompiler/* <StmtIterator> */lsaCompiler,
			final LSAParser lsaParser) {
		super(spaceRef, lsaCompiler, lsaParser);
	}

	@Override
	public final void checkInjectPermissions(final CompiledLSA lsa,
			final SAPEREAgent requestor) throws SAPEREException {
		if (requestor instanceof UserAgentImpl) {
			lsa.clearProperty(CREATOR_PROP);
			lsa.assertProperty(CREATOR_PROP, requestor.getAgentURI());
		}
	}

	@Override
	public final void checkReadPermissions(final LSAid lsaId,
			final SAPEREAgent requestor) throws SAPEREException {
		if (requestor instanceof UserAgentImpl) {
			return;
		}
	}

	@Override
	public final void checkRemovePermissions(final CompiledLSA lsa,
			final SAPEREAgent requestor) throws SAPEREException {
		if (requestor instanceof UserAgentImpl) {
			checkIfIsOwner(lsa, requestor);
		}
	}

	@Override
	public final void checkUpdatePermissions(final CompiledLSA lsa,
			final SAPEREAgent requestor) throws SAPEREException {
		if (requestor instanceof UserAgentImpl) {
			checkIfIsOwner(lsa, requestor);
		}
	}

	@Override
	public final void checkObservePermissions(final LSAid lsaId,
			final SAPEREAgent requestor) throws SAPEREException {
		if (requestor instanceof UserAgentImpl) {
			return;
		}
	}

	/**
	 * <p>
	 * Checks if the requestor is also the owner of the LSA, otherwise aborts.
	 * </p>
	 * 
	 * @param lsa
	 *            The involved LSA
	 * @param requestor
	 *            The requestor
	 * @throws SAPEREException
	 *             Operation not allowed
	 */
	private void checkIfIsOwner(final CompiledLSA lsa,
			final SAPEREAgent requestor) throws SAPEREException {
		if (!lsa.readURIProperty(CREATOR_PROP)[0].equals(requestor
				.getAgentURI())) {
			throw new SAPEREException("Operation not allowed");
		}
	}
}
