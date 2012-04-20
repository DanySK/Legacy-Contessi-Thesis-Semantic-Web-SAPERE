package it.apice.sapere.api.ecolaws.visitors.internal;

import it.apice.sapere.api.ecolaws.ChemicalPattern;
import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.filter.impl.HasFilterImpl;
import it.apice.sapere.api.ecolaws.filter.impl.HasNotFilterImpl;
import it.apice.sapere.api.ecolaws.filters.AssignFilter;
import it.apice.sapere.api.ecolaws.filters.HasFilter;
import it.apice.sapere.api.ecolaws.filters.HasNotFilter;
import it.apice.sapere.api.ecolaws.filters.OpFilter;
import it.apice.sapere.api.ecolaws.terms.ListTerm;
import it.apice.sapere.api.ecolaws.terms.PatternNameTerm;
import it.apice.sapere.api.ecolaws.terms.PropertyTerm;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * This class collect a set of auxiliary functions from TR.WP1.2011.06.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public final class AuxFunctions {

	/** Singleton instance. */
	private static final transient AuxFunctions INSTANCE = new AuxFunctions();

	/**
	 * <p>
	 * Default constructor.
	 * </p>
	 */
	private AuxFunctions() {

	}

	/**
	 * <p>
	 * Lookup Function.
	 * </p>
	 * <p>
	 * Checks if the provided (variable) ListTerm is assigned to some property
	 * in some filter in one of the provided patterns.
	 * </p>
	 * 
	 * @param patterns
	 *            A list of patterns on which execute the lookup
	 * @param x
	 *            The ?x variable to be searched
	 * @return A list of results, reporting PatternName, PropertyTerm and the ?x
	 *         variable itself
	 */
	public List<LookUpResult> look(final ChemicalPattern<?>[] patterns,
			final ListTerm<?> x) {
		final List<LookUpResult> res = new LinkedList<LookUpResult>();
		for (ChemicalPattern<?> patt : patterns) {
			for (Filter filt : patt.filters()) {
				if (checkIfShouldBeReturned(filt, x)) {
					res.add(new LookUpResult(patt.getName(),
							((AssignFilter) filt).getLeftTerm(), x));
				}
			}
		}

		return res;
	}

	/**
	 * <p>
	 * Verifies if the filter is what we're searching for.
	 * </p>
	 * 
	 * @param filt
	 *            The filter to be checked
	 * @param x
	 *            The ListTerm that should be assigned
	 * @return True if fits the requirements, false otherwise
	 */
	private boolean checkIfShouldBeReturned(final Filter filt,
			final ListTerm<?> x) {
		if (filt instanceof AssignFilter) {
			return ((AssignFilter) filt).getRightTerm().equals(x);
		}

		return false;
	}

	/**
	 * <p>
	 * Complementary Function.
	 * </p>
	 * <p>
	 * Retrieves the complement of the provided filter.
	 * </p>
	 * 
	 * @param filt
	 *            The filter to be complemented ({@link AssignFilter})
	 * @return The complement of the provided filter ({@link AssignFilter})
	 */
	public AssignFilter c(final AssignFilter filt) {
		return filt;
	}

	/**
	 * <p>
	 * Complementary Function.
	 * </p>
	 * <p>
	 * Retrieves the complement of the provided filter.
	 * </p>
	 * 
	 * @param filt
	 *            The filter to be complemented ({@link HasFilter})
	 * @return The complement of the provided filter ({@link HasNotFilter})
	 */
	public HasNotFilter c(final HasFilter filt) {
		return new HasNotFilterImpl(filt.getLeftTerm(), filt.getRightTerm());
	}

	/**
	 * <p>
	 * Complementary Function.
	 * </p>
	 * <p>
	 * Retrieves the complement of the provided filter.
	 * </p>
	 * 
	 * @param filt
	 *            The filter to be complemented ({@link HasNotFilter})
	 * @return The complement of the provided filter ({@link HasFilter})
	 */
	public HasFilter c(final HasNotFilter filt) {
		return new HasFilterImpl(filt.getLeftTerm(), filt.getRightTerm());
	}

	/**
	 * <p>
	 * Complementary Function for a list of {@link OpFilter}s.
	 * </p>
	 * 
	 * @param filts
	 *            A list of {@link OpFilter}s
	 * @return A list of complemented {@link OpFilter}s.
	 */
	public List<OpFilter> c(final List<OpFilter> filts) {
		final List<OpFilter> res = new LinkedList<OpFilter>();
		for (OpFilter filt : filts) {
			res.add(c(filt));
		}

		return res;
	}

	/**
	 * <p>
	 * Complementary Function for a list of {@link OpFilter}s.
	 * </p>
	 * 
	 * @param filts
	 *            An array of {@link OpFilter}s
	 * @return An array of complemented {@link OpFilter}s.
	 */
	public OpFilter[] c(final OpFilter[] filts) {
		final List<OpFilter> res = new LinkedList<OpFilter>();
		for (OpFilter filt : filts) {
			res.add(c(filt));
		}

		return res.toArray(new OpFilter[res.size()]);
	}

	/**
	 * <p>
	 * Complementary Function for a generic OpFilter.
	 * </p>
	 * 
	 * @param filt
	 *            An {@link OpFilter} to be complemented
	 * @return The complemented {@link OpFilter}
	 */
	private OpFilter c(final OpFilter filt) {
		if (filt instanceof AssignFilter) {
			return c((AssignFilter) filt);
		}

		if (filt instanceof HasFilter) {
			return c((HasFilter) filt);
		}

		if (filt instanceof HasNotFilter) {
			return c((HasNotFilter) filt);
		}

		throw new IllegalArgumentException("Invalid filter type");
	}

	/**
	 * <p>
	 * Retrieves this class singleton.
	 * </p>
	 * 
	 * @return {@link AuxFunctions} singleton
	 */
	public static AuxFunctions getInstance() {
		return INSTANCE;
	}

	/**
	 * <p>
	 * This class represents the result of a Lookup Procedure as implemented
	 * here (w.r.t TR WP1.2011.06).
	 * </p>
	 * 
	 * @author Paolo Contessi
	 * 
	 */
	public static class LookUpResult {

		/** The pattern name that is involved. */
		private final transient PatternNameTerm y;

		/** The property that is involved. */
		private final transient PropertyTerm t;

		/** The list term assigned to the property in the involved pattern. */
		private final transient ListTerm<?> x;

		/**
		 * <p>
		 * Builds a new {@link LookUpResult}.
		 * </p>
		 * 
		 * @param anY
		 *            The interested pattern
		 * @param aT
		 *            The interested property
		 * @param anX
		 *            The interested list of values
		 */
		public LookUpResult(final PatternNameTerm anY, final PropertyTerm aT,
				final ListTerm<?> anX) {
			if (anY == null) {
				throw new IllegalArgumentException("Invalid y");
			}

			if (aT == null) {
				throw new IllegalArgumentException("Invalid t");
			}

			if (anX == null) {
				throw new IllegalArgumentException("Invalid x");
			}

			y = anY;
			t = aT;
			x = anX;
		}

		/**
		 * <p>
		 * Retrieves the name of the pattern.
		 * </p>
		 * 
		 * @return A {@link PatternNameTerm}
		 */
		public final PatternNameTerm getY() {
			return y;
		}

		/**
		 * <p>
		 * Retrieves the involved property.
		 * </p>
		 * 
		 * @return A {@link PropertyTerm}
		 */
		public final PropertyTerm getT() {
			return t;
		}

		/**
		 * <p>
		 * Retrieves the list of values assigned to the specified property in a
		 * filter of the specified pattern.
		 * </p>
		 * 
		 * @return A {@link ListTerm}
		 */
		public final ListTerm<?> getX() {
			return x;
		}
	}
}
