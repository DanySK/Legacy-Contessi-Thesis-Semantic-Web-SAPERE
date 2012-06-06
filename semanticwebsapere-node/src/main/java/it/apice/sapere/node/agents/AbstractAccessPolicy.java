package it.apice.sapere.node.agents;

import it.apice.api.node.logging.impl.LoggerFactoryImpl;
import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.node.agents.LSAspaceAccessPolicy;
import it.apice.sapere.api.node.agents.SAPEREAgent;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.api.space.core.CompiledLSA;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.observation.LSAObserver;

import java.net.URI;
import java.util.Set;

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
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class AbstractAccessPolicy implements LSAspaceAccessPolicy {

	/** Reference to the LSA-space. */
	private final transient LSAspaceCore/* <StmtIterator> */space;

	/** Reference to the requestor. */
	private transient SAPEREAgent agent;

	/** Reference to a LSA Compiler. */
	private final transient LSACompiler/* <StmtIterator> */compiler;

	/** Reference to a LSA Parser. */
	private final transient LSAParser parser;

	/**
	 * <p>
	 * Builds a new {@link AbstractAccessPolicy}.
	 * </p>
	 * 
	 * @param spaceRef
	 *            Reference to the LSA-space
	 * @param lsaCompiler
	 *            Reference to a LSA Compiler
	 * @param lsaParser
	 *            Reference to a LSA Parser
	 */
	public AbstractAccessPolicy(final LSAspaceCore/* <StmtIterator> */spaceRef,
			final LSACompiler/* <StmtIterator> */lsaCompiler,
			final LSAParser lsaParser) {
		if (spaceRef == null) {
			throw new IllegalArgumentException(
					"Invalid LSA-space reference provided");
		}

		if (lsaCompiler == null) {
			throw new IllegalArgumentException("Invalid LSA Compiler provided");
		}

		if (lsaParser == null) {
			throw new IllegalArgumentException("Invalid LSA Parser provided");
		}

		space = spaceRef;
		compiler = lsaCompiler;
		parser = lsaParser;
	}

	@Override
	public final void setRequestor(final SAPEREAgent anAgent) {
		if (anAgent == null) {
			throw new IllegalArgumentException("Invalid SAPEREagent provided");
		}

		if (agent != null) {
			throw new IllegalStateException("Must not change requestor");
		}

		agent = anAgent;
	}

	@Override
	public final void loadOntology(final URI ontoURI) throws SAPEREException {
		space.beginWrite();
		try {
			space.loadOntology(ontoURI);
			LoggerFactoryImpl
					.getInstance()
					.getLogger(AbstractAccessPolicy.class)
					.spy(String.format("%s loads ontology (%s)",
							agent.getLocalAgentId(), ontoURI));
		} finally {
			space.done();
		}
	}

	@Override
	public final URI[] getLoadedOntologies() {
		space.beginRead();
		try {
			return space.getLoadedOntologies();
		} finally {
			space.done();
		}
	}

	@Override
	public final LSAspace inject(final LSA lsa) throws SAPEREException {
		LoggerFactoryImpl
				.getInstance()
				.getLogger(AbstractAccessPolicy.class)
				.spy(String.format("%s requests INJECT",
						agent.getLocalAgentId()));

		final CompiledLSA cLsa = compiler.compile(lsa);
		checkInjectPermissions(cLsa, agent);

		space.beginWrite();
		try {
			space.inject(cLsa);
		} finally {
			space.done();
		}

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
	 * @param cLsa
	 *            The involved LSA
	 * @param requestor
	 *            The requestor {@link SAPEREAgent}
	 * @throws SAPEREException
	 *             Operation forbidden
	 */
	public abstract void checkInjectPermissions(final CompiledLSA cLsa,
			final SAPEREAgent requestor) throws SAPEREException;

	@Override
	public final LSA read(final LSAid lsaId) throws SAPEREException {
		LoggerFactoryImpl
				.getInstance()
				.getLogger(AbstractAccessPolicy.class)
				.spy(String.format("%s requests READ", 
						agent.getLocalAgentId()));
		checkReadPermissions(lsaId, agent);

		space.beginRead();
		try {
			final Set<LSA> res = parser.parseLSAs(
					space.read(lsaId).toString(RDFFormat.RDF_XML),
					RDFFormat.RDF_XML);
			assert res.size() == 1;
			return res.iterator().next();
		} finally {
			space.done();
		}
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
	public abstract void checkReadPermissions(final LSAid lsaId,
			final SAPEREAgent requestor) throws SAPEREException;

	@Override
	public final LSAspace remove(final LSA lsa) throws SAPEREException {
		LoggerFactoryImpl
				.getInstance()
				.getLogger(AbstractAccessPolicy.class)
				.spy(String.format("%s requests REMOVE",
						agent.getLocalAgentId()));

		final CompiledLSA cLsa = compiler.compile(lsa);
		checkRemovePermissions(cLsa, agent);

		space.beginWrite();
		try {
			space.remove(cLsa);
		} finally {
			space.done();
		}

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
	 * @param cLsa
	 *            The involved LSA
	 * @param requestor
	 *            The requestor {@link SAPEREAgent}
	 * @throws SAPEREException
	 *             Operation forbidden
	 */
	public abstract void checkRemovePermissions(final CompiledLSA cLsa,
			final SAPEREAgent requestor) throws SAPEREException;

	@Override
	public final LSAspace update(final LSA lsa) throws SAPEREException {
		LoggerFactoryImpl
				.getInstance()
				.getLogger(AbstractAccessPolicy.class)
				.spy(String.format("%s requests UPDATE",
						agent.getLocalAgentId()));

		final CompiledLSA cLsa = compiler.compile(lsa);
		checkUpdatePermissions(cLsa, agent);

		space.beginWrite();
		try {
			space.update(cLsa);
		} finally {
			space.done();
		}

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
	 * @param cLsa
	 *            The involved LSA
	 * @param requestor
	 *            The requestor {@link SAPEREAgent}
	 * @throws SAPEREException
	 *             Operation forbidden
	 */
	public abstract void checkUpdatePermissions(final CompiledLSA cLsa,
			final SAPEREAgent requestor) throws SAPEREException;

	@Override
	public final LSAspace observe(final LSAid lsaId, final LSAObserver obs)
			throws SAPEREException {
		LoggerFactoryImpl
				.getInstance()
				.getLogger(AbstractAccessPolicy.class)
				.spy(String.format("%s requests OBSERVE",
						agent.getLocalAgentId()));

		checkObservePermissions(lsaId, agent);
		space.beginWrite();
		try {
			space.observe(lsaId, obs);
		} finally {
			space.done();
		}

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
	public abstract void checkObservePermissions(final LSAid lsaId,
			final SAPEREAgent requestor) throws SAPEREException;

	@Override
	public final LSAspace ignore(final LSAid lsaId, final LSAObserver obs) {
		LoggerFactoryImpl
				.getInstance()
				.getLogger(AbstractAccessPolicy.class)
				.spy(String.format("%s requests IGNORE",
						agent.getLocalAgentId()));

		space.beginWrite();
		try {
			space.ignore(lsaId, obs);
		} finally {
			space.done();
		}

		return this;
	}

	@Override
	public final void beginRead() {
		LoggerFactoryImpl
				.getInstance()
				.getLogger(AbstractAccessPolicy.class)
				.spy(String.format("%s requests begin transaction (READ)",
						agent.getLocalAgentId()));

		space.beginRead();

		LoggerFactoryImpl
				.getInstance()
				.getLogger(AbstractAccessPolicy.class)
				.spy(String.format("%s begin transaction (READ)",
						agent.getLocalAgentId()));
	}

	@Override
	public final void beginWrite() {
		LoggerFactoryImpl
				.getInstance()
				.getLogger(AbstractAccessPolicy.class)
				.spy(String.format("%s requests begin transaction (WRITE)",
						agent.getLocalAgentId()));

		space.beginWrite();

		LoggerFactoryImpl
				.getInstance()
				.getLogger(AbstractAccessPolicy.class)
				.spy(String.format("%s begin transaction (WRITE)",
						agent.getLocalAgentId()));
	}

	@Override
	public final void done() {
		LoggerFactoryImpl
		.getInstance()
		.getLogger(AbstractAccessPolicy.class)
		.spy(String.format("%s is completing transaction",
				agent.getLocalAgentId()));

		space.done();

		LoggerFactoryImpl
				.getInstance()
				.getLogger(AbstractAccessPolicy.class)
				.spy(String.format("%s completed transaction",
						agent.getLocalAgentId()));
	}

}
