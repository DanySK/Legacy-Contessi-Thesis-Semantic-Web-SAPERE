package it.apice.sapere.api;

import static org.junit.Assert.assertEquals;
import it.apice.sapere.api.lsas.LSA;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

/**
 * <p>
 * Test case for SAPEREParser entity.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestSAPEREParser extends AbstractModelTest {

	/** Constant: SAPERE RDF prefix. */
	private static final transient String SAPERE_PREFIX = "http://"
			+ "www.sapere-project.eu/ontologies/2012/0/sapere-model.owl#";

	/** Constant: FOAF RDF prefix. */
	private static final transient String FOAF_PREFIX = "http://"
			+ "xmlns.com/foaf/0.1/";

	/** Constant: EX RDF prefix. */
	private static final transient String EX_PREFIX = "http://www.example.org#";

	/** Constant: sapere:lsa1432 sapere:time year. */
	private static final transient int LSA1432_TIME_YEAR = 2011;

	/** Constant: sapere:lsa1432 sapere:time month. */
	private static final transient int LSA1432_TIME_MONTH = 4;

	/** Constant: sapere:lsa1432 sapere:time day. */
	private static final transient int LSA1432_TIME_DAY = 31;

	/** Constant: sapere:lsa1432 sapere:time hour. */
	private static final transient int LSA1432_TIME_HOUR = 12;

	/** Constant: sapere:lsa1432 sapere:time minutes. */
	private static final transient int LSA1432_TIME_MIN = 45;

	/** Constant: sapere:lsa1432 sapere:time seconds. */
	private static final transient int LSA1432_TIME_SEC = 39;

	/** Constant: sapere:lsa1432 foaf:age. */
	private static final transient int LSA1432_AGE = 20;

	/** Constant: sapere:lsa2543 sapere:time year. */
	private static final transient int LSA2543_TIME_YEAR = 2010;

	/** Constant: sapere:lsa2543 sapere:time month. */
	private static final transient int LSA2543_TIME_MONTH = 4;

	/** Constant: sapere:lsa2543 sapere:time day. */
	private static final transient int LSA2543_TIME_DAY = 31;

	/** Constant: sapere:lsa2543 sapere:time hour. */
	private static final transient int LSA2543_TIME_HOUR = 18;

	/** Constant: sapere:lsa2543 sapere:time minutes. */
	private static final transient int LSA2543_TIME_MIN = 5;

	/** Constant: sapere:lsa2543 sapere:time seconds. */
	private static final transient int LSA2543_TIME_SEC = 0;

	/** Constant: sapere:lsa2543 foaf:age. */
	private static final transient int LSA2543_AGE = 25;

	/**
	 * <p>
	 * Should create a new instance of the Parser.
	 * </p>
	 * 
	 * @param fact
	 *            A reference to the ExtSAPEREFactory that the parser will use
	 *            to generate model's entities
	 * @return An instance of the SAPEREParser
	 */
	protected abstract SAPEREParser createParser(ExtSAPEREFactory fact);

	/**
	 * <p>
	 * Tests the parser.
	 * </p>
	 * 
	 * @throws Exception
	 *             Something went wrong
	 */
	@Test
	public final void testSAPEREParser() throws Exception {
		final SAPEREParser parser = createParser(createExtFactory());
		final Set<LSA> model = createModelForTest();

		System.out.println("Checking from InputStream..");
		System.out.println("==========================================");

		for (LSA lsa : model) {
			for (LSA pLsa : parser.parseLSAs(getInputAsStream())) {
				if (lsa.getLSAId().equals(pLsa.getLSAId())) {
					System.out.println(lsa);
					System.err.println(pLsa);
					System.out.println("==========================================");
					assertEquals(lsa, pLsa);
				}
			}
		}
		
		System.out.println("Checking from Reader..");
		System.out.println("==========================================");
		
		for (LSA lsa : model) {
			for (LSA pLsa : parser.parseLSAs(getInputAsReader())) {
				if (lsa.getLSAId().equals(pLsa.getLSAId())) {
					System.err.println(pLsa);
					System.out.println(lsa);
					System.out.println("==========================================");
					assertEquals(lsa, pLsa);
				}
			}
		}
		
		System.out.println("Checking from String..");
		System.out.println("==========================================");
		
		for (LSA lsa : model) {
			for (LSA pLsa : parser.parseLSAs(getInputAsString())) {
				if (lsa.getLSAId().equals(pLsa.getLSAId())) {
					System.err.println(pLsa);
					System.out.println(lsa);
					System.out.println("==========================================");
					assertEquals(lsa, pLsa);
				}
			}
		}
	}

	/**
	 * <p>
	 * Creates the model for comparison with the parsed one.
	 * </p>
	 * 
	 * @return The model = 2 LSAs
	 * @throws Exception
	 *             Something went wrong
	 */
	private Set<LSA> createModelForTest() throws Exception {
		final Set<LSA> res = new HashSet<LSA>();

		res.add(createLSA1432());
		res.add(createLSA2543());

		return res;
	}

	/**
	 * <p>
	 * Creates sapere:lsa2543.
	 * </p>
	 * 
	 * @return The created LSA
	 * @throws Exception
	 *             Something went wrong
	 */
	private LSA createLSA2543() throws Exception {
		final LSA lsa = createExtFactory().createLSA(
				createExtFactory().createLSAid(sapereURI("lsa2543")));

		Calendar cal = Calendar.getInstance();
		cal.set(LSA2543_TIME_YEAR, LSA2543_TIME_MONTH, LSA2543_TIME_DAY,
				LSA2543_TIME_HOUR, LSA2543_TIME_MIN, LSA2543_TIME_SEC);
		cal.set(Calendar.MILLISECOND, 0);
		lsa.getSemanticDescription()
				.addProperty(
						createFactory().createProperty(
								sapereURI("type"),
								createFactory().createPropertyValue(
										sapereURI("person"))))
				.addProperty(
						createFactory().createProperty(
								sapereURI("time"),
								createFactory().createPropertyValue(
										cal.getTime())))
				.addProperty(
						createFactory().createProperty(
								foafURI("age"),
								createFactory()
										.createPropertyValue(LSA2543_AGE)));
		return lsa;
	}

	/**
	 * <p>
	 * Creates sapere:lsa1432.
	 * </p>
	 * 
	 * @return The created LSA
	 * @throws Exception
	 *             Something went wrong
	 */
	private LSA createLSA1432() throws Exception {
		final LSA lsa = createExtFactory()
				.createLSA(
						createExtFactory()
								.createLSAid(
										new URI(
												"http://www.sapere-project.eu/"
														+ "ontologies/2012/0/sapere-model.owl#lsa1432")));

		Calendar cal = Calendar.getInstance();
		cal.set(LSA1432_TIME_YEAR, LSA1432_TIME_MONTH, LSA1432_TIME_DAY,
				LSA1432_TIME_HOUR, LSA1432_TIME_MIN, LSA1432_TIME_SEC);
		cal.set(Calendar.MILLISECOND, 0);
		lsa.getSemanticDescription()
				.addProperty(
						createFactory().createProperty(
								sapereURI("type"),
								createFactory().createPropertyValue(
										sapereURI("person"))))
				.addProperty(
						createFactory().createProperty(
								sapereURI("time"),
								createFactory().createPropertyValue(
										cal.getTime())))
				.addProperty(
						createFactory().createProperty(
								foafURI("age"),
								createFactory()
										.createPropertyValue(LSA1432_AGE)))
				.addProperty(
						createFactory().createProperty(
								exURI("interest"),
								createFactory().createPropertyValue("music"),
								createFactory().createPropertyValue("sport"),
								createFactory().createPropertyValue(
										"travelling")));
		return lsa;
	}

	/**
	 * <p>
	 * Creates a URI for an entity in the SAPERE namespace.
	 * </p>
	 * 
	 * @param fragment
	 *            local-id
	 * @return New URI
	 * @throws Exception
	 *             Something went wrong
	 */
	private URI sapereURI(final String fragment) throws Exception {
		return new URI(SAPERE_PREFIX + fragment);
	}

	/**
	 * <p>
	 * Creates a URI for an entity in the FOAF namespace.
	 * </p>
	 * 
	 * @param fragment
	 *            local-id
	 * @return New URI
	 * @throws Exception
	 *             Something went wrong
	 */
	private URI foafURI(final String fragment) throws Exception {
		return new URI(FOAF_PREFIX + fragment);
	}

	/**
	 * <p>
	 * Creates a URI for an entity in the EX namespace.
	 * </p>
	 * 
	 * @param fragment
	 *            local-id
	 * @return New URI
	 * @throws Exception
	 *             Something went wrong
	 */
	private URI exURI(final String fragment) throws Exception {
		return new URI(EX_PREFIX + fragment);
	}

	/**
	 * <p>
	 * Gets the input in the form of a String.
	 * </p>
	 * 
	 * @return The input for the parser
	 * @throws Exception
	 *             Cannot produce input
	 */
	private String getInputAsString() throws Exception {
		final BufferedReader reader = new BufferedReader(getInputAsReader());
		final StringBuilder stringBuilder = new StringBuilder();
		final String ls = System.getProperty("line.separator");
		String line = null;
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}

		return stringBuilder.toString();
	}

	/**
	 * <p>
	 * Gets the input in the form of a Reader.
	 * </p>
	 * 
	 * @return The input for the parser
	 * @throws Exception
	 *             Cannot produce input
	 */
	private Reader getInputAsReader() throws Exception {
		return new InputStreamReader(getInputAsStream());
	}

	/**
	 * <p>
	 * Gets the input in the form of an InputStream.
	 * </p>
	 * 
	 * @return The input for the parser
	 * @throws Exception
	 *             Cannot produce input
	 */
	private InputStream getInputAsStream() throws Exception {
		return AbstractTestSAPEREParser.class.getClassLoader()
				.getResourceAsStream(
						"it/apice/sapere/api/parser-test-turtle.txt");
	}
}
