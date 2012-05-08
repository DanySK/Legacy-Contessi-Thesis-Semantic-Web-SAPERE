package it.apice.sapere.api.space.core.strategy;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.core.CompiledLSA;

/**
 * <p>
 * Basic interface to be implemented with a strategy to be applied in the
 * pipeline.
 * </p>
 * <p>
 * Each step should take an LSA(-id) from the previous step and return an
 * (eventually) modified version of it to be provided to the following step.
 * 
 * @author Paolo Contessi
 * 
 * @param <T>
 *            RDF Statements iterator used by {@link CompiledLSA}
 */
public interface CustomStrategyPipelineStep<T> {

	/**
	 * <p>
	 * Handles provided LSA to be injected and applies the implemented strategy
	 * before returning it.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA to be checked against strategy constraints before
	 *            injection
	 * @return The eventually modified LSA to be injected
	 * @throws SAPEREException
	 *             Operation aborted due to strategy
	 */
	CompiledLSA<T> handleInject(CompiledLSA<T> lsa) throws SAPEREException;

	/**
	 * <p>
	 * Handles provided LSA-id and applies the implemented strategy before
	 * returning it.
	 * </p>
	 * 
	 * @param lsaId
	 *            The LSA-id to be checked against strategy constraints before
	 *            proceeding with LSA retrieval
	 * @return The eventually modified LSAid to be read
	 * @throws SAPEREException
	 *             Operation aborted due to strategy
	 */
	LSAid handleRead(LSAid lsaId) throws SAPEREException;

	/**
	 * <p>
	 * Handles provided LSA to be removed and applies the implemented strategy
	 * before returning.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA to be checked against strategy constraints before
	 *            removal
	 * @return The eventually modified LSA to be removed
	 * @throws SAPEREException
	 *             Operation aborted due to strategy
	 */
	CompiledLSA<T> handleRemove(CompiledLSA<T> lsa) throws SAPEREException;

	/**
	 * <p>
	 * Handles provided LSA to be updated and applies the implemented strategy
	 * before returning it.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA to be checked against strategy constraints before
	 *            modification
	 * @return The eventually modified LSA to be updated
	 * @throws SAPEREException
	 *             Operation aborted due to strategy
	 */
	CompiledLSA<T> handleUpdate(CompiledLSA<T> lsa) throws SAPEREException;
}
