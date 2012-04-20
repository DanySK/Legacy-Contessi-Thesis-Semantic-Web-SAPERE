package it.apice.sapere.api.ecolaws.visitors.impl;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.Product;
import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.Reactant;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.filters.CopyFilter;
import it.apice.sapere.api.ecolaws.filters.OpFilter;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

/**
 * <p>
 * This abstract class provides basic eco-law navigation capabailities, commonly
 * used while visiting.
 * </p>
 * <p>
 * Some methods are provided in order to allow the insertion of custom
 * behaviours.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractEcolawVisitor implements EcolawVisitor {

	/** Eco-law to be visited. */
	private final transient Ecolaw ecolaw;

	/**
	 * <p>
	 * Builds a new AbstractEcolawVisitor.
	 * </p>
	 * 
	 * @param anEcolaw
	 *            The law to be visited
	 */
	public AbstractEcolawVisitor(final Ecolaw anEcolaw) {
		if (anEcolaw == null) {
			throw new IllegalArgumentException("Invalid Ecolaw to be visited");
		}

		ecolaw = anEcolaw;
	}

	/**
	 * <p>
	 * Retrieves the eco-law that the visitor should visit.
	 * </p>
	 *
	 * @return An eco-law
	 */
	protected final Ecolaw getEcolaw() {
		return ecolaw;
	}

	@Override
	public final void visit(final Ecolaw law) {
		law.getRate().accept(this);
		for (Reactant react : law.reactants()) {
			react.accept(this);
		}

		for (Product prod : law.products()) {
			prod.accept(this);
		}
	}

	@Override
	public final void visit(final Rate<?> rate) {
		doVisit(rate);
	}

	@Override
	public final void visit(final Product prod) {
		prod.getName().accept(this);
		for (Filter filter : prod.filters()) {
			filter.accept(this);
		}
	}

	@Override
	public final void visit(final Reactant react) {
		react.getName().accept(this);
		for (Filter filter : react.filters()) {
			filter.accept(this);
		}
	}

	@Override
	public final void visit(final Filter filter) {
		doVisit(filter);
		if (filter instanceof CopyFilter) {
			final CopyFilter cfilt = (CopyFilter) filter;
			cfilt.getSource().accept(this);
		} else if (filter instanceof OpFilter) {
			final OpFilter pfilt = (OpFilter) filter;
			pfilt.getLeftTerm().accept(this);
			pfilt.getRightTerm().accept(this);
		}
	}

	@Override
	public final void visit(final Term<?> term) {
		doVisit(term);
	}

	/**
	 * <p>
	 * Provides a custom handler for an eco-law's component.
	 * </p>
	 * 
	 * @param rate
	 *            Eco-law's rate
	 */
	protected abstract void doVisit(Rate<?> rate);

	/**
	 * <p>
	 * Provides a custom handler for an eco-law's component.
	 * </p>
	 * 
	 * @param term
	 *            Eco-law's pattern's filter's term
	 */
	protected abstract void doVisit(Term<?> term);

	/**
	 * <p>
	 * Provides a custom handler for an eco-law's component.
	 * </p>
	 * 
	 * @param filter
	 *            Eco-law's pattern's filter
	 */
	protected abstract void doVisit(Filter filter);
	
	/**
	 * <p>
	 * Provides a custom handler for an eco-law's component.
	 * </p>
	 * 
	 * @param product
	 *            Eco-law's product
	 */
	protected abstract void doVisit(Product product);
	
	/**
	 * <p>
	 * Provides a custom handler for an eco-law's component.
	 * </p>
	 * 
	 * @param reactant
	 *            Eco-law's reactant
	 */
	protected abstract void doVisit(Reactant reactant);
}