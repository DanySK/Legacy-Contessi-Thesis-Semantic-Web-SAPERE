package it.apice.sapere.api;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.formulas.FormulaFactory;
import it.apice.sapere.api.ecolaws.formulas.RightOperand;
import it.apice.sapere.api.ecolaws.terms.Formula;
import it.apice.sapere.api.ecolaws.terms.ListTerm;
import it.apice.sapere.api.ecolaws.terms.PatternNameTerm;
import it.apice.sapere.api.ecolaws.terms.PropertyTerm;
import it.apice.sapere.api.ecolaws.terms.ValueTerm;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.SemanticDescription;
import it.apice.sapere.api.lsas.values.BooleanValue;
import it.apice.sapere.api.lsas.values.LSAidValue;
import it.apice.sapere.api.lsas.values.LongValue;
import it.apice.sapere.api.lsas.values.PropertyValue;
import it.apice.sapere.api.lsas.values.URIValue;

import java.net.URI;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * Test case for EcolawFactory entity.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestEcolawFactory 
		extends AbstractEcolawModelTest {

	/** Reference to an Ecolaw Factory. */
	private final transient EcolawFactory factory = createEcolawFactory();

	/** Reference to an LSA Factory. */
	private final transient LSAFactory lsaFactory = createFactory();

	/**
	 * <p>
	 * Provides a new {@link FormulaFactory}.
	 * </p>
	 * 
	 * @return Instance of FormulaFactory
	 */
	protected abstract FormulaFactory createFormulaFactory();

	/**
	 * Checks that unwanted behaviours are handled (createEcolaw).
	 */
	@Test
	public final void testUnwantedEcolaws() {
		Assert.assertNotNull(factory.createEcolaw());

		try {
			factory.createEcolaw(null);
			Assert.fail("Strange Ecolaw (1)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createEcolaw("");
			Assert.fail("Strange Ecolaw (2)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}
	}

	/**
	 * Checks that unwanted behaviours are handled (createProduct).
	 */
	@Test
	public final void testUnwantedProducts() {
		try {
			factory.createProduct((String) null);
			Assert.fail("Strange Product (1)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createProduct((PatternNameTerm) null);
			Assert.fail("Strange Product (2)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createProduct("");
			Assert.fail("Strange Product (3)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}
	}

	/**
	 * Checks that unwanted behaviours are handled (createReactant).
	 */
	@Test
	public final void testUnwantedReactants() {
		try {
			factory.createReactant((String) null);
			Assert.fail("Strange Reactant (1)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createReactant((PatternNameTerm) null);
			Assert.fail("Strange Reactant (2)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createReactant("");
			Assert.fail("Strange Reactant (3)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}
	}

	/**
	 * Checks that unwanted behaviours are handled (createPatternNameTerm).
	 */
	@Test
	public final void testUnwantedPatternNameTerm() {
		try {
			factory.createPatternNameTerm(null);
			Assert.fail("Strange PatternNameTerm (1)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createPatternNameTerm("");
			Assert.fail("Strange PatternNameTerm (2)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}
	}

	/**
	 * Checks that unwanted behaviours are handled (createXXXRate).
	 */
	@Test
	public final void testUnwantedRates() {
		Assert.assertNotNull(factory.createASAPRate());

		final double[] rates = new double[] { 0.0, -1.0, -0.01, -50.7 };
		int i = 1;
		for (; i <= rates.length; i++) {
			try {
				factory.createMarkovianRate(rates[i - 1]);
				Assert.fail("Strange Rate (" + i + ")");
			} catch (Exception ex) {
				Assert.assertTrue(ex instanceof IllegalArgumentException);
			}
		}

		try {
			factory.createMarkovianRate(null);
			Assert.fail("Strange Rate (" + i + ")");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}
	}

	/**
	 * Checks that unwanted behaviours are handled (createXXXVarTerm).
	 */
	@Test
	public final void testVarTerms() {
		try {
			factory.createVarTerm(null);
			Assert.fail("Strange VarTerm (1)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createVarTerm("");
			Assert.fail("Strange VarTerm (2)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createAnnotatedVarTerm(null, createValidFormula());
			Assert.fail("Strange AnnotatedVarTerm (1)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createAnnotatedVarTerm("", createValidFormula());
			Assert.fail("Strange AnnotatedVarTerm (2)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createAnnotatedVarTerm("asv", null);
			Assert.fail("Strange AnnotatedVarTerm (3)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createAnnotatedVarTerm(null, null);
			Assert.fail("Strange AnnotatedVarTerm (4)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createAnnotatedVarTerm("", null);
			Assert.fail("Strange AnnotatedVarTerm (5)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}
	}

	/**
	 * Checks that unwanted behaviours are handled (createXXXTerm).
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testGroundTerms() {
		try {
			factory.createPropertyTerm((PropertyName) null);
			Assert.fail("Strange PropertyTerm (1)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.<BooleanValue> createListTerm((BooleanValue) null);
			Assert.fail("Strange ListTerm (1)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.<BooleanValue> createListTerm(new BooleanValue[0]);
			Assert.fail("Strange ListTerm (2)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.<BooleanValue> createListTermFromTerms(
					(ValueTerm<BooleanValue>[]) null);
			Assert.fail("Strange ListTerm (3)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createListTermFromTerms(new ValueTerm[0]);
			Assert.fail("Strange ListTerm (4)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createListTerm(null, null);
			Assert.fail("Strange ListTerm (5)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createSDescTerm((LSA) null);
			Assert.fail("Strange SDescTerm (1)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createSDescTerm((SemanticDescription) null);
			Assert.fail("Strange SDescTerm (2)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createValueTerm((PropertyValue<?, ?>) null);
			Assert.fail("Strange ValueTerm (1)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createValueTerm((String) null);
			Assert.fail("Strange ValueTerm (2)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createValueTerm("");
			Assert.fail("Strange ValueTerm (3)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createTypedValueTerm(null);
			Assert.fail("Strange TypedValueTerm (1)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}
	}

	/**
	 * Checks that unwanted behaviours are handled (create(Var)PropertyTerm).
	 */
	@Test
	public final void testVarPropertyTerms() {
		try {
			factory.createPropertyTerm((String) null);
			Assert.fail("Strange VarPropertyTerm (1)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createPropertyTerm("");
			Assert.fail("Strange VarPropertyTerm (2)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createPropertyTerm((String) null,
					this.<PropertyName> createValidFormula());
			Assert.fail("Strange VarPropertyTerm (1)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createPropertyTerm("",
					this.<PropertyName> createValidFormula());
			Assert.fail("Strange VarPropertyTerm (2)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createPropertyTerm("asv", null);
			Assert.fail("Strange VarPropertyTerm (3)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createPropertyTerm(null, null);
			Assert.fail("Strange VarPropertyTerm (4)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createPropertyTerm("", null);
			Assert.fail("Strange VarPropertyTerm (5)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}
	}

	/**
	 * Checks that unwanted behaviours are handled (create(Var)ListTerm).
	 */
	@Test
	public final void testVarListTerms() {
		try {
			factory.createListTerm((String) null);
			Assert.fail("Strange VarListTerm (1)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createListTerm("");
			Assert.fail("Strange VarListTerm (2)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createListTerm((String) null,
					this.<List<Term<BooleanValue>>> createValidFormula());
			Assert.fail("Strange VarListTerm (1)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createListTerm("",
					this.<List<Term<BooleanValue>>> createValidFormula());
			Assert.fail("Strange VarListTerm (2)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createListTerm("asv", null);
			Assert.fail("Strange VarListTerm (3)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createListTerm(null, null);
			Assert.fail("Strange VarListTerm (4)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createListTerm("", null);
			Assert.fail("Strange VarListTerm (5)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}
	}

	/**
	 * Checks that unwanted behaviours are handled (create(Var)SDescTerm).
	 */
	@Test
	public final void testVarSDescTerms() {
		try {
			factory.createSDescTerm((String) null);
			Assert.fail("Strange VarSDescTerm (1)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createSDescTerm("");
			Assert.fail("Strange VarSDescTerm (2)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createSDescTerm((String) null,
					this.<SemanticDescription> createValidFormula());
			Assert.fail("Strange VarSDescTerm (1)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createSDescTerm("",
					this.<SemanticDescription> createValidFormula());
			Assert.fail("Strange VarSDescTerm (2)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createSDescTerm("asv", null);
			Assert.fail("Strange VarSDescTerm (3)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createSDescTerm(null, null);
			Assert.fail("Strange VarSDescTerm (4)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}

		try {
			factory.createSDescTerm("", null);
			Assert.fail("Strange VarSDescTerm (5)");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}
	}

	/**
	 * <p>
	 * Some creation invariants.
	 * </p>
	 */
	@Test
	public final void testCreation() {
		Assert.assertFalse(
				"Two unlabelled ecolaws should be considered different",
				factory.createEcolaw().equals(factory.createEcolaw()));

		Assert.assertEquals(
				"Two ecolaws with the same label are equals at the beginning",
				factory.createEcolaw("test"), factory.createEcolaw("test"));
		Assert.assertNotSame(
				"Two ecolaws with the same label should be created "
						+ "as two different entities",
				factory.createEcolaw("test"), factory.createEcolaw("test"));
	}

	/**
	 * <p>
	 * Tests the creation of YOUNGEST ecolaw (see TR.WP1.2011.06: A formal
	 * translation of eco-laws into SPARQL).
	 * </p>
	 * 
	 * @throws Exception
	 *             Something went wrong
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testYOUNGEST() throws Exception {
		final PatternNameTerm field = factory.createPatternNameTerm("FIELD");
		final PatternNameTerm field2 = factory.createPatternNameTerm("FIELD2");

		final PropertyTerm pumpSource = factory.createPropertyTerm(lsaFactory
				.createPropertyName(new URI(
						"http://www.example.org/pump#source")));
		final PropertyTerm pumpTime = factory.createPropertyTerm(lsaFactory
				.createPropertyName(new URI(
						"http://www.example.org/pump#pump_time")));
		final PropertyTerm pumpDiffTime = factory.createPropertyTerm(lsaFactory
				.createPropertyName(new URI(
						"http://www.example.org/pump#diff_time")));

		final ValueTerm<LongValue> tVar = factory
				.<LongValue> createValueTerm("T");

		final Ecolaw ecol = factory
				.createEcolaw("YOUNGEST")
				.addReactant(
						factory.createReactant(field)
								.assign(pumpSource,
							factory.<LSAidValue> createListTermFromTerms(factory
									.<LSAidValue> createValueTerm("L")))
								.assign(pumpTime,
							factory.<LongValue> createListTermFromTerms(tVar)))
				.addReactant(
						factory.createReactant(field2)
								.clone(factory.createSDescTerm(field))
								.assign(pumpTime,
							factory.<LongValue> createListTermFromTerms(factory
												.<LongValue> createValueTerm(
														"T2",
														createGtFormula(tVar))))
								.assign(pumpDiffTime,
							factory.<LongValue> createListTermFromTerms(factory
								.<LongValue> createValueTerm("DT"))))
				.addProduct(
						factory.createProduct(field).clone(
								factory.createSDescTerm(field2)));

		Assert.assertNotNull(ecol);
		Assert.assertEquals("<YOUNGEST> "
				+ "?FIELD:[http://www.example.org/pump#source = (?L); "
				+ "http://www.example.org/pump#pump_time = (?T)] + "
				+ "?FIELD2:[clones ?FIELD.D; "
				+ "http://www.example.org/pump#pump_time = "
				+ "(?{T2 : ?T2 > ?T}); "
				+ "http://www.example.org/pump#diff_time = (?DT)] "
				+ "----> ?FIELD:[clones ?FIELD2.D]", ecol.toString());
	}

	/**
	 * <p>
	 * Tests the creation of BOND-PV ecolaw (see TR.WP1.2011.06: A formal
	 * translation of eco-laws into SPARQL).
	 * </p>
	 * 
	 * @throws Exception
	 *             Something went wrong
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testBONDPV() throws Exception {
		final PatternNameTerm target = factory.createPatternNameTerm("TARGET");
		final PatternNameTerm src = factory.createPatternNameTerm("SRC");
		final PatternNameTerm bondReq = factory
				.createPatternNameTerm("BOND-REQ");

		final PropertyTerm prop = factory.createPropertyTerm("PROP");
		final ListTerm<?> values = factory.createListTerm("VALUES");

		final PropertyTerm bondRequest = factory.createPropertyTerm(lsaFactory
				.createPropertyName(new URI(
						"http://www.example.org/bond#request")));
		final PropertyTerm b = factory.createPropertyTerm("B");
		final PropertyTerm sapereType = factory.createPropertyTerm(lsaFactory
				.createPropertyName(new URI(
						"http://www.example.org/sapere#type")));
		final ValueTerm<URIValue> bondRequestPV = factory
				.<URIValue> createTypedValueTerm(lsaFactory
						.createPropertyValue(new URI(
								"http://www.example.org/bond#request_pv")));
		final PropertyTerm bondProp = factory.createPropertyTerm(lsaFactory
				.createPropertyName(new URI(
						"http://www.example.org/bond#bond_prop")));
		final PropertyTerm tProp = factory.createPropertyTerm(lsaFactory
				.createPropertyName(new URI(
						"http://www.example.org/bond#target_prop")));
		final PropertyTerm tValues = factory.createPropertyTerm(lsaFactory
				.createPropertyName(new URI(
						"http://www.example.org/bond#target_value")));

		final Ecolaw ecol = factory
				.createEcolaw("BOND-PV")
				.addReactant(factory.createReactant(target).has(prop, values))
				.addReactant(
						factory.createReactant(src)
								.has(bondRequest,
							factory.createListTermFromTerms(bondReq))
								.hasNot(b,
							factory.createListTermFromTerms(target)))
				.addReactant(
						factory.createReactant(bondReq)
								.has(sapereType,
							factory.createListTermFromTerms(bondRequestPV))
								.assign(bondProp,
							factory.<URIValue> createListTermFromTerms(b
									.toValueTerm()))
								.assign(tProp,
							factory.<URIValue> createListTermFromTerms(prop
									.toValueTerm()))
								.assign(tValues, values))
				.addProduct(
						factory.createProduct(src).has(b,
								factory.createListTermFromTerms(target)))
				.addProduct(factory.createProduct(bondReq))
				.addProduct(factory.createProduct(target));

		Assert.assertNotNull(ecol);
		Assert.assertEquals("<BOND-PV> ?TARGET:[?PROP has ?VALUES] + "
				+ "?SRC:[http://www.example.org/bond#request has (?BOND-REQ); "
				+ "?B has-not (?TARGET)] + "
				+ "?BOND-REQ:[http://www.example.org/sapere#type has "
				+ "(http://www.example.org/bond#request_pv); "
				+ "http://www.example.org/bond#bond_prop = (?B); "
				+ "http://www.example.org/bond#target_prop = (?PROP); "
				+ "http://www.example.org/bond#target_value = ?VALUES] "
				+ "----> ?SRC:[?B has (?TARGET)] + ?BOND-REQ + ?TARGET",
				ecol.toString());
	}

	/**
	 * <p>
	 * Creates a Greater Than Formula.
	 * </p>
	 * 
	 * @param tVar
	 *            The second term of comparison (the one which should has a
	 *            minor value)
	 * @return The formula
	 */
	private Formula<LongValue> createGtFormula(
			final ValueTerm<LongValue> tVar) {
		return createFormulaFactory().<LongValue> createGtFormula("?T",
				new RightOperand<LongValue>() {

					@Override
					public LongValue rightOperand() {
						return tVar.getValue();
					}

				});
	}

	/**
	 * <p>
	 * Creates a dummy valid formula.
	 * </p>
	 * 
	 * @param <T>
	 *            Type of terms
	 * @return A simple formula (accepts everything)
	 */
	private <T> Formula<T> createValidFormula() {
		return createFormulaFactory().<T> createIsFormula("0",
				new RightOperand<T>() {

					@Override
					public T rightOperand() {
						return null;
					}
				});
	}
}
