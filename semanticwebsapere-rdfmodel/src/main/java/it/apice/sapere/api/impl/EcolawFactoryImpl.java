package it.apice.sapere.api.impl;

import it.apice.sapere.api.EcolawFactory;
import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Product;
import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.Reactant;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.impl.ASAPRateImpl;
import it.apice.sapere.api.ecolaws.impl.EcolawImpl;
import it.apice.sapere.api.ecolaws.impl.MarkovianRateImpl;
import it.apice.sapere.api.ecolaws.impl.ProductImpl;
import it.apice.sapere.api.ecolaws.impl.ReactantImpl;
import it.apice.sapere.api.ecolaws.terms.AnnotatedVarTerm;
import it.apice.sapere.api.ecolaws.terms.Formula;
import it.apice.sapere.api.ecolaws.terms.ListTerm;
import it.apice.sapere.api.ecolaws.terms.PatternNameTerm;
import it.apice.sapere.api.ecolaws.terms.PropertyTerm;
import it.apice.sapere.api.ecolaws.terms.SDescTerm;
import it.apice.sapere.api.ecolaws.terms.ValueTerm;
import it.apice.sapere.api.ecolaws.terms.VarTerm;
import it.apice.sapere.api.ecolaws.terms.impl.AnnotatedVarTermImpl;
import it.apice.sapere.api.ecolaws.terms.impl.ListTermImpl;
import it.apice.sapere.api.ecolaws.terms.impl.PatternNameTermImpl;
import it.apice.sapere.api.ecolaws.terms.impl.PropertyTermImpl;
import it.apice.sapere.api.ecolaws.terms.impl.SDescTermImpl;
import it.apice.sapere.api.ecolaws.terms.impl.ValueTermImpl;
import it.apice.sapere.api.ecolaws.terms.impl.VarTermImpl;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.SemanticDescription;
import it.apice.sapere.api.lsas.values.DoubleValue;
import it.apice.sapere.api.lsas.values.PropertyValue;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * This class provides an implementation of the {@link EcolawFactory} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class EcolawFactoryImpl implements EcolawFactory {

	@Override
	public final Ecolaw createEcolaw() {
		return new EcolawImpl();
	}

	@Override
	public final Ecolaw createEcolaw(final String label) {
		return new EcolawImpl(label);
	}

	@Override
	public final Reactant createReactant(final String name) {
		return new ReactantImpl(createPatternNameTerm(name));
	}

	@Override
	public final Reactant createReactant(final PatternNameTerm name) {
		if (name == null) {
			throw new IllegalArgumentException("Invalid name");
		}

		return new ReactantImpl(name);
	}

	@Override
	public final Product createProduct(final String name) {
		return new ProductImpl(createPatternNameTerm(name));
	}

	@Override
	public final Product createProduct(final PatternNameTerm name) {
		if (name == null) {
			throw new IllegalArgumentException("Invalid name");
		}

		return new ProductImpl(name);
	}

	@Override
	public final Rate<?> createMarkovianRate(final double rate) {
		if (rate <= 0.0) {
			throw new IllegalArgumentException(
					"Invalid rate (should be greater than zero");
		}

		return new MarkovianRateImpl(rate);
	}

	@Override
	public final Rate<?> createMarkovianRate(
			final ValueTerm<DoubleValue> rate) {
		return new MarkovianRateImpl(rate);
	}

	@Override
	public final Rate<?> createASAPRate() {
		return new ASAPRateImpl();
	}

	@Override
	public final <Type> VarTerm<Type> createVarTerm(final String name) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("Invalid name");
		}

		return new VarTermImpl<Type>(name);
	}

	@Override
	public final <Type> AnnotatedVarTerm<Type> createAnnotatedVarTerm(
			final String name, final Formula<Type> formula) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("Invalid name");
		}

		return new AnnotatedVarTermImpl<Type>(name, formula);
	}

	@Override
	public final <Type extends PropertyValue<?, ?>> ValueTerm<Type> 
			createTypedValueTerm(final Type value) {
		if (value == null) {
			throw new IllegalArgumentException("Invalid value");
		}

		return new ValueTermImpl<Type>(value);
	}

	@Override
	public final ValueTerm<? extends PropertyValue<?, ?>> createValueTerm(
			final PropertyValue<?, ?> value) {
		if (value == null) {
			throw new IllegalArgumentException("Invalid value");
		}

		return new ValueTermImpl<PropertyValue<?, ?>>(value);
	}

	@Override
	public final <Type extends PropertyValue<?, ?>> ValueTerm<Type> 
	createValueTerm(final String name) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("Invalid name");
		}

		return new ValueTermImpl<Type>(name);
	}

	@Override
	public final <Type extends PropertyValue<?, ?>> ValueTerm<Type> 
			createValueTerm(final String name, final Formula<Type> formula) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("Invalid name");
		}

		return new ValueTermImpl<Type>(name, formula);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <Type extends PropertyValue<?, ?>> ListTerm<Type> 
			createListTerm(final Type... values) {
		if (values == null || values.length == 0) {
			throw new IllegalArgumentException(
					"Invalid values (null or empty)");
		}

		final List<Term<Type>> vterms = new LinkedList<Term<Type>>();
		for (Type val : values) {
			if (val == null) {
				throw new IllegalArgumentException(
						"Cannot add null values to a list");
			}
			vterms.add((ValueTerm<Type>) createValueTerm(val));
		}

		return new ListTermImpl<Type>(vterms);
	}

	@Override
	public final <Type extends PropertyValue<?, ?>> ListTerm<Type> 
			createListTermFromTerms(final Term<Type>... values) {
		if (values == null || values.length == 0) {
			throw new IllegalArgumentException(
					"Invalid values (null or empty)");
		}

		final List<Term<Type>> vterms = new LinkedList<Term<Type>>();
		for (Term<Type> val : values) {
			if (val == null) {
				throw new IllegalArgumentException(
						"Cannot add null values to a list");
			}
			vterms.add(val);
		}

		return new ListTermImpl<Type>(vterms);
	}

	@Override
	public final <Type extends PropertyValue<?, ?>> ListTerm<Type> 
			createListTerm(final String name) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("Invalid name");
		}

		return new ListTermImpl<Type>(name);
	}

	@Override
	public final PropertyTerm createPropertyTerm(final PropertyName pname) {
		if (pname == null) {
			throw new IllegalArgumentException("Invalid property name");
		}

		return new PropertyTermImpl(pname);
	}

	@Override
	public final PropertyTerm createPropertyTerm(final String name) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("Invalid name");
		}

		return new PropertyTermImpl(name);
	}

	@Override
	public final PropertyTerm createPropertyTerm(final String name,
			final Formula<PropertyName> formula) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("Invalid name");
		}

		return new PropertyTermImpl(name, formula);
	}

	@Override
	public final SDescTerm createSDescTerm(final LSA lsa) {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid lsa");
		}

		return new SDescTermImpl(lsa);
	}

	@Override
	public final SDescTerm createSDescTerm(final SemanticDescription sdesc) {
		if (sdesc == null) {
			throw new IllegalArgumentException("Invalid semantic description");
		}

		return new SDescTermImpl(sdesc);
	}

	@Override
	public final SDescTerm createSDescTerm(final String name) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("Invalid name");
		}

		return new SDescTermImpl(name);
	}

	@Override
	public final PatternNameTerm createPatternNameTerm(final String name) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("Invalid name");
		}

		return new PatternNameTermImpl(name);
	}

	@Override
	public final <Type extends PropertyValue<?, ?>> ListTerm<Type> 
			createListTerm(final String name, 
					final Formula<List<Term<Type>>> formula) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("Invalid name");
		}

		return new ListTermImpl<Type>(name, formula);
	}

	@Override
	public final SDescTerm createSDescTerm(final String name,
			final Formula<SemanticDescription> formula) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("Invalid name");
		}

		return new SDescTermImpl(name, formula);
	}

	@Override
	public final SDescTerm createSDescTerm(final PatternNameTerm field) {
		return new SDescTermImpl(field);
	}

}
