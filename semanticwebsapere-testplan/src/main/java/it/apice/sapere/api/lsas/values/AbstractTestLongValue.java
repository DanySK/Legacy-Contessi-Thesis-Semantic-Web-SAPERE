package it.apice.sapere.api.lsas.values;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class test Property Value for a specific type: Long.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestLongValue extends
		AbstractTestPropertyValue<Long> {

	@Override
	protected final List<Long> createValues() {
		final List<Long> res = new ArrayList<Long>(5);

		res.add(Long.MAX_VALUE);
		res.add(1L);
		res.add(0L);
		res.add(-2L);
		res.add(Long.MIN_VALUE + 1);

		return res;
	}

	@Override
	protected final PropertyValue<Long> createPropertyValue(
			final Long val) {
		return createFactory().createPropertyValue(val);
	}

}
