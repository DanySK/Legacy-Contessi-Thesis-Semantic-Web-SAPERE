package it.apice.sapere.api.lsas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import it.apice.sapere.api.AbstractModelTest;
import it.apice.sapere.api.SAPEREFactory;

import java.net.URI;

import org.junit.Test;

/**
 * <p>
 * Test case for SemanticDescription entity.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestSemanticDescription 
		extends AbstractModelTest {

	/** Reference to a SAPERE Factory. */
	private final transient SAPEREFactory factory = createFactory();

	/**
	 * <p>
	 * Test SemanticDescription.
	 * </p>
	 * 
	 * @throws Exception
	 *             Something went wrong
	 */
	@Test
	public final void testSemanticDescription() throws Exception {
		final SemanticDescription sd = factory.createLSA()
				.getSemanticDescription();
		final SemanticDescription clonedSD = sd.clone();
		assertEquals(sd, clonedSD);
		assertTrue(clonedSD.isCloneOf(sd));
		assertTrue(clonedSD.isExtensionOf(sd));
		assertTrue(sd.isExtensionOf(clonedSD));

		final URI propName = new URI("http://localhost:8080/sapere#prop");
		sd.addProperty(factory.createProperty(propName));

		assertTrue(clonedSD.isCloneOf(sd));
		assertTrue(clonedSD.isExtensionOf(sd));
		assertTrue(sd.isExtensionOf(clonedSD));

		sd.get(factory.createPropertyName(propName)).addValue(
				factory.createPropertyValue(false));

		assertFalse(sd.equals(clonedSD));
		assertFalse(clonedSD.isCloneOf(sd));
		assertFalse(clonedSD.isExtensionOf(sd));
		assertTrue(sd.isExtensionOf(clonedSD));
	}
}
