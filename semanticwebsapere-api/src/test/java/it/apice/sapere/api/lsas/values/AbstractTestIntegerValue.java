package it.apice.sapere.api.lsas.values;

import it.apice.sapere.api.SAPEREFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class test Property Value for a specific type: Integer.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestIntegerValue extends
		AbstractTestPropertyValue<Integer> {

	/** Reference to SAPERE API Factory. */
	private final transient SAPEREFactory factory = createFactory();

	@Override
	protected final List<Integer> createValues() {
		final List<Integer> res = new ArrayList<Integer>(5);

		res.add(Integer.MAX_VALUE);
		res.add(1);
		res.add(0);
		res.add(-1);
		res.add(Integer.MIN_VALUE);

		return res;
	}

	@Override
	protected final PropertyValue<Integer> createPropertyValue(
			final Integer val) {
		return factory.createPropertyValue(val);
	}

}
