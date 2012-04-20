package it.apice.sapere.api.lsas.values;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * This class test Property Value for a specific type: Date.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestDateTimeValue extends
		AbstractTestPropertyValue<Date, DateTimeValue> {

	@Override
	protected final List<Date> createValues() {
		final List<Date> res = new ArrayList<Date>(2);

		res.add(new Date());

		return res;
	}

	@Override
	protected final PropertyValue<Date, DateTimeValue> createPropertyValue(
			final Date val) {
		return createFactory().createPropertyValue(val);
	}

}
