package it.apice.sapere.api.lsas.values;

import it.apice.sapere.api.SAPEREFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class test Property Value for a specific type: Float.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestFloatValue extends
		AbstractTestPropertyValue<Float> {

	/** Reference to SAPERE API Factory. */
	private final transient SAPEREFactory factory = createFactory();

	@Override
	protected final List<Float> createValues() {
		final List<Float> res = new ArrayList<Float>(6);

		res.add(Float.MAX_VALUE);
		res.add(1.0f);
		res.add(Float.MIN_NORMAL);
		res.add(0.0f);
		res.add(-1.0f);
		res.add(Float.MIN_VALUE);

		return res;
	}

	@Override
	protected final PropertyValue<Float> createPropertyValue(final Float val) {
		return factory.createPropertyValue(val);
	}

}
