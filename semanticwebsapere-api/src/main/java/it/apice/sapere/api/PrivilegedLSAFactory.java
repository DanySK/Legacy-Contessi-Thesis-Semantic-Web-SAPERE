package it.apice.sapere.api;

import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;

import java.net.URI;

/**
 * <p>
 * A PrivilegedLSAFactory is an extension of the LSAFactory, which provides
 * additional facilities useful for background management.
 * </p>
 * <p>
 * This interface represents what an LSAParser will require in order to perform
 * its task.
 * <p>
 * WARNING: This interface should not be exposed to a SAPEREAgent because it
 * relaxes some constraint that foundational for eco-system correctness.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface PrivilegedLSAFactory extends LSAFactory {

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

//	/* === AGENTS === */
}
