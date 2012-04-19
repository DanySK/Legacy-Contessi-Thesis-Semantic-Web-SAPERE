package it.apice.sapere.api.ecolaws.formulas.impl;

import it.apice.sapere.api.ecolaws.formulas.FormulaFactory;
import it.apice.sapere.api.ecolaws.formulas.GtEqFormula;
import it.apice.sapere.api.ecolaws.formulas.GtFormula;
import it.apice.sapere.api.ecolaws.formulas.IsFormula;
import it.apice.sapere.api.ecolaws.formulas.LtEqFormula;
import it.apice.sapere.api.ecolaws.formulas.LtFormula;
import it.apice.sapere.api.ecolaws.formulas.RightOperand;

/**
 * <p>
 * This class implements the {@link FormulaFactory} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class FormulaFactoryImpl implements FormulaFactory {

	@Override
	public final <Type> IsFormula<Type> createIsFormula(final String rOpString,
			final RightOperand<Type> rOp) {
		return new IsFormulaImpl<Type>(rOpString) {

			@Override
			public Type rightOperand() {
				return rOp.rightOperand();
			}

		};
	}

	@Override
	public final <Type extends Comparable<Type>> GtFormula<Type> 
			createGtFormula(final String rOpString, 
					final RightOperand<Type> rOp) {
		return new GtFormulaImpl<Type>(rOpString) {

			@Override
			public Type rightOperand() {
				return rOp.rightOperand();
			}

		};
	}

	@Override
	public final <Type extends Comparable<Type>> GtEqFormula<Type> 
			createGtEqFormula(final String rOpString, 
					final RightOperand<Type> rOp) {
		return new GtEqFormulaImpl<Type>(rOpString) {

			@Override
			public Type rightOperand() {
				return rOp.rightOperand();
			}

		};
	}

	@Override
	public final <Type extends Comparable<Type>> LtFormula<Type> 
			createLtFormula(final String rOpString, 
					final RightOperand<Type> rOp) {
		return new LtFormulaImpl<Type>(rOpString) {

			@Override
			public Type rightOperand() {
				return rOp.rightOperand();
			}

		};
	}

	@Override
	public final <Type extends Comparable<Type>> LtEqFormula<Type> 
			createLtEqFormula(final String rOpString, 
					final RightOperand<Type> rOp) {
		return new LtEqFormulaImpl<Type>(rOpString) {

			@Override
			public Type rightOperand() {
				return rOp.rightOperand();
			}

		};
	}

}
