package it.apice.sapere.space.avoid;

import it.apice.sapere.api.avoid.RDFModelServices;
import it.apice.sapere.api.space.core.EcolawCompiler;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.core.impl.EcolawCompilerImpl;
import it.apice.sapere.api.space.core.impl.LSACompilerImpl;
import it.apice.sapere.space.impl.LSAspaceImpl;

/**
 * <p>
 * Utility class that publishes bundles services when used outside an OSGi
 * context.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class RDFSpaceServices extends RDFModelServices {

	/** LSA-space. */
	private final transient LSAspaceCore<?> space;

	/** LSA Compiler. */
	private final transient LSACompiler<?> compiler;

	/** Ecolaw Compiler. */
	private final transient EcolawCompiler lCompiler;

	/** Singleton instance. */
	private static transient RDFSpaceServices instance;

	/**
	 * <p>
	 * Builds a new {@link RDFSpaceServices}.
	 * </p>
	 */
	protected RDFSpaceServices() {
		space = new LSAspaceImpl(getLSAFactory());
		compiler = new LSACompilerImpl(getLSAFactory());
		lCompiler = new EcolawCompilerImpl();
	}

	/**
	 * <p>
	 * Retrieves the {@link LSAspaceCore} reference.
	 * </p>
	 * 
	 * @return The {@link LSAspaceCore} reference
	 */
	public final LSAspaceCore<?> getSpace() {
		return space;
	}

	/**
	 * <p>
	 * Retrieves the {@link LSACompiler} reference.
	 * </p>
	 * 
	 * @return The {@link LSACompiler} reference
	 */
	public final LSACompiler<?> getLSACompiler() {
		return compiler;
	}

	/**
	 * <p>
	 * Retrieves the {@link EcolawCompiler} reference.
	 * </p>
	 * 
	 * @return The {@link EcolawCompiler} reference
	 */
	public final EcolawCompiler getEcolawCompiler() {
		return lCompiler;
	}

	/**
	 * <p>
	 * Initializes the {@link RDFSpaceServices}.
	 * </p>
	 */
	public static void init() {
		RDFModelServices.init();
		instance = new RDFSpaceServices();
	}

	/**
	 * <p>
	 * Initializes {@link RDFSpaceServices}.
	 * </p>
	 * 
	 * @param nodeUri
	 *            Node URI
	 */
	public static void init(final String nodeUri) {
		RDFModelServices.init(nodeUri);
		instance = new RDFSpaceServices();
	}

	/**
	 * <p>
	 * Singleton method.
	 * </p>
	 * 
	 * @return The singleton instance
	 */
	public static RDFSpaceServices getInstance() {
		if (instance == null) {
			init();
		}

		return instance;
	}
}
