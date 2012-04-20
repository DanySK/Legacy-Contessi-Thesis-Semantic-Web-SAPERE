package it.apice.sapere.api.ecolaws.formulas;

import it.apice.sapere.api.AbstractLSAModelTest;
import it.apice.sapere.api.ecolaws.terms.Formula;

import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * This class tests a {@link FormulaFactory} implementation.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestFormulaFactory extends AbstractLSAModelTest {

	/**
	 * <p>
	 * Creates a {@link FormulaFactory} to be tested.
	 * </p>
	 * 
	 * @return An instance of the factory
	 */
	protected abstract FormulaFactory createFormulaFactory();

	/**
	 * <p>
	 * Tests {@link GtEqFormula}.
	 * </p>
	 */
	@Test
	public final void testGtEqFactory() {
		try {
			createFormulaFactory().<Integer> createGtEqFormula(null, null);
			Assert.fail("Should not create formula (null, null)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			createFormulaFactory().<Integer> createGtEqFormula("", null);
			Assert.fail("Should not create formula (\"\", null)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			createFormulaFactory().<Integer> createGtEqFormula("0", null);
			Assert.fail("Should not create formula (ok, null)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		final Formula<Integer> f = createFormulaFactory()
				.<Integer> createGtEqFormula("0", new RightOperand<Integer>() {

					@Override
					public Integer rightOperand() {
						return 0;
					}
				});
		Assert.assertNotNull("A formula should have been created, why not?", f);
	}

	/**
	 * <p>
	 * Tests {@link GtFormula}.
	 * </p>
	 */
	@Test
	public final void testGtFactory() {
		try {
			createFormulaFactory().<Integer> createGtFormula(null, null);
			Assert.fail("Should not create formula (null, null)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			createFormulaFactory().<Integer> createGtFormula("", null);
			Assert.fail("Should not create formula (\"\", null)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			createFormulaFactory().<Integer> createGtFormula("0", null);
			Assert.fail("Should not create formula (ok, null)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		final Formula<Integer> f = createFormulaFactory()
				.<Integer> createGtFormula("0", new RightOperand<Integer>() {

					@Override
					public Integer rightOperand() {
						return 0;
					}
				});
		Assert.assertNotNull("A formula should have been created, why not?", f);
	}

	/**
	 * <p>
	 * Tests {@link IsFormula}.
	 * </p>
	 */
	@Test
	public final void testIsFactory() {
		try {
			createFormulaFactory().<Integer> createIsFormula(null, null);
			Assert.fail("Should not create formula (null, null)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			createFormulaFactory().<Integer> createIsFormula("", null);
			Assert.fail("Should not create formula (\"\", null)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			createFormulaFactory().<Integer> createIsFormula("0", null);
			Assert.fail("Should not create formula (ok, null)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		final Formula<Integer> f = createFormulaFactory()
				.<Integer> createIsFormula("0", new RightOperand<Integer>() {

					@Override
					public Integer rightOperand() {
						return 0;
					}
				});
		Assert.assertNotNull("A formula should have been created, why not?", f);
	}

	/**
	 * <p>
	 * Tests {@link LtEqFormula}.
	 * </p>
	 */
	@Test
	public final void testLtEqFactory() {
		try {
			createFormulaFactory().<Integer> createLtEqFormula(null, null);
			Assert.fail("Should not create formula (null, null)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			createFormulaFactory().<Integer> createLtEqFormula("", null);
			Assert.fail("Should not create formula (\"\", null)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			createFormulaFactory().<Integer> createLtEqFormula("0", null);
			Assert.fail("Should not create formula (ok, null)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		final Formula<Integer> f = createFormulaFactory()
				.<Integer> createLtEqFormula("0", new RightOperand<Integer>() {

					@Override
					public Integer rightOperand() {
						return 0;
					}
				});
		Assert.assertNotNull("A formula should have been created, why not?", f);
	}

	/**
	 * <p>
	 * Tests {@link LtFormula}.
	 * </p>
	 */
	@Test
	public final void testLtFactory() {
		try {
			createFormulaFactory().<Integer> createLtFormula(null, null);
			Assert.fail("Should not create formula (null, null)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			createFormulaFactory().<Integer> createLtFormula("", null);
			Assert.fail("Should not create formula (\"\", null)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			createFormulaFactory().<Integer> createLtFormula("0", null);
			Assert.fail("Should not create formula (ok, null)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		final Formula<Integer> f = createFormulaFactory()
				.<Integer> createLtFormula("0", new RightOperand<Integer>() {

					@Override
					public Integer rightOperand() {
						return 0;
					}
				});
		Assert.assertNotNull("A formula should have been created, why not?", f);
	}
}
