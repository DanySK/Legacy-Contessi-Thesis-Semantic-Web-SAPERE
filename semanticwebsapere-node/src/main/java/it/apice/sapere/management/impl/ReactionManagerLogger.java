package it.apice.sapere.management.impl;

import it.apice.sapere.api.space.core.CompiledEcolaw;
import it.apice.sapere.api.space.match.MatchingEcolaw;
import it.apice.sapere.management.ReactionManager;
import it.apice.sapere.management.ReactionManagerObserver;
import it.apice.sapere.node.internal.LoggerFactoryImpl;

import org.apache.log4j.Logger;

/**
 * <p>
 * This class realizes a Logger that can be attached to the
 * {@link ReactionManager} in order to visualize its behaviour.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class ReactionManagerLogger implements ReactionManagerObserver {

	/** Logger reference. */
	private final transient Logger logger;

	/**
	 * <p>
	 * Builds a new {@link ReactionManagerLogger}.
	 * </p>
	 * 
	 * @param manager
	 *            The manager that will be observed
	 */
	public ReactionManagerLogger(final ReactionManager manager) {
		logger = LoggerFactoryImpl.getInstance().getLogger(manager);
	}

	@Override
	public final void ecolawAdded(final CompiledEcolaw law) {
		logger.info("reaction_manager$ Eco-law " + law.getLabel() + " ADDED");
	}

	@Override
	public final void ecolawRemoved(final CompiledEcolaw law) {
		logger.info("reaction_manager$ Eco-law " + law.getLabel() + " REMOVED");
	}

	@Override
	public final void ecolawEnabled(final MatchingEcolaw law, final long time) {
		logger.info("reaction_manager$ Eco-law " + law.getLabel()
				+ " chosen as next");
	}

	@Override
	public final void ecolawApplied(final MatchingEcolaw law, final long time) {
		logger.info("reaction_manager$ Eco-law " + law.getLabel()
				+ " applied, looking for next");
	}

}
