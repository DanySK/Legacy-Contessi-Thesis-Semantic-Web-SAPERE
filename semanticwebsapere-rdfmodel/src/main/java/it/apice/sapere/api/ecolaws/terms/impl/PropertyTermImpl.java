package it.apice.sapere.api.ecolaws.terms.impl;

import it.apice.sapere.api.ecolaws.terms.Formula;
import it.apice.sapere.api.ecolaws.terms.PropertyTerm;
import it.apice.sapere.api.ecolaws.terms.ValueTerm;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.values.URIValue;
import it.apice.sapere.api.lsas.values.impl.URIValueImpl;

/**
 * <p>
 * Implementation of the SDescTerm interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class PropertyTermImpl extends AnnotatedVarTermImpl<PropertyName>
		implements PropertyTerm {

	/**
	 * <p>
	 * Builds a new SDescTermImpl.
	 * </p>
	 * 
	 * @param varName
	 *            The name of the variable
	 */
	public PropertyTermImpl(final String varName) {
		super(varName);
	}

	/**
	 * <p>
	 * Builds a new SDescTermImpl.
	 * </p>
	 * 
	 * @param varName
	 *            The name of the variable
	 * @param boolCond
	 *            The binding acceptance criteria
	 */
	public PropertyTermImpl(final String varName,
			final Formula<PropertyName> boolCond) {
		super(varName, boolCond);
	}

	/**
	 * <p>
	 * Builds a new SDescTermImpl.
	 * </p>
	 * 
	 * @param prop
	 *            The property name
	 */
	public PropertyTermImpl(final PropertyName prop) {
		super(prop);
	}

	@Override
	public final ValueTerm<URIValue> toValueTerm() {
		if (isBound() || isGround()) {
			return new ValueTermImpl<URIValue>(new URIValueImpl(getValue()
					.getValue()));
		}

		throw new IllegalStateException(
				"This term is not bound, so the value cannot be extracted");
	}

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 * 
	 * @param src
	 *            Term to be cloned
	 */
	public PropertyTermImpl(final PropertyTermImpl src) {
		super(src);
	}
}
