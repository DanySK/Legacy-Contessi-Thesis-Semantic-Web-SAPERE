package it.apice.sapere.distdemo.sensor.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.management.ReactionManager;
import it.apice.sapere.api.node.agents.SAPEREAgentsFactory;

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

	/**
	 * <p>
	 * Builds a new {@link SensorPlatform}.
	 * </p>
	 * 
	 * @param manager
	 *            Reference to reaction manager
	 * @param aFactory
	 *            Reference to Agents Factory
	 */
	public SensorPlatform(final ReactionManager manager,
			final SAPEREAgentsFactory aFactory) {
		if (manager == null) {
			throw new IllegalArgumentException("Invalid manager provided");
		}

		if (aFactory == null) {
			throw new IllegalArgumentException(
					"Invalid agents factory provided");
		}

		_manager = manager;
		_factory = aFactory;
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
		assert _manager != null;
	}

}
