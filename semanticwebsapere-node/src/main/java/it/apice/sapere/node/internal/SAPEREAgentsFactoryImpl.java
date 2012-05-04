package it.apice.sapere.node.internal;

import it.apice.sapere.api.EcolawFactory;
import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.ecolaws.formulas.FormulaFactory;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.node.agents.SAPEREAgent;
import it.apice.sapere.node.agents.SAPEREAgentsFactory;

/**
 * <p>
 * This class implements the {@link SAPEREAgentsFactory} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class SAPEREAgentsFactoryImpl implements SAPEREAgentsFactory {

	/**
	 * <p>
	 * Bulids a new {@link SAPEREAgentsFactoryImpl}.
	 * </p>
	 * 
	 * @param sysLsaFactory
	 *            A {@link LSAFactory} reference.
	 * @param lsaParser
	 *            A {@link LSAParser} reference.
	 * @param lawFactory
	 *            A {@link EcolawFactory} reference.
	 * @param fFactory
	 *            A {@link FormulaFactory} reference.
	 * @param sysSpace
	 *            A {@link LSAspace} reference.
	 */
	public SAPEREAgentsFactoryImpl(final LSAFactory sysLsaFactory,
			final LSAParser lsaParser, final EcolawFactory lawFactory,
			final FormulaFactory fFactory, final LSAspace sysSpace) {
		if (sysLsaFactory == null) {
			throw new IllegalArgumentException("Invalid LSA Factory provided");
		}

		if (lsaParser == null) {
			throw new IllegalArgumentException("Invalid LSA Parser provided");
		}

		if (lawFactory == null) {
			throw new IllegalArgumentException(
					"Invalid Eco-law Factory provided");
		}

		if (fFactory == null) {
			throw new IllegalArgumentException(
					"Invalid Eco-law's Formula Factory provided");
		}

		if (sysSpace == null) {
			throw new IllegalArgumentException("Invalid LSA-space provided");
		}
	}

	@Override
	public final SAPEREAgent createAgent(
			final Class<? extends SAPEREAgent> agentClass) {
		if (agentClass == null) {
			throw new IllegalArgumentException("Invalid Agent Class provided");
		}

		return null;
	}

}
