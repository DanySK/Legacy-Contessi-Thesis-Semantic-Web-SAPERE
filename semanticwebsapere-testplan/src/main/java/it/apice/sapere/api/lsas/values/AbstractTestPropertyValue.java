package it.apice.sapere.api.lsas.values;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.apice.sapere.api.AbstractLSAModelTest;
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
 * @param <CompType>
 *            The interface for comparison
 */
public abstract class AbstractTestPropertyValue<ValueType 
		extends Comparable<ValueType>, CompType>
		extends AbstractLSAModelTest {

	/**
	 * <p>
	 * Tests the correctness of a PropertyValue.
	 * </p>
	 */
	@Test
	public final void testPropertyValue() {
		final List<ValueType> vals = createValues();
		final List<PropertyValue<ValueType, CompType>> pvalues = 
				createPropertyValues(vals);

		assertTrue(
				"Why values and property values cardinalities are not equal?",
				vals.size() == pvalues.size());

		for (int idx = 0; idx < vals.size(); idx++) {
			final PropertyValue<ValueType, CompType> pval = pvalues.get(idx);
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

			if (vals.size() > 1) {
				final PropertyValue<ValueType, CompType> other = pvalues
						.get((idx + 1) % vals.size());
				assertFalse(pval.equals(other));

				// // Why hashcode of 0 and -1 are equals? the same with 1 and
				// -2,
				// MIN and MAX
				// System.err.println(new Long(1).hashCode());
				// System.err.println(new Long(0).hashCode());
				// System.err.println(new Long(-1).hashCode());
				// System.err.println(new Long(-2).hashCode() + "\n");
				// System.err.println(new Long(Long.MAX_VALUE).hashCode());
				// System.err.println(new Long(Long.MIN_VALUE).hashCode());
				assertFalse("hashcode clash: idx=" + idx,
						pval.hashCode() == other.hashCode());
			}
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
	 *            ValuesList from which Property ValuesList should be generated
	 * @return A list of Property ValuesList
	 */
	private List<PropertyValue<ValueType, CompType>> createPropertyValues(
			final List<ValueType> vals) {
		final List<PropertyValue<ValueType, CompType>> res = 
				new ArrayList<PropertyValue<ValueType, CompType>>(
				vals.size());

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
	protected abstract PropertyValue<ValueType, CompType> createPropertyValue(
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
