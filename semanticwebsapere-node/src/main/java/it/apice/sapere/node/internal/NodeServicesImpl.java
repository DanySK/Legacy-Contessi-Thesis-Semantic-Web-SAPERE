package it.apice.sapere.node.internal;

import it.apice.sapere.api.EcolawFactory;
import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.ecolaws.formulas.FormulaFactory;
import it.apice.sapere.api.management.ReactionManager;
import it.apice.sapere.api.node.NodeServices;
import it.apice.sapere.api.space.core.EcolawCompiler;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.match.functions.MatchFunctRegistry;

import com.hp.hpl.jena.sparql.function.Function;

/**
 * <p>
 * This class implements the {@link NodeServices} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public final class NodeServicesImpl implements NodeServices {

	/** LSA Factory service. */
	private final transient PrivilegedLSAFactory sysLsaFactory;

	/** LSA Compiler service. */
	private final transient LSACompiler<?> lsaCompiler;

	/** LSA Parser service. */
	private final transient LSAParser lsaParser;

	/** Eco-law Factory service. */
	private final transient EcolawFactory lawFactory;

	/** Eco-law Compiler service. */
	private final transient EcolawCompiler lawCompiler;

	/** Formula Factory service. */
	private final transient FormulaFactory fFactory;

	/** LSA-space service. */
	private final transient LSAspaceCore<?> lsaSpace;

	/** Match Functions Customization service. */
	private transient MatchFunctRegistry<Function> mFunctReg;

	/** Run-time reference to the {@link ReactionManager}. */
	private static transient ReactionManager rManager;

	/** Singleton instance. */
	private static transient NodeServicesImpl instance;

	/**
	 * <p>
	 * Builds a new {@link NodeServicesImpl}.
	 * </p>
	 * 
	 * @param lsaFactoryRef
	 *            Reference to LSA Factory
	 * @param lsaCompilerRef
	 *            Reference to LSA Compiler
	 * @param lsaParserRef
	 *            Reference to LSA Parser
	 * @param lawFactoryRef
	 *            Reference to Eco-law Factory
	 * @param lawCompilerRef
	 *            Reference to Eco-law Compiler
	 * @param fFactoryRef
	 *            Reference to Formula Factory
	 * @param lsaSpaceRef
	 *            Reference to LSA-space
	 * @param functReg
	 *            Reference to Match Functions Registry
	 */
	private NodeServicesImpl(final PrivilegedLSAFactory lsaFactoryRef,
			final LSACompiler<?> lsaCompilerRef, final LSAParser lsaParserRef,
			final EcolawFactory lawFactoryRef,
			final EcolawCompiler lawCompilerRef,
			final FormulaFactory fFactoryRef,
			final LSAspaceCore<?> lsaSpaceRef, 
			final MatchFunctRegistry<Function> functReg) {
		sysLsaFactory = lsaFactoryRef;
		lsaCompiler = lsaCompilerRef;
		lsaParser = lsaParserRef;
		lawFactory = lawFactoryRef;
		lawCompiler = lawCompilerRef;
		fFactory = fFactoryRef;
		lsaSpace = lsaSpaceRef;
		mFunctReg = functReg;
	}

	/**
	 * <p>
	 * Initializes services references.
	 * </p>
	 * 
	 * @param lsaFactoryRef
	 *            Reference to LSA Factory
	 * @param lsaCompilerRef
	 *            Reference to LSA Compiler
	 * @param lsaParserRef
	 *            Reference to LSA Parser
	 * @param lawFactoryRef
	 *            Reference to Eco-law Factory
	 * @param lawCompilerRef
	 *            Reference to Eco-law Compiler
	 * @param fFactoryRef
	 *            Reference to Formula Factory
	 * @param lsaSpaceRef
	 *            Reference to LSA-space
	 * @param functReg
	 *            Reference to Match Functions Registry
	 */
	public static void init(final PrivilegedLSAFactory lsaFactoryRef,
			final LSACompiler<?> lsaCompilerRef, final LSAParser lsaParserRef,
			final EcolawFactory lawFactoryRef,
			final EcolawCompiler lawCompilerRef,
			final FormulaFactory fFactoryRef,
			final LSAspaceCore<?> lsaSpaceRef, 
			final MatchFunctRegistry<Function> functReg) {
		if (instance != null) {
			throw new IllegalStateException("Already initialized");
		}

		instance = new NodeServicesImpl(lsaFactoryRef, lsaCompilerRef,
				lsaParserRef, lawFactoryRef, lawCompilerRef, fFactoryRef,
				lsaSpaceRef, functReg);
	}

	/**
	 * <p>
	 * Singleton method.
	 * </p>
	 * 
	 * @return Global reference to {@link NodeServices}
	 */
	public static NodeServices getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Should be initialized first");
		}

		return instance;
	}

	@Override
	public PrivilegedLSAFactory getLSAFactory() {
		return sysLsaFactory;
	}

	@Override
	public LSACompiler<?> getLSACompiler() {
		return lsaCompiler;
	}

	@Override
	public LSAParser getLSAParser() {
		return lsaParser;
	}

	@Override
	public EcolawFactory getEcolawFactory() {
		return lawFactory;
	}

	@Override
	public EcolawCompiler getEcolawCompiler() {
		return lawCompiler;
	}

	@Override
	public FormulaFactory getFormulaFactory() {
		return fFactory;
	}

	@Override
	public LSAspaceCore<?> getLSAspace() {
		return lsaSpace;
	}

	@Override
	public ReactionManager getReactionManager() {
		return rManager;
	}

	@Override
	public MatchFunctRegistry<Function> getCustomFunctionRegistry() {
		return mFunctReg;
	}

	/**
	 * <p>
	 * Registers a runtime reference to the {@link ReactionManager}.
	 * </p>
	 * 
	 * @param manager
	 *            {@link ReactionManager} instance.
	 */
	public static void registerReactionManager(final ReactionManager manager) {
		if (manager == null) {
			throw new IllegalArgumentException("");
		}

		rManager = manager;
	}

}
