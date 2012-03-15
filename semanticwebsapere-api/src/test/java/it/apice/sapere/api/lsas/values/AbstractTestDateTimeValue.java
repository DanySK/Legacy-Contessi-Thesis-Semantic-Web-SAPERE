package it.apice.sapere.api.lsas.values;

import it.apice.sapere.api.SAPEREFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * This class test Property Value for a specific type: Boolean.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestDateTimeValue extends
		AbstractTestPropertyValue<Date> {

	/** Reference to SAPERE API Factory. */
	private final transient SAPEREFactory factory = createFactory();

	@Override
	protected final List<Date> createValues() {
		final List<Date> res = new ArrayList<Date>(2);

		res.add(new Date());

		return res;
	}

	@Override
	protected final PropertyValue<Date> createPropertyValue(
			final Date val) {
		return factory.createPropertyValue(val);
	}

}
