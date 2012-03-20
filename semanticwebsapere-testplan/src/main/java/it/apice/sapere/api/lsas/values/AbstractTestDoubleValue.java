package it.apice.sapere.api.lsas.values;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class test Property Value for a specific type: Double.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestDoubleValue extends
		AbstractTestPropertyValue<Double> {

	@Override
	protected final List<Double> createValues() {
		final List<Double> res = new ArrayList<Double>(6);

		res.add(Double.MAX_VALUE);
		res.add(1.0);
		res.add(Double.MIN_NORMAL);
		res.add(0.0);
		res.add(-1.0);
		res.add(Double.MIN_VALUE);

		return res;
	}

	@Override
	protected final PropertyValue<Double> createPropertyValue(
			final Double val) {
		return createFactory().createPropertyValue(val);
	}

}
