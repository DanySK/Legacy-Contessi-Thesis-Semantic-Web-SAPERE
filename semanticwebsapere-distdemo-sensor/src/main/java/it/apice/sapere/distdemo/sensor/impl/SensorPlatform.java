package it.apice.sapere.distdemo.sensor.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.management.ReactionManager;
import it.apice.sapere.api.node.agents.SAPEREAgentsFactory;
import it.apice.sapere.api.space.core.EcolawCompiler;
import it.apice.sapere.commons.DiffusionEcolaw;

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
		_manager.addEcolaw(DiffusionEcolaw.createASAPDiffusion(_compiler,
				"analysis_node", "sensing:Observation"));
	}

}
