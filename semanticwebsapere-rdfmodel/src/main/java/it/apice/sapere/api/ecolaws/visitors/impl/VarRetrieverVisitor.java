package it.apice.sapere.api.ecolaws.visitors.impl;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.Product;
import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.Reactant;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.filters.CopyFilter;
import it.apice.sapere.api.ecolaws.filters.OpFilter;
import it.apice.sapere.api.ecolaws.terms.VarTerm;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * This class models a visitors capable of extracting all variables contained in
 * an eco-law.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class VarRetrieverVisitor implements EcolawVisitor {

	/** Eco-law to be visited. */
	private final transient Ecolaw ecolaw;

	/** Eco-law's variables list. */
	private final transient List<VarTerm<?>> vars;

	/**
	 * <p>
	 * Builds a new Eco-law Visitor whose aim is extracting all variable
	 * contained in it.
	 * </p>
	 * 
	 * @param anEcolaw
	 *            The eco-law to be inspected
	 */
	public VarRetrieverVisitor(final Ecolaw anEcolaw) {
		if (anEcolaw == null) {
			throw new IllegalArgumentException("Invalid Ecolaw to be visited");
		}

		ecolaw = anEcolaw;
		vars = new LinkedList<VarTerm<?>>();
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
		checkAndAddIfVar(rate.getRateValue());
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
		if (filter instanceof CopyFilter) {
			final CopyFilter cfilt = (CopyFilter) filter;
			checkAndAddIfVar(cfilt.getSource());
		} else if (filter instanceof OpFilter) {
			final OpFilter pfilt = (OpFilter) filter;
			checkAndAddIfVar(pfilt.getLeftTerm());
			checkAndAddIfVar(pfilt.getRightTerm());
		}
	}

	@Override
	public final void visit(final Term<?> term) {
		checkAndAddIfVar(term);
	}

	/**
	 * <p>
	 * Retrieves all variables contained in the specified eco-law.
	 * </p>
	 * 
	 * @return The list of all eco-law's variables
	 */
	public final List<VarTerm<?>> getAllVariables() {
		vars.clear();
		ecolaw.accept(this);

		return vars;
	}

	/**
	 * <p>
	 * Checks if the provided object is a Variable of the ecolaw; in that case
	 * adds the variable to the list.
	 * </p>
	 * 
	 * @param obj
	 *            The candidate variable
	 */
	private void checkAndAddIfVar(final Object obj) {
		if (obj instanceof VarTerm<?>) {
			final VarTerm<?> var = (VarTerm<?>) obj;
			if (var.isVar()) {
				vars.add(var);
			}
		}
	}

	/**
	 * <p>
	 * Inspects the provided law and extracts all the variables.
	 * </p>
	 * 
	 * @param law
	 *            The ecolaw to be inspected
	 * @return The list of all eco-law's variables
	 */
	public static final List<VarTerm<?>> retrieveAllVariables(
			final Ecolaw law) {
		return new VarRetrieverVisitor(law).getAllVariables();
	}
}
