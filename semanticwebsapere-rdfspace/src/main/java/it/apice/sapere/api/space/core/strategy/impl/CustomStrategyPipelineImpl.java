package it.apice.sapere.api.space.core.strategy.impl;

import it.apice.sapere.api.space.core.strategy.CustomStrategyPipelineStep;
import it.apice.sapere.api.space.core.strategy.CustomStrategyPipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class implements the {@link CustomStrategyPipeline} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <T>
 *            RDF Statements iterator used by CompiledLSAs.
 */
public class CustomStrategyPipelineImpl<T> 
		implements CustomStrategyPipeline<T> {

	/** Pipeline. */
	private final transient List<CustomStrategyPipelineStep<T>> steps = 
			new ArrayList<CustomStrategyPipelineStep<T>>();

	@SuppressWarnings("unchecked")
	@Override
	public final CustomStrategyPipelineStep<T>[] getSteps() {
		return steps.toArray(new CustomStrategyPipelineStep[steps.size()]);
	}

	@Override
	public final void addStepAfter(final CustomStrategyPipelineStep<T> step,
			final CustomStrategyPipelineStep<T> place) {
		steps.add(steps.indexOf(place) + 1, step);
	}

	@Override
	public final void addStepBefore(final CustomStrategyPipelineStep<T> step,
			final CustomStrategyPipelineStep<T> place) {
		steps.add(steps.indexOf(place), step);
	}

	@Override
	public final void addStepAtTheBeginning(
			final CustomStrategyPipelineStep<T> step) {
		steps.add(0, step);
	}

	@Override
	public final void addStepAtTheEnd(
			final CustomStrategyPipelineStep<T> step) {
		steps.add(step);
	}

}
