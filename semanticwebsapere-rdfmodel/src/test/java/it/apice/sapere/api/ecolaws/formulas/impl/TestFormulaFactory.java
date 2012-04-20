package it.apice.sapere.api.ecolaws.formulas.impl;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.ecolaws.formulas.AbstractTestFormulaFactory;
import it.apice.sapere.api.ecolaws.formulas.FormulaFactory;
import it.apice.sapere.api.impl.LSAFactoryImpl;

/**
 * <p>
 * Realization of a {@link FormulaFactory} test.
 * </p>
 *
 * @author Paolo Contessi
 *
 */
public class TestFormulaFactory extends AbstractTestFormulaFactory {

	/** Factory reference. */
	private transient PrivilegedLSAFactory factory;

	/** Formula Factory reference. */
	private transient FormulaFactory ffactory;

	@Override
	protected final FormulaFactory createFormulaFactory() {
		if (ffactory == null) {
			ffactory = new FormulaFactoryImpl();
		}

		return ffactory;
	}

	@Override
	protected final LSAFactory createFactory() {
		if (factory == null) {
			factory = initFactory();
		}

		return factory;
	}

	@Override
	protected final PrivilegedLSAFactory createPrivilegedFactory() {
		if (factory == null) {
			factory = initFactory();
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
	private PrivilegedLSAFactory initFactory() {
		return new LSAFactoryImpl("http://www.sapere-project.eu/sapere#node"
				+ System.currentTimeMillis());
	}
}
