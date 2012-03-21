package it.apice.sapere.api.lsas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.apice.sapere.api.AbstractModelTest;

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
		final SemanticDescription sd = createFactory().createLSA()
				.getSemanticDescription();
		SemanticDescription clonedSD = sd.clone();

		assertEquals(sd, clonedSD);
		assertTrue(sd.isCloneOf(clonedSD));
		assertTrue(clonedSD.isCloneOf(sd));
		assertTrue(clonedSD.isExtensionOf(sd));
		assertTrue(sd.isExtensionOf(clonedSD));

		final URI propName = new URI("http://localhost:8080/sapere#prop");
		sd.addProperty(createFactory().createProperty(propName));
		clonedSD = sd.clone();

		assertEquals(sd, clonedSD);
		assertTrue(clonedSD.isCloneOf(sd));
		assertTrue(sd.isCloneOf(clonedSD));
		assertTrue(clonedSD.isExtensionOf(sd));
		assertTrue(sd.isExtensionOf(clonedSD));

		sd.get(createFactory().createPropertyName(propName)).addValue(
				createFactory().createPropertyValue(false));

		assertFalse(sd.equals(clonedSD));
		assertFalse(clonedSD.isCloneOf(sd));
		assertFalse(sd.isCloneOf(clonedSD));
		assertFalse(clonedSD.isExtensionOf(sd));
		assertTrue(sd.isExtensionOf(clonedSD));
	}
}
