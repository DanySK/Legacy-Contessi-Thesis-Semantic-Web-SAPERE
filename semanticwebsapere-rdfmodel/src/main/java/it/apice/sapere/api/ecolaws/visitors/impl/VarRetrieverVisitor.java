package it.apice.sapere.api.ecolaws.visitors.impl;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.Product;
import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.Reactant;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.terms.VarTerm;

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
public class VarRetrieverVisitor extends AbstractEcolawVisitor {

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
		super(anEcolaw);
		vars = new LinkedList<VarTerm<?>>();
	}
	
	@Override
	protected final void doVisit(final Rate<?> rate) {
		checkAndAddIfVar(rate.getRateValue());
	}

	@Override
	protected final void doVisit(final Term<?> term) {
		checkAndAddIfVar(term);
	}

	@Override
	protected void doVisit(final Filter filter) {
		// Nothing to do...
	}

	@Override
	protected void doVisit(final Product product) {
		// Nothing to do...
	}

	@Override
	protected void doVisit(final Reactant reactant) {
		// Nothing to do...
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
		getEcolaw().accept(this);

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
	final void checkAndAddIfVar(final Object obj) {
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
	 *            The eco-law to be inspected
	 * @return The list of all eco-law's variables
	 */
	public static final List<VarTerm<?>> retrieveAllVariables(
			final Ecolaw law) {
		return new VarRetrieverVisitor(law).getAllVariables();
	}
}
