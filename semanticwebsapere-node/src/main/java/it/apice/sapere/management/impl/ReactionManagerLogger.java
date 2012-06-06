package it.apice.sapere.management.impl;

import it.apice.sapere.api.management.ReactionManager;
import it.apice.sapere.api.management.ReactionManagerObserver;
import it.apice.sapere.api.node.LogUtils;
import it.apice.sapere.api.space.core.CompiledEcolaw;
import it.apice.sapere.api.space.match.MatchingEcolaw;
import it.apice.sapere.node.internal.LoggerFactoryImpl;

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
	private final transient LogUtils logger;

	/**
	 * <p>
	 * Builds a new {@link ReactionManagerLogger}.
	 * </p>
	 * 
	 * @param manager
	 *            The manager that will be observed
	 */
	public ReactionManagerLogger(final ReactionManager manager) {
		assert manager != null;
		logger = LoggerFactoryImpl.getInstance().getLogger(getClass());
	}

	@Override
	public final void ecolawAdded(final CompiledEcolaw law) {
		logger.log("reaction_manager$ Eco-law" + law.getLabel() + " ADDED\n" 
				+ law.toString());
	}

	@Override
	public final void ecolawRemoved(final CompiledEcolaw law) {
		logger.log("reaction_manager$ Eco-law" + law.getLabel() + " REMOVED");
	}

	@Override
	public final void ecolawEnabled(final MatchingEcolaw law, final long time) {
		logger.spy("reaction_manager$ Eco-law" + law.getLabel()
				+ " chosen as next");
	}

	@Override
	public final void ecolawApplied(final MatchingEcolaw law, final long time) {
		logger.spy("reaction_manager$ Eco-law" + law.getLabel()
				+ " applied, looking for next");
	}

}
