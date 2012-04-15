package it.apice.sapere.api.lsas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.apice.sapere.api.AbstractLSAModelTest;
import it.apice.sapere.api.lsas.values.PropertyValue;

import java.net.URI;

import org.junit.Test;

/**
 * <p>
 * Test case for Property entity.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestProperty extends AbstractLSAModelTest {

	/**
	 * <p>
	 * Test Property with no initial values.
	 * </p>
	 * 
	 * @throws Exception
	 *             Something went wrong
	 */
	@Test
	public final void testEmptyProperty() throws Exception {
		final URI propName = new URI("http://localhost:8080/sapere#prop");
		final Property prop = createFactory().createProperty(propName);

		assertNotNull("Why no property has been created?", prop);
		assertTrue("Property should have no values", prop.values().length == 0);

		final PropertyValue<?> val = createFactory().createPropertyValue(true);
		assertFalse(prop.hasValue(val));
		assertTrue(prop.addValue(val).hasValue(val));

		final PropertyValue<?> val2 = createFactory()
				.createPropertyValue(false);
		prop.changeValue(val, val2);
		assertFalse(prop.hasValue(val));
		assertTrue(prop.hasValue(val2));

		assertTrue(prop.removeValue(val2).values().length == 0);
	}

	/**
	 * <p>
	 * Test Property with initial values.
	 * </p>
	 * 
	 * @throws Exception
	 *             Something went wrong
	 */
	@Test
	public final void testProperty() throws Exception {
		final URI propName = new URI("http://localhost:8080/sapere#prop");
		final PropertyValue<?> val = createFactory().createPropertyValue(true);
		final Property prop = createFactory().createProperty(propName, val);

		assertNotNull("Why no property has been created?", prop);
		assertTrue("Property should have no values", prop.values().length == 1);
		assertTrue(prop.hasValue(val));

		final PropertyValue<?> val2 = createFactory()
				.createPropertyValue(false);
		prop.changeValue(val, val2);
		assertFalse(prop.hasValue(val));
		assertTrue(prop.hasValue(val2));
		prop.addValue(val);
		assertTrue(prop.hasValue(val));

		final URI prop2Name = new URI("http://localhost:8080/sapere#prop2");
		final Property prop2 = createFactory()
				.createProperty(prop2Name, val, val2);
		assertTrue(prop.values().length == 2);
		assertFalse(
				"Two properties should be equals only if their name are equal",
				prop2.equals(prop));

		assertTrue(prop.removeValue(val).removeValue(val2)
				.values().length == 0);

		final Property clonedProp = prop2.clone();
		assertEquals(clonedProp, prop2);
	}

}
