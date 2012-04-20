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
		AbstractTestPropertyValue<Long, LongValue> {

	@Override
	protected final List<Long> createValues() {
		final List<Long> res = new ArrayList<Long>(5);

		res.add(Long.MAX_VALUE);
		res.add(1L);
		res.add(0L);
		res.add((-1L - 1L));
		res.add(Long.MIN_VALUE + 1);

		return res;
	}

	@Override
	protected final PropertyValue<Long, LongValue> createPropertyValue(
			final Long val) {
		return createFactory().createPropertyValue(val);
	}

}
