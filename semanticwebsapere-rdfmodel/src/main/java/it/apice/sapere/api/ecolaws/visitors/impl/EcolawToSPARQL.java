package it.apice.sapere.api.ecolaws.visitors.impl;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.Product;
import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.Reactant;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.filters.ClonesFilter;
import it.apice.sapere.api.ecolaws.filters.ExtendsFilter;
import it.apice.sapere.api.ecolaws.filters.OpFilter;
import it.apice.sapere.api.ecolaws.formulas.IsFormula;
import it.apice.sapere.api.ecolaws.terms.AnnotatedVarTerm;
import it.apice.sapere.api.ecolaws.terms.PatternNameTerm;
import it.apice.sapere.api.ecolaws.terms.VarTerm;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

/**
 * <p>
 * This class represents an {@link EcolawVisitor}, capable of translating an
 * eco-law into a SPARQL Query.
 * </p>
 * <p>
 * The right part (Products) is not considered by this converter, because not
 * useful for the desired query.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public final class EcolawToSPARQL implements EcolawVisitor {

	/** Eco-law to be translated in SPARQL. */
	private final transient Ecolaw law;

	/** The query builder. */
	private final transient StringBuilder query;

	/**
	 * <p>
	 * Builds a new {@link EcolawToSPARQL}.
	 * </p>
	 * 
	 * @param anEcolaw
	 *            The ecolaw to be translated
	 */
	private EcolawToSPARQL(final Ecolaw anEcolaw) {
		if (anEcolaw == null) {
			throw new IllegalArgumentException("Cannot translate null");
		}

		law = anEcolaw;
		query = new StringBuilder("SELECT DISTINCT * WHERE ");
	}

	@Override
	public void visit(final Ecolaw elaw) {
		elaw.getRate().accept(this);
		for (Reactant react : elaw.reactants()) {
			react.accept(this);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void visit(final Rate<?> rate) {
		final Object rVal = rate.getRateValue();
		if (rVal instanceof VarTerm<?> && ((VarTerm) rVal).isVar()) {
			ruleW((VarTerm<?>) rVal);
		}
	}

	@Override
	public void visit(final Term<?> term) {
		// Ignored because following rules order
	}

	@Override
	public void visit(final Filter filter) {
		// Ignored because following rules order
	}

	@Override
	public void visit(final Product product) {
		// Ignored because producing a SPARQL query for matching phase
	}

	@Override
	public void visit(final Reactant reactant) {
		ruleLP(reactant);
	}

	private void ruleW(final VarTerm<?> var) {
		if (var instanceof AnnotatedVarTerm<?>) {
			ruleW((AnnotatedVarTerm<?>) var);
		}
	}

	private void ruleW(final AnnotatedVarTerm<?> var) {
		if (var.getFormula() instanceof IsFormula) {
			// BIND rule
			query.append(String.format("BIND( %s AS ?%s)", var.getFormula()
					.getRightOp(), var.getVarName()));
		} else {
			// FILTER rule
			query.append(String.format("FILTER(%s)", var.getFormula()
					.getStringRepr()));
		}
	}

	private void ruleTL(final VarTerm<?> term) {
		if (term.isGround()) {
			query.append(term.toString());
		} else {
			query.append("?" + term.getVarName());
		}
	}

	private void ruleLP(final Reactant react) {
		for (Filter filt : react.filters()) {
			if (filt instanceof OpFilter) {
				ruleLO((OpFilter) filt, react.getName());
			} else if (filt instanceof ClonesFilter) {
				ruleLC((ClonesFilter) filt, react.getName());
			} else if (filt instanceof ExtendsFilter) {
				ruleLE((ExtendsFilter) filt, react.getName());
			}
		}
	}

	private void ruleLC(final ClonesFilter filter, final PatternNameTerm pname) {

	}

	private void ruleLE(final ExtendsFilter filter, final PatternNameTerm pname) {

	}

	private void ruleLO(final OpFilter filter, final PatternNameTerm pname) {

	}

	/**
	 * <p>
	 * Creates a SPARQL query.
	 * </p>
	 * 
	 * @return The query
	 */
	private String createQuery() {
		query.append("{ ");
		law.accept(this);

		return query.append(" }").toString();
	}

	/**
	 * <p>
	 * Converts the law in a SPARQL query.
	 * </p>
	 * 
	 * @param law
	 *            The ecolaw to be converted
	 * @return The resulting SPARQL query
	 */
	public static String convert(final Ecolaw law) {
		return new EcolawToSPARQL(law).createQuery();
	}
}
