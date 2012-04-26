package it.apice.sapere.api.ecolaws;

import it.apice.sapere.api.EcolawFactory;
import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.ecolaws.compilers.EcolawToSPARQL;
import it.apice.sapere.api.ecolaws.formulas.FormulaFactory;
import it.apice.sapere.api.ecolaws.formulas.RightOperand;
import it.apice.sapere.api.ecolaws.formulas.impl.FormulaFactoryImpl;
import it.apice.sapere.api.ecolaws.terms.Formula;
import it.apice.sapere.api.ecolaws.terms.ListTerm;
import it.apice.sapere.api.ecolaws.terms.PatternNameTerm;
import it.apice.sapere.api.ecolaws.terms.PropertyTerm;
import it.apice.sapere.api.ecolaws.terms.ValueTerm;
import it.apice.sapere.api.impl.EcolawFactoryImpl;
import it.apice.sapere.api.impl.LSAFactoryImpl;
import it.apice.sapere.api.lsas.values.LSAidValue;
import it.apice.sapere.api.lsas.values.LongValue;
import it.apice.sapere.api.lsas.values.URIValue;

import java.net.URI;

/**
 * <p>
 * Visual EcolawToSPARQL tester.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public final class TestEcolawToSPARQL {

	/** Ecolaw Factory. */
	private static final transient EcolawFactory FACTORY = 
			new EcolawFactoryImpl();

	/** LSA Factory. */
	private static final transient LSAFactory LSA_FACTORY = 
			new LSAFactoryImpl();

	/** Formula Factory. */
	private static final transient FormulaFactory F_FORMULA = 
			new FormulaFactoryImpl();

	/**
	 * <p>
	 * Dummy constructor.
	 * </p>
	 */
	private TestEcolawToSPARQL() {

	}

	/**
	 * <p>
	 * Entry point.
	 * </p>
	 * 
	 * @param args
	 *            Not used
	 * @throws Exception
	 *             Test failed
	 */
	public static void main(final String[] args) throws Exception {
		testYOUNGEST();
		testBONDPV();
	}

	/**
	 * <p>
	 * Tests YOUNGEST eco-law translation.
	 * </p>
	 * 
	 * @throws Exception
	 *             Test failed
	 */
	private static void testYOUNGEST() throws Exception {
		System.out.println("Translating [YOUNGEST]...");
		final Ecolaw law = createYOUNGEST();
		System.out.println(EcolawToSPARQL.convert(law));
		System.out.println("\n");
	}

	/**
	 * <p>
	 * Creates the YOUNGEST eco-law.
	 * </p>
	 * 
	 * @return The YOUNGEST eco-law
	 * @throws Exception
	 *             Test failed
	 */
	@SuppressWarnings("unchecked")
	private static Ecolaw createYOUNGEST() throws Exception {
		final PatternNameTerm field = FACTORY.createPatternNameTerm("FIELD");
		final PatternNameTerm field2 = FACTORY.createPatternNameTerm("FIELD2");

		final PropertyTerm pumpSource = FACTORY.createPropertyTerm(LSA_FACTORY
				.createPropertyName(new URI(
						"http://www.example.org/pump#source")));
		final PropertyTerm pumpTime = FACTORY.createPropertyTerm(LSA_FACTORY
				.createPropertyName(new URI(
						"http://www.example.org/pump#pump_time")));
		final PropertyTerm pumpDiffTime = FACTORY
				.createPropertyTerm(LSA_FACTORY.createPropertyName(new URI(
						"http://www.example.org/pump#diff_time")));

		final ValueTerm<LongValue> tVar = FACTORY
				.<LongValue> createValueTerm("T");

		return FACTORY
				.createEcolaw("YOUNGEST")
				.addReactant(
						FACTORY.createReactant(field)
								.assign(pumpSource,
							FACTORY.<LSAidValue> createListTermFromTerms(FACTORY
									.<LSAidValue> createValueTerm("L")))
								.assign(pumpTime,
							FACTORY.<LongValue> createListTermFromTerms(tVar)))
				.addReactant(
						FACTORY.createReactant(field2)
								.clone(FACTORY.createSDescTerm(field))
								.assign(pumpTime,
							FACTORY.<LongValue> createListTermFromTerms(FACTORY
									.<LongValue> createValueTerm(
										"T2",
										createGtFormula(tVar))))
								.assign(pumpDiffTime,
							FACTORY.<LongValue> createListTermFromTerms(FACTORY
									.<LongValue> createValueTerm("DT"))))
				.addProduct(
						FACTORY.createProduct(field).clone(
								FACTORY.createSDescTerm(field2)));
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
	private static Formula<LongValue> createGtFormula(
			final ValueTerm<LongValue> tVar) {
		return F_FORMULA.<LongValue> createGtFormula("?T",
				new RightOperand<LongValue>() {

					@Override
					public LongValue rightOperand() {
						return tVar.getValue();
					}

				});
	}

	/**
	 * <p>
	 * Tests BOND-PV eco-law translation.
	 * </p>
	 * 
	 * @throws Exception
	 *             Test failed
	 */
	private static void testBONDPV() throws Exception {
		System.out.println("Translating [BOND-PV]...");
		final Ecolaw law = createBONDPV();
		System.out.println(EcolawToSPARQL.convert(law));
		System.out.println("\n");
	}

	/**
	 * <p>
	 * Creates the BOND-PV eco-law.
	 * </p>
	 * 
	 * @return The BOND-PV eco-law
	 * @throws Exception
	 *             Test failed
	 */
	@SuppressWarnings("unchecked")
	private static Ecolaw createBONDPV() throws Exception {
		final PatternNameTerm target = FACTORY.createPatternNameTerm("TARGET");
		final PatternNameTerm src = FACTORY.createPatternNameTerm("SRC");
		final PatternNameTerm bondReq = FACTORY
				.createPatternNameTerm("BOND-REQ");

		final PropertyTerm prop = FACTORY.createPropertyTerm("PROP");
		final ListTerm<?> values = FACTORY.createListTerm("VALUES");

		final PropertyTerm bondRequest = FACTORY.createPropertyTerm(LSA_FACTORY
				.createPropertyName(new URI(
						"http://www.example.org/bond#request")));
		final PropertyTerm b = FACTORY.createPropertyTerm("B");
		final PropertyTerm sapereType = FACTORY.createPropertyTerm(LSA_FACTORY
				.createPropertyName(new URI(
						"http://www.example.org/sapere#type")));
		final ValueTerm<URIValue> bondRequestPV = FACTORY
				.<URIValue> createTypedValueTerm(LSA_FACTORY
						.createPropertyValue(new URI(
								"http://www.example.org/bond#request_pv")));
		final PropertyTerm bondProp = FACTORY.createPropertyTerm(LSA_FACTORY
				.createPropertyName(new URI(
						"http://www.example.org/bond#bond_prop")));
		final PropertyTerm tProp = FACTORY.createPropertyTerm(LSA_FACTORY
				.createPropertyName(new URI(
						"http://www.example.org/bond#target_prop")));
		final PropertyTerm tValues = FACTORY.createPropertyTerm(LSA_FACTORY
				.createPropertyName(new URI(
						"http://www.example.org/bond#target_value")));

		return FACTORY
				.createEcolaw("BOND-PV")
				.addReactant(FACTORY.createReactant(target).has(prop, values))
				.addReactant(
						FACTORY.createReactant(src)
								.has(bondRequest,
							FACTORY.createListTermFromTerms(bondReq))
								.hasNot(b,
							FACTORY.createListTermFromTerms(target)))
				.addReactant(
						FACTORY.createReactant(bondReq)
								.has(sapereType,
							FACTORY.createListTermFromTerms(bondRequestPV))
								.assign(bondProp,
							FACTORY.<URIValue> createListTermFromTerms(b
									.toValueTerm()))
								.assign(tProp,
							FACTORY.<URIValue> createListTermFromTerms(prop
									.toValueTerm()))
								.assign(tValues, values))
				.addProduct(
						FACTORY.createProduct(src).has(b,
								FACTORY.createListTermFromTerms(target)))
				.addProduct(FACTORY.createProduct(bondReq))
				.addProduct(FACTORY.createProduct(target));
	}
}
