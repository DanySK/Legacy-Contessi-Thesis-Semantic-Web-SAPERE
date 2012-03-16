package it.apice.sapere.api;

import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.nodes.SAPEREAgent;

import java.net.URI;

/**
 * <p>
 * An ExtSAPEREFactory provides extra features, useful for realization of
 * internal behaviours like parsing.
 * </p>
 * <p>
 * WARNING: This interface should not be exposed to a SAPEREAgent.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface ExtSAPEREFactory extends SAPEREFactory {

	/* === LSAS === */

	/**
	 * <p>
	 * Creates a new synthetic LSA instance.
	 * </p>
	 * <p>
	 * WARNING: In order to avoid id clashes the provided LSA-id must be unique.
	 * The method <code>createLSA()</code> should be preferred!
	 * </p>
	 * 
	 * @param lsaId
	 *            The LSA-id to be used
	 * @return A fresh, custom, LSA
	 */
	LSA createLSA(LSAid lsaId);

	/**
	 * <p>
	 * Creates a new LSA instance.
	 * </p>
	 * <p>
	 * WARNING: In order to avoid id clashes the provided LSA-id must be unique.
	 * The method <code>createLSA()</code> should be preferred!
	 * </p>
	 * 
	 * @param lsaId
	 *            The LSA-id to be used
	 * @param owner
	 *            The SAPEREAgents which creates, and will own, the created LSA.
	 * @return A fresh, custom, LSA
	 */
	LSA createLSA(LSAid lsaId, SAPEREAgent owner);

	/* === LSA'S PROPERTIES === */

	/* === LSA'S PROPERTY VALUES === */

	/* === LSA-ID === */

	/**
	 * <p>
	 * Creates a new LSA-id, with a custom URI.
	 * </p>
	 * <p>
	 * WARNING: In order to avoid id clashes the provided URI must be unique.
	 * The method <code>createLSAid()</code> should be preferred!
	 * </p>
	 * 
	 * @param id
	 *            A unique id
	 * @return A fresh, custom, LSA-id
	 */
	LSAid createLSAid(URI id);

	/* === LSA'S PROPERTY NAMES === */

	/* === AGENTS === */
}
