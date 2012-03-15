package it.apice.sapere.api.lsas.values;

import it.apice.sapere.api.SAPEREFactory;

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
		AbstractTestPropertyValue<Boolean> {

	/** Reference to SAPERE API Factory. */
	private final transient SAPEREFactory factory = createFactory();

	@Override
	protected final List<Boolean> createValues() {
		final List<Boolean> res = new ArrayList<Boolean>(2);

		res.add(Boolean.TRUE);
		res.add(Boolean.FALSE);

		return res;
	}

	@Override
	protected final PropertyValue<Boolean> createPropertyValue(
			final Boolean val) {
		return factory.createPropertyValue(val);
	}

}
