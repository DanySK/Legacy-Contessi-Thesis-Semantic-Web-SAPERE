package it.apice.sapere.node.internal;

import it.apice.sapere.api.EcolawFactory;
import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.ecolaws.formulas.FormulaFactory;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.api.space.core.EcolawCompiler;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.management.ReactionManager;
import it.apice.sapere.node.agents.NodeServices;
import it.apice.sapere.node.agents.UserNodeServices;

/**
 * <p>
 * This class implements the {@link UserNodeServices} interface.
 * </p>
 *
 * @author Paolo Contessi
 *
 */
public class UserNodeServicesImpl implements UserNodeServices {

	/** LSA Factory service. */
	private final transient LSAFactory lsaFactory;

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
	private final transient LSAspace lsaSpace;

	/** Run-time reference to the {@link ReactionManager}. */
	private final transient ReactionManager rManager;

	/**
	 * <p>
	 * Builds a new {@link UserNodeServicesImpl}.
	 * </p>
	 * 
	 * @param src
	 *            The {@link NodeServices} which generates it
	 */
	UserNodeServicesImpl(final NodeServices src) {
		lsaFactory = src.getLSAFactory();
		lsaCompiler = src.getLSACompiler();
		lsaParser = src.getLSAParser();
		lawFactory = src.getEcolawFactory();
		lawCompiler = src.getEcolawCompiler();
		fFactory = src.getFormulaFactory();
		lsaSpace = src.getLSAspace();
		rManager = src.getReactionManager();
	}

	@Override
	public final LSAFactory getLSAFactory() {
		return lsaFactory;
	}

	@Override
	public final LSACompiler<?> getLSACompiler() {
		return lsaCompiler;
	}

	@Override
	public final LSAParser getLSAParser() {
		return lsaParser;
	}

	@Override
	public final EcolawFactory getEcolawFactory() {
		return lawFactory;
	}

	@Override
	public final EcolawCompiler getEcolawCompiler() {
		return lawCompiler;
	}

	@Override
	public final FormulaFactory getFormulaFactory() {
		return fFactory;
	}

	@Override
	public final LSAspace getLSAspace() {
		return lsaSpace;
	}

	@Override
	public final ReactionManager getReactionManager() {
		return rManager;
	}
}
