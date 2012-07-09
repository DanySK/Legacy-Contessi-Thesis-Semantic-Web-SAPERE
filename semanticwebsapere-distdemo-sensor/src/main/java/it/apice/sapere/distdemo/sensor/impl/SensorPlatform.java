package it.apice.sapere.distdemo.sensor.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.management.ReactionManager;
import it.apice.sapere.api.node.agents.SAPEREAgentsFactory;
import it.apice.sapere.api.space.core.EcolawCompiler;
import it.apice.sapere.distdemo.sensor.DiffusionEcolaw;

/**
 * <p>
 * Sensor Platform business logic.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class SensorPlatform {

	/** Number of sensor to be created. */
	private final long sensingPeriod;

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
	 * @param period
	 *            Period between temperature sensing
	 */
	public SensorPlatform(final ReactionManager manager,
			final SAPEREAgentsFactory aFactory, final EcolawCompiler eCompiler,
			final long period) {
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

		if (period < 0) {
			sensingPeriod = 1000L;
		} else {
			sensingPeriod = period;
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
		// long waitTime = 2500;
		// for (int counter = 0; counter < sensingPeriod; counter++) {
		// TempSensor spec = null;
		// if (sensingPeriod == 1) {
		// spec = new TempSensor(0.1, 2, 0);
		// } else {
		// spec = new TempSensor(0.0, 1, counter);
		// }
		//
		// _factory.createAgent("temp_sensor" + counter, spec).spawn();
		//
		// try {
		// Thread.sleep(waitTime);
		// } catch (InterruptedException ex) {
		// assert ex != null;
		// }
		//
		// waitTime /= 2;
		// }

		_factory.createAgent("temp_sensor" + sensingPeriod,
				new TempSensor(1.0e3 / sensingPeriod, 0.0, 1, 0)).spawn();
	}

	/**
	 * <p>
	 * Configures eco-laws.
	 * </p>
	 */
	private void setEcolaws() {
		_manager.addEcolaw(DiffusionEcolaw.createPeriodicDiffusion(_compiler,
				"analysis_node", "sensing:Observation", 1.0));
	}

}
