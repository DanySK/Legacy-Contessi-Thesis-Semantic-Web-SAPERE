package it.apice.sapere.api.lsas.values;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.apice.sapere.api.AbstractModelTest;
import it.apice.sapere.api.lsas.LSAid;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * <p>
 * Test case for PropertyValue entity.
 * </p>
 * <p>
 * THIS CLASS SHOULD NOT BE DIRECTLY EXTENDED
 * </p>
 * 
 * @author Paolo Contessi
 * @param <ValueType>
 *            The type that will be contained in the tested PropertyValue
 */
public abstract class AbstractTestPropertyValue<ValueType> extends
		AbstractModelTest {

	/**
	 * <p>
	 * Tests the correctness of a PropertyValue.
	 * </p>
	 */
	@Test
	public final void testPropertyValue() {
		final List<ValueType> vals = createValues();
		final List<PropertyValue<ValueType>> pvalues = 
				createPropertyValues(vals);

		assertTrue(
				"Why values and property values cardinalities are not equal?",
				vals.size() == pvalues.size());

		for (int idx = 0; idx < vals.size(); idx++) {
			final PropertyValue<ValueType> pval = pvalues.get(idx);
			final ValueType val = vals.get(idx);
			assertEquals("Stored value should be equal to provided one",
					pval.getValue(), val);
			if (pval.hasLocale()) {
				assertNotNull("Should have a language code "
						+ "(according to hasLocale()), why not?",
						pval.getLanguageCode());
			}

			assertTrue(pval.isBoolean() == (val instanceof Boolean));
			assertTrue(pval.isLiteral() == (val instanceof String));
			assertTrue(pval.isLSAId() == (val instanceof LSAid));
			assertTrue(pval.isNumber() == (val instanceof Integer
					|| val instanceof Long || val instanceof Float 
					|| val instanceof Double));
			assertTrue(pval.isURI() == (val instanceof URI));

			assertEquals(pval, pval);
			assertTrue(pval.hashCode() == pval.hashCode());

			final PropertyValue<ValueType> other = pvalues.get((idx + 1)
					% vals.size());
			assertFalse(pval.equals(other));
			assertFalse(pval.hashCode() == other.hashCode());
		}
	}

	/**
	 * <p>
	 * Creates a list of Property Values, generated from the provided list of
	 * values.
	 * </p>
	 * <p>
	 * Please respect the order.
	 * </p>
	 * 
	 * @param vals
	 *            Values from which Property Values should be generated
	 * @return A list of Property Values
	 */
	private List<PropertyValue<ValueType>> createPropertyValues(
			final List<ValueType> vals) {
		final List<PropertyValue<ValueType>> res = 
				new ArrayList<PropertyValue<ValueType>>(vals.size());

		for (ValueType val : vals) {
			res.add(createPropertyValue(val));
		}

		return res;
	}

	/**
	 * <p>
	 * Should create a Property Value from the provided value.
	 * </p>
	 * 
	 * @param val
	 *            The value
	 * @return The Property VAlue
	 */
	protected abstract PropertyValue<ValueType> createPropertyValue(
			ValueType val);

	/**
	 * <p>
	 * Should create a list of values of type <code>ValueType</code>.
	 * </p>
	 * 
	 * @return A List of values
	 */
	protected abstract List<ValueType> createValues();
}
