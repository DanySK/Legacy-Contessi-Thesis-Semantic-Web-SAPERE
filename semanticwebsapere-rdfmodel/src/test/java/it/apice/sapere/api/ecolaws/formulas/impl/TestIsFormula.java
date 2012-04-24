package it.apice.sapere.api.ecolaws.formulas.impl;

import it.apice.sapere.api.ecolaws.formulas.AbstractTestFormula;
import it.apice.sapere.api.ecolaws.formulas.IsFormula;
import it.apice.sapere.api.ecolaws.terms.Formula;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Realization of a {@link Formula} test: {@link IsFormula}.
 * </p>
 *
 * @author Paolo Contessi
 *
 */
public class TestIsFormula extends AbstractTestFormula {

	private static final int NUM_10 = 10;

	@Override
	protected final Formula<Integer> createFormula() {
		return new IsFormulaImpl<Integer>("0") {

			@Override
			public Integer rightOperand() {
				return 0;
			}
		};
	}

	@Override
	protected final List<Integer> createValidValues() {
		final List<Integer> vals = new LinkedList<Integer>();

		vals.add(0);

		return vals;
	}

	@Override
	protected final List<Integer> createInvalidValues() {
		final List<Integer> vals = new LinkedList<Integer>();

		for (int i = -NUM_10; i <= -1; i++) {
			vals.add(i);
		}
		
		for (int i = 1; i <= NUM_10; i++) {
			vals.add(i);
		}

		return vals;
	}
}
