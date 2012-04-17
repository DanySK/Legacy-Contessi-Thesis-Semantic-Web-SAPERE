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

import java.util.LinkedList;
import java.util.List;

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
	private final PatternNameTerm name;

	/** Set of pattern's filters. */
	private final List<Filter> filters = new LinkedList<Filter>();

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

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 *
	 * @param src The source
	 */
	protected ChemicalPatternImpl(final ChemicalPattern<ExtenderType> src) {
		try {
			name = (PatternNameTerm) src.getName().clone();
			for (Filter filter : src.filters()) {
				filters.add(filter.clone());
			}
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException("Cannot clone", e);
		}
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

	// @SuppressWarnings("unchecked")
	@Override
	public final ExtenderType match(final Term<?> term1, final Term<?> term2) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
		// Cannot be checked at runtime but holds
		// || assert this instanceof ExtenderType; ||

		// return (ExtenderType) this;
	}

	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder(name.toString());

		if (!filters.isEmpty()) {
			builder.append(":[");

			boolean notFirst = false;
			for (Filter filter : filters) {
				if (notFirst) {
					builder.append("; ");
				}

				builder.append(filter.toString());
				notFirst = true;
			}
			
			builder.append("]");
		}

		return builder.toString();
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filters == null) ? 0 : filters.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ChemicalPatternImpl other = (ChemicalPatternImpl) obj;
		if (filters == null) {
			if (other.filters != null) {
				return false;
			}
		} else if (!filters.equals(other.filters)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public ChemicalPattern<ExtenderType> clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (ChemicalPattern<ExtenderType>) super.clone();
	}
}
