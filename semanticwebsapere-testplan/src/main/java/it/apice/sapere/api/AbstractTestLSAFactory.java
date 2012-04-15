package it.apice.sapere.api;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.lsas.values.PropertyValue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.junit.Test;

/**
 * <p>
 * Test case for LSAFactory entity.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestLSAFactory extends AbstractLSAModelTest {

	/** Reference to an LSA Factory. */
	private final transient LSAFactory factory = createFactory();

	/**
	 * Checks that unwanted behaviours are handled.
	 */
	@Test
	public final void testUnwanted() {
		try {
			factory.createProperty(null);
			fail("Unwanted Property(null)");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			factory.createProperty(createValidURI(), new PropertyValue<?>[] {});
			fail("Unwanted Property(ok, empty)");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			factory.createProperty(createValidURI(),
					new PropertyValue<?>[] { null });
			fail("Unwanted Property(ok, {null, ..., null})");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			factory.createPropertyName((URI) null);
			fail("Unwanted PropertyName((URI) null)");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			factory.createPropertyValue((Date) null);
			fail("Unwanted PropertyName((Date) null)");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			factory.createPropertyValue((String) null);
			fail("Unwanted PropertyName((String) null)");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			factory.createPropertyValue((LSAid) null);
			fail("Unwanted PropertyName((LSAid) null)");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			factory.createPropertyValue(null, createValidLanguageCode());
			fail("Unwanted PropertyValue(null, ok)");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			factory.createPropertyValue(createValidStringValue(), "");
			fail("Unwanted PropertyValue(ok, empty)");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			factory.createPropertyValue(createValidStringValue2(), null);
			fail("Unwanted PropertyValue(ok, null)");
		} catch (Exception ex) {
			assertTrue(true);
		}
	}

	/**
	 * <p>
	 * Creates a valid String value.
	 * </p>
	 * 
	 * @return The empty String
	 */
	protected final String createValidStringValue() {
		return "";
	}

	/**
	 * <p>
	 * Creates a valid String value.
	 * </p>
	 * 
	 * @return A String (not null)
	 */
	protected final String createValidStringValue2() {
		return "literal";
	}

	/**
	 * <p>
	 * Creates a valid Language Code.
	 * </p>
	 * 
	 * @return A Language Code (not null, not empty string)
	 */
	protected final String createValidLanguageCode() {
		return "en";
	}

	/**
	 * <p>
	 * Creates a valid URI.
	 * </p>
	 * 
	 * @return An URI
	 */
	protected final URI createValidURI() {
		try {
			return new URI("http://localhost:8080/sapere#uri");
		} catch (URISyntaxException e) {
			return null;
		}
	}
}
