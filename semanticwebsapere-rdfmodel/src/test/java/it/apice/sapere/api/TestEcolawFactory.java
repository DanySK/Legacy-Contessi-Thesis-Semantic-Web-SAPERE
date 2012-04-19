package it.apice.sapere.api;

import it.apice.sapere.api.ecolaws.formulas.FormulaFactory;
import it.apice.sapere.api.ecolaws.formulas.impl.FormulaFactoryImpl;
import it.apice.sapere.api.impl.EcolawFactoryImpl;
import it.apice.sapere.api.impl.LSAFactoryImpl;

/**
 * <p>
 * Test for EcolawFactoryImpl.
 * </p>
 *
 * @author Paolo Contessi
 *
 */
public class TestEcolawFactory extends AbstractTestEcolawFactory {

	/** LSA Factory reference. */
	private transient PrivilegedLSAFactory factory;
	
	/** Eco-law Factory reference. */
	private transient EcolawFactory efactory;
	
	/** Formula Factory reference. */
	private transient FormulaFactory ffactory;

	@Override
	protected final LSAFactory createFactory() {
		if (factory == null) {
			factory = initLSAFactory();
		}

		return factory;
	}

	@Override
	protected final PrivilegedLSAFactory createPrivilegedFactory() {
		if (factory == null) {
			factory = initLSAFactory();
		}

		return factory;
	}

	/**
	 * <p>
	 * Initializes the factory.
	 * </p>
	 *
	 * @return Instance to the tested factory
	 */
	private PrivilegedLSAFactory initLSAFactory() {
		return new LSAFactoryImpl(
				"http://www.sapere-project.eu/sapere#node"
						+ System.currentTimeMillis());
	}
	
	/**
	 * <p>
	 * Initializes the factory.
	 * </p>
	 *
	 * @return Instance to the tested factory
	 */
	private EcolawFactory initEcolawFactory() {
		return new EcolawFactoryImpl();
	}
	
	@Override
	protected final EcolawFactory createEcolawFactory() {
		if (efactory == null) {
			efactory = initEcolawFactory();
		}

		return efactory;
	}
	
	/**
	 * <p>
	 * Initializes the factory.
	 * </p>
	 *
	 * @return Instance to the tested factory
	 */
	private FormulaFactory initFormulaFactory() {
		return new FormulaFactoryImpl();
	}

	@Override
	protected final FormulaFactory createFormulaFactory() {
		if (ffactory == null) {
			ffactory = initFormulaFactory();
		}

		return ffactory;
	}

}
