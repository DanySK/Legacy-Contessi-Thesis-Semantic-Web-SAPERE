package it.apice.sapere.node;

import it.apice.sapere.node.agents.SAPEREAgent;

import org.osgi.framework.BundleActivator;

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
	 * @param bActiv
	 *            A {@link BundleActivator}
	 * @return A {@link LogUtils} reference
	 */
	LogUtils getLogger(final BundleActivator bActiv);
	
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