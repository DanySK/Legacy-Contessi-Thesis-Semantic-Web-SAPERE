package it.apice.sapere.distdemo.analysis.impl;

import it.apice.sapere.api.management.ReactionManager;
import it.apice.sapere.api.node.agents.SAPEREAgentsFactory;

/**
 * <p>
 * Analysis Platform business logic.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class AnalysisPlatform {

	/** Reference to reaction manager. */
	private final transient ReactionManager _manager;

	/** Reference to Agents Factory. */
	private final transient SAPEREAgentsFactory _factory;

	/**
	 * <p>
	 * Builds a new {@link AnalysisPlatform}.
	 * </p>
	 * 
	 * @param manager
	 *            Reference to reaction manager
	 * @param aFactory
	 *            Reference to Agents Factory
	 */
	public AnalysisPlatform(final ReactionManager manager,
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
	 */
	public void execute() {
		setEcolaws();
		runAgents();
	}

	/**
	 * <p>
	 * Configures agents.
	 * </p>
	 */
	private void runAgents() {
		//_factory.createAgent("temp_sensor", new );
	}

	/**
	 * <p>
	 * Configures eco-laws.
	 * </p>
	 */
	private void setEcolaws() {
		// TODO Auto-generated method stub
		
	}

}
