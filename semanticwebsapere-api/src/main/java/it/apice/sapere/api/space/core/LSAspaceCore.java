package it.apice.sapere.api.space.core;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.api.space.core.strategy.CustomStrategyPipeline;
import it.apice.sapere.api.space.match.MatchResult;
import it.apice.sapere.api.space.match.MatchingEcolaw;
import it.apice.sapere.api.space.observation.SpaceObserver;

/**
 * <p>
 * This inteface models a (local) LSA-space as seen by the system. It contains
 * calls that should not be exposed to a SAPEREAgent.
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
 * @param <RDFStmtIterType>
 *            The class that represents an iterator over RDF Statements
 */
public interface LSAspaceCore<RDFStmtIterType> extends LSAspace {

	/* === LSA HANDLING === */

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
	LSAspace injectCompiled(CompiledLSA<RDFStmtIterType> lsa)
			throws SAPEREException;

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
	CompiledLSA<RDFStmtIterType> readCompiled(LSAid lsaId)
			throws SAPEREException;

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
	LSAspace removeCompiled(CompiledLSA<RDFStmtIterType> lsa)
			throws SAPEREException;

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
	LSAspace updateCompiled(CompiledLSA<RDFStmtIterType> lsa)
			throws SAPEREException;

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
	 * @throws SAPEREException
	 *             Cannot complete match process
	 */
	MatchResult[] match(CompiledEcolaw law) throws SAPEREException;

	/**
	 * <p>
	 * Executes the Ecolaw, changing LSA-space current state.
	 * </p>
	 * 
	 * @param law
	 *            The law to be applied
	 * @return The updated LSA-space
	 * @throws SAPEREException
	 *             Cannot apply the eco-law
	 */
	LSAspace apply(MatchingEcolaw law) throws SAPEREException;

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

	/* === BEHAVIOUR CUSTOMIZATION === */

	/**
	 * <p>
	 * Retrieves a reference to the internal strategy pipeline.
	 * </p>
	 * <p>
	 * This pipeline should be used for extension purposes, in order to install
	 * custom components that manipulates LSA before executing primitives.
	 * </p>
	 * 
	 * @return The custom strategy pipeline
	 */
	CustomStrategyPipeline<RDFStmtIterType> getCustomStrategyPipeline();
}
