package it.apice.sapere.api.lsas.values.impl;

import it.apice.sapere.api.lsas.Property;
import it.apice.sapere.api.lsas.SemanticDescription;
import it.apice.sapere.api.lsas.values.SDescValue;

/**
 * <p>
 * Implementation of SDescValue.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see SDescValue
 */
public class SDescValueImpl extends
		PropertyValueImpl<SemanticDescription, SDescValue> implements
		SDescValue {

	/**
	 * <p>
	 * Builds a new SDescValue.
	 * </p>
	 * 
	 * @param aValue
	 *            The value to be stored
	 */
	public SDescValueImpl(final SemanticDescription aValue) {
		super(aValue);
	}

	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder("[\n");
		for (Property prop : getValue().properties()) {
			builder.append("\t\t").append(prop.toString()).append(";\n");
		}
		
		return builder.append("\t]").toString();
	}
}
