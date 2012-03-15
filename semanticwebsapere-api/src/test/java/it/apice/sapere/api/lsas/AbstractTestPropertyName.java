package it.apice.sapere.api.lsas;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import it.apice.sapere.api.AbstractModelTest;
import it.apice.sapere.api.SAPEREFactory;

import org.junit.Test;

/**
 * <p>
 * Test case for PropertyName entity.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestPropertyName extends AbstractModelTest {

	/** Reference to a SAPERE Factory. */
	private final transient SAPEREFactory factory = createFactory();

	/**
	 * <p>
	 * Tests Property Name.
	 * </p>
	 * 
	 * @throws Exception
	 *             Something went wrong
	 */
	@Test
	public final void testPropertyName() throws Exception {
		final URI propName = new URI("http://localhost:8080/sapere#prop");
		final PropertyName prop = factory.createPropertyName(propName);
		final PropertyName propB = factory.createPropertyName(propName);
		final URI prop2Name = new URI("http://localhost:8080/sapere#prop2");
		final PropertyName prop2 = factory.createPropertyName(prop2Name);

		assertTrue(prop.equals(propB));
		assertTrue(prop.hashCode() == propB.hashCode());

		assertFalse(prop.equals(prop2));
		assertFalse(prop.hashCode() == prop2.hashCode());

		assertFalse(propB.equals(prop2));
		assertFalse(propB.hashCode() == prop2.hashCode());
	}
}
