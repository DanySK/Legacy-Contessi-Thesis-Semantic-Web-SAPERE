package it.apice.sapere.api.space.core.strategy;


/**
 * <p>
 * This interface models a pipeline useful to customize/extend LSA-space
 * behaviour while handling primitives.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <T>
 *            RDF Statements iterator used by CompiledLSA
 */
public interface CustomStrategyPipeline<T> {

	/**
	 * <p>
	 * Retrieves an ordered list of pipeline's steps.
	 * </p>
	 * 
	 * @return Ordered list of all pipeline's steps.
	 */
	CustomStrategyPipelineStep<T>[] getSteps();

	/**
	 * <p>
	 * Inserts a new step after an already inserted one.
	 * </p>
	 * 
	 * @param step
	 *            New step to be inserted
	 * @param place
	 *            Already inserted step taken as place reference
	 */
	void addStepAfter(CustomStrategyPipelineStep<T> step,
			CustomStrategyPipelineStep<T> place);

	/**
	 * <p>
	 * Inserts a new step before an already inserted one.
	 * </p>
	 * 
	 * @param step
	 *            New step to be inserted
	 * @param place
	 *            Already inserted step taken as place reference
	 */
	void addStepBefore(CustomStrategyPipelineStep<T> step,
			CustomStrategyPipelineStep<T> place);

	/**
	 * <p>
	 * Inserts a new step as first pipeline's step.
	 * </p>
	 * 
	 * @param step
	 *            New step to be inserted
	 */
	void addStepAtTheBeginning(CustomStrategyPipelineStep<T> step);
	
	/**
	 * <p>
	 * Inserts a new step as last pipeline's step.
	 * </p>
	 * 
	 * @param step
	 *            New step to be inserted
	 */
	void addStepAtTheEnd(CustomStrategyPipelineStep<T> step);
}
