package it.apice.sapere.distdemo.sensor.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.management.ReactionManager;
import it.apice.sapere.api.node.agents.SAPEREAgentsFactory;
import it.apice.sapere.api.space.core.EcolawCompiler;

/**
 * <p>
 * Sensor Platform business logic.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class SensorPlatform {

	/** Reference to reaction manager. */
	private final transient ReactionManager _manager;

	/** Reference to Agents Factory. */
	private final transient SAPEREAgentsFactory _factory;

	/** Reference to Eco-law Compiler. */
	private final transient EcolawCompiler _compiler;

	/**
	 * <p>
	 * Builds a new {@link SensorPlatform}.
	 * </p>
	 * 
	 * @param manager
	 *            Reference to reaction manager
	 * @param aFactory
	 *            Reference to Agents Factory
	 * @param eCompiler
	 *            Reference to Eco-law Compiler
	 */
	public SensorPlatform(final ReactionManager manager,
			final SAPEREAgentsFactory aFactory, 
			final EcolawCompiler eCompiler) {
		if (manager == null) {
			throw new IllegalArgumentException("Invalid manager provided");
		}

		if (aFactory == null) {
			throw new IllegalArgumentException(
					"Invalid agents factory provided");
		}

		if (eCompiler == null) {
			throw new IllegalArgumentException(
					"Invalid eco-law compiler provided");
		}

		_manager = manager;
		_factory = aFactory;
		_compiler = eCompiler;
	}

	/**
	 * <p>
	 * Runs the business logic.
	 * </p>
	 * 
	 * @throws SAPEREException
	 *             Cannot run demo
	 */
	public void execute() throws SAPEREException {
		setEcolaws();
		runAgents();
	}

	/**
	 * <p>
	 * Configures agents.
	 * </p>
	 * 
	 * @throws SAPEREException
	 *             Cannot spawn Agents
	 */
	private void runAgents() throws SAPEREException {
		_factory.createAgent("temp_sensor", new TempSensor()).spawn();
	}

	/**
	 * <p>
	 * Configures eco-laws.
	 * </p>
	 */
	private void setEcolaws() {
		_manager.addEcolaw(_compiler.create(getMatchQuery(), getUpdateQuery(),
				getRate()));
	}

	/**
	 * <p>
	 * Retrieves Match Query.
	 * </p>
	 * 
	 * @return SPARQL Query
	 */
	private String getMatchQuery() {
		return "PREFIX sapere: <http://www.sapere-project.eu/"
				+ "ontologies/2012/0/sapere-model.owl#> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX sensing: <http://www.sapere-project.eu/distdemo#> "
				+ "SELECT DISTINCT * WHERE { "
				+ "?observ rdf:type sensing:Observation; "
				+ "sapere:location sapere:local. "
				+ "?newloc sapere:type sapere:neighbour; "
				+ "sapere:name \"analysis_node\" }";
	}

	/**
	 * <p>
	 * Retrieves Update Query.
	 * </p>
	 * 
	 * @return SPARQL/Update Query
	 */
	private String getUpdateQuery() {
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
	private String getRate() {
		return "" + Double.MAX_VALUE;
	}
}
