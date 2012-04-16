package it.apice.sapere.api.ecolaws.impl;

import it.apice.sapere.api.ecolaws.ChemicalPattern;
import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.filter.impl.AssignFilterImpl;
import it.apice.sapere.api.ecolaws.filter.impl.ClonesFilterImpl;
import it.apice.sapere.api.ecolaws.filter.impl.ExtendsFilterImpl;
import it.apice.sapere.api.ecolaws.filter.impl.HasFilterImpl;
import it.apice.sapere.api.ecolaws.filter.impl.HasNotFilterImpl;
import it.apice.sapere.api.ecolaws.terms.ListTerm;
import it.apice.sapere.api.ecolaws.terms.PatternNameTerm;
import it.apice.sapere.api.ecolaws.terms.PropertyTerm;
import it.apice.sapere.api.ecolaws.terms.SDescTerm;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * This class provides an implementation of the {@link ChemicalPattern}
 * interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <ExtenderType>
 *            Final type of the pattern (Product or Reactant), used as a trick
 *            for chaining. Its adoption requires the suppression of unchecked
 *            since, at runtime, it's not possible to check against a generic
 *            type.
 */
public abstract class ChemicalPatternImpl<ExtenderType> implements
		ChemicalPattern<ExtenderType> {

	/** Name of the pattern. */
	private final transient PatternNameTerm name;

	/** Set of pattern's filters. */
	private final transient Set<Filter> filters = new HashSet<Filter>();

	/**
	 * <p>
	 * Builds a new {@link ChemicalPatternImpl}.
	 * </p>
	 * 
	 * @param pname
	 *            Pattern's name
	 */
	protected ChemicalPatternImpl(final PatternNameTerm pname) {
		name = pname;
	}

	@Override
	public final PatternNameTerm getName() {
		return name;
	}

	@Override
	public final Filter[] filters() {
		return filters.toArray(new Filter[filters.size()]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ExtenderType has(final PropertyTerm prop,
			final ListTerm<?> values) {
		filters.add(new HasFilterImpl(prop, values));

		// Cannot be checked at runtime but holds
		// || assert this instanceof ExtenderType; ||

		return (ExtenderType) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ExtenderType hasNot(final PropertyTerm prop,
			final ListTerm<?> values) {
		filters.add(new HasNotFilterImpl(prop, values));

		// Cannot be checked at runtime but holds
		// || assert this instanceof ExtenderType; ||

		return (ExtenderType) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ExtenderType assign(final PropertyTerm prop,
			final ListTerm<?> values) {
		filters.add(new AssignFilterImpl(prop, values));

		// Cannot be checked at runtime but holds
		// || assert this instanceof ExtenderType; ||

		return (ExtenderType) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ExtenderType clone(final SDescTerm desc) {
		filters.add(new ClonesFilterImpl(desc));

		// Cannot be checked at runtime but holds
		// || assert this instanceof ExtenderType; ||

		return (ExtenderType) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ExtenderType extend(final SDescTerm desc) {
		filters.add(new ExtendsFilterImpl(desc));

		// Cannot be checked at runtime but holds
		// || assert this instanceof ExtenderType; ||

		return (ExtenderType) this;
	}

	//@SuppressWarnings("unchecked")
	@Override
	public final ExtenderType match(final Term<?> term1, final Term<?> term2) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
		// Cannot be checked at runtime but holds
		// || assert this instanceof ExtenderType; ||

		// return (ExtenderType) this;
	}

}
