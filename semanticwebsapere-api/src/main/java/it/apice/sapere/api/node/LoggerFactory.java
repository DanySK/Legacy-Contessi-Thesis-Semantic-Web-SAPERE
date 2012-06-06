package it.apice.sapere.api.node;

import it.apice.sapere.api.node.agents.SAPEREAgent;

/**
 * <p>
 * This interface models an utility entity that handles logs.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface LoggerFactory {

	/**
	 * <p>
	 * Obtains a reference to a logger.
	 * </p>
	 * 
	 * @param agent
	 *            The agent that will use it
	 * @return A {@link LogUtils} reference
	 */
	LogUtils getLogger(final SAPEREAgent agent);
	
	/**
	 * <p>
	 * Obtains a reference to a logger.
	 * </p>
	 * 
	 * @param clazz
	 *            A {@link Class}
	 * @return A {@link LogUtils} reference
	 */
	LogUtils getLogger(final Class<?> clazz);
}