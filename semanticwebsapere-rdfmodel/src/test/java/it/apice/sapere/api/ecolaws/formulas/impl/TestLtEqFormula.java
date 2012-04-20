package it.apice.sapere.api.ecolaws.formulas.impl;

import it.apice.sapere.api.ecolaws.formulas.AbstractTestFormula;
import it.apice.sapere.api.ecolaws.formulas.LtEqFormula;
import it.apice.sapere.api.ecolaws.terms.Formula;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Realization of a {@link Formula} test: {@link LtEqFormula}.
 * </p>
 *
 * @author Paolo Contessi
 *
 */
public class TestLtEqFormula extends AbstractTestFormula {

	@Override
	protected final Formula<Integer> createFormula() {
		return new LtEqFormulaImpl<Integer>("0") {

			@Override
			public Integer rightOperand() {
				return 0;
			}
		};
	}

	@Override
	protected final List<Integer> createValidValues() {
		final List<Integer> vals = new LinkedList<Integer>();

		for (int i = -10; i <= 0; i++) {
			vals.add(i);
		}

		return vals;
	}

	@Override
	protected final List<Integer> createInvalidValues() {
		final List<Integer> vals = new LinkedList<Integer>();

		for (int i = 1; i <= 10; i++) {
			vals.add(i);
		}

		return vals;
	}
}
