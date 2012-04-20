package it.apice.sapere.api.ecolaws.formulas;

import it.apice.sapere.api.ecolaws.terms.Formula;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * This class tests a {@link Formula} implementation.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestFormula {

	/**
	 * <p>
	 * Creates a new Formula to be tested.
	 * </p>
	 * 
	 * @return The Formula to be tested
	 */
	protected abstract Formula<Integer> createFormula();

	/**
	 * <p>
	 * Creates a list of values that, passed to <code>accept()</code>, should
	 * return <code>true</code>.
	 * </p>
	 * 
	 * @return List of values that should be accepted
	 */
	protected abstract List<Integer> createValidValues();

	/**
	 * <p>
	 * Creates a list of values that, passed to <code>accept()</code>, should
	 * return <code>false</code>.
	 * </p>
	 * 
	 * @return List of values that should not be accepted
	 */
	protected abstract List<Integer> createInvalidValues();

	/**
	 * <p>
	 * Tests the recognition of valid values.
	 * </p>
	 */
	@Test
	public final void testValidRecognition() {
		final Formula<Integer> f = createFormula();

		for (Integer val : createValidValues()) {
			Assert.assertTrue(
					String.format("Value \"%d\" not recognized as valid", val),
					f.accept(val));
		}
	}

	/**
	 * <p>
	 * Tests the recognition of invalid values.
	 * </p>
	 */
	@Test
	public final void testInvalidRecognition() {
		final Formula<Integer> f = createFormula();

		for (Integer val : createInvalidValues()) {
			Assert.assertFalse(String.format(
					"Value \"%d\" not recognized as invalid", val), f
					.accept(val));
		}
	}
}
