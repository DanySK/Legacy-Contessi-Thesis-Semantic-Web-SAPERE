package it.apice.sapere.api.lsas.values;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class test Property Value for a specific type: Boolean.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestBooleanValue extends
		AbstractTestPropertyValue<Boolean, BooleanValue> {

	@Override
	protected final List<Boolean> createValues() {
		final List<Boolean> res = new ArrayList<Boolean>(2);

		res.add(Boolean.TRUE);
		res.add(Boolean.FALSE);

		return res;
	}

	@Override
	protected final PropertyValue<Boolean, BooleanValue> createPropertyValue(
			final Boolean val) {
		return createFactory().createPropertyValue(val);
	}

}
