package it.apice.sapere.api.lsas.values;

import it.apice.sapere.api.SAPEREFactory;
import it.apice.sapere.api.lsas.LSAid;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class test Property Value for a specific type: LSAid.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestLSAidValue extends
		AbstractTestPropertyValue<LSAid> {

	/** Number of ids to be created. */
	private static final transient int NUM_OF_IDS = 10;

	/** Reference to SAPERE API Factory. */
	private final transient SAPEREFactory factory = createFactory();

	@Override
	protected final List<LSAid> createValues() {
		final List<LSAid> res = new ArrayList<LSAid>(NUM_OF_IDS);

		for (int count = 0; count < NUM_OF_IDS; count++) {
			res.add(factory.createLSAid());
		}

		return res;
	}

	@Override
	protected final PropertyValue<LSAid> createPropertyValue(final LSAid val) {
		return factory.createPropertyValue(val);
	}

}
