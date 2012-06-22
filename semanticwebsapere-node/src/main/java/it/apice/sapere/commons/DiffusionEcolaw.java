package it.apice.sapere.commons;

import it.apice.sapere.api.space.core.CompiledEcolaw;
import it.apice.sapere.api.space.core.EcolawCompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.management.impl.DiffusionHandler;

/**
 * <p>
 * Utility class which provides pre-compiled diffusion eco-law and relatives.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public final class DiffusionEcolaw {

	/**
	 * <p>
	 * Hidden constructor.
	 * </p>
	 */
	private DiffusionEcolaw() {

	}

	/**
	 * <p>
	 * Retrieves an periodic diffusion eco-law.
	 * </p>
	 * 
	 * @param compiler
	 *            Reference to {@link EcolawCompiler}
	 * @param toName
	 *            Name of the node to which diffuse
	 * @param type
	 *            LSA's rdf:type to be diffused
	 * @param rate
	 *            Scheduling rate
	 * @return The eco-law, compiled
	 */
	public static CompiledEcolaw createPeriodicDiffusion(
			final EcolawCompiler compiler, final String toName,
			final String type, final double rate) {
		return compiler.create(getASAPMatchQuery(type, toName),
				getASAPUpdateQuery(), "" + rate);
	}

	/**
	 * <p>
	 * Retrieves an ASAP diffusion eco-law.
	 * </p>
	 * 
	 * @param compiler
	 *            Reference to {@link EcolawCompiler}
	 * @param toName
	 *            Name of the node to which diffuse
	 * @param type
	 *            LSA's rdf:type to be diffused
	 * @return The eco-law, compiled
	 */
	public static CompiledEcolaw createASAPDiffusion(
			final EcolawCompiler compiler, final String toName,
			final String type) {
		return compiler.create(getASAPMatchQuery(type, toName),
				getASAPUpdateQuery(), getASAPRate());
	}

	/**
	 * <p>
	 * Retrieves Match Query.
	 * </p>
	 * 
	 * @param type
	 *            LSA's rdf:type to be diffused
	 * @param toName
	 *            Name of the node to which diffuse
	 * @return SPARQL Query
	 */
	private static String getASAPMatchQuery(final String type,
			final String toName) {
		return "PREFIX sapere: <http://www.sapere-project.eu/"
				+ "ontologies/2012/0/sapere-model.owl#> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX sensing: <http://www.sapere-project.eu/distdemo#> "
				+ "SELECT DISTINCT * WHERE { " + "?observ rdf:type " + type
				+ "; " + "sapere:location sapere:local. "
				+ "?newloc sapere:type sapere:neighbour; " + "sapere:name \""
				+ toName + "\" }";
	}

	/**
	 * <p>
	 * Retrieves Update Query.
	 * </p>
	 * 
	 * @return SPARQL/Update Query
	 */
	private static String getASAPUpdateQuery() {
		return "PREFIX sapere: <http://www.sapere-project.eu/"
				+ "ontologies/2012/0/sapere-model.owl#> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX sensing: <http://www.sapere-project.eu/distdemo#> "
				+ "MODIFY DELETE { !observ sapere:location sapere:local. } "
				+ "INSERT { !observ sapere:location !newloc } WHERE { "
				+ "?observ rdf:type sensing:Observation; "
				+ "sapere:location sapere:local. }";
	}

	/**
	 * <p>
	 * Retrieves Query Rate.
	 * </p>
	 * 
	 * @return Scheduling Rate
	 */
	private static String getASAPRate() {
		return "" + Double.MAX_VALUE;
	}

	/**
	 * <p>
	 * Retrieves an ASAP {@link DiffusionHandler}.
	 * </p>
	 * 
	 * @param lsaSpace
	 *            Reference to LSA-space
	 * @param nodeId
	 *            Node identifier
	 * @return An ASAP {@link DiffusionHandler}
	 */
	public static DiffusionHandler getDiffusionHandler(
			final LSAspaceCore<?> lsaSpace, final String nodeId) {
		return new DiffusionHandler(lsaSpace, nodeId, false);
	}
}
