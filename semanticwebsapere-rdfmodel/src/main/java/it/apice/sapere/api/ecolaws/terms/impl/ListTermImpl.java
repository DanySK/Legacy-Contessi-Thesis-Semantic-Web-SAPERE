package it.apice.sapere.api.ecolaws.terms.impl;

import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.terms.Formula;
import it.apice.sapere.api.ecolaws.terms.ListTerm;
import it.apice.sapere.api.lsas.values.PropertyValue;

import java.util.List;

/**
 * <p>
 * Implementation of ListTerm interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            Internal type value
 */
public class ListTermImpl<Type extends PropertyValue<?>> extends
		AnnotatedVarTermImpl<List<Term<Type>>> implements ListTerm<Type> {

	/**
	 * <p>
	 * Builds a new ListTermImpl.
	 * </p>
	 * 
	 * @param varName
	 *            The name of the variable
	 */
	public ListTermImpl(final String varName) {
		super(varName);
	}

	/**
	 * <p>
	 * Builds a new ListTermImpl.
	 * </p>
	 * 
	 * @param varName
	 *            The name of the variable
	 * @param boolCond
	 *            The binding acceptance criteria
	 */
	public ListTermImpl(final String varName,
			final Formula<List<Term<Type>>> boolCond) {
		super(varName, boolCond);
	}

	/**
	 * <p>
	 * Builds a new ListTermImpl.
	 * </p>
	 * 
	 * @param values
	 *            A list of values
	 */
	public ListTermImpl(final List<Term<Type>> values) {
		super(values);
	}

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 * 
	 * @param src
	 *            Term to be cloned
	 */
	public ListTermImpl(final ListTermImpl<Type> src) {
		super(src);
	}
}
